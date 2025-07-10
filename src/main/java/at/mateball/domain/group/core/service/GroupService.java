package at.mateball.domain.group.core.service;

import at.mateball.domain.group.api.dto.*;
import at.mateball.domain.group.api.dto.base.DirectGetBaseRes;
import at.mateball.domain.group.api.dto.base.GroupGetBaseRes;
import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.group.core.repository.GroupRepository;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.api.dto.base.GroupMemberBaseRes;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import at.mateball.domain.matchrequirement.core.service.MatchRequirementService;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static at.mateball.domain.group.core.validator.DateValidator.validate;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final MatchRequirementService matchRequirementService;

    private final static int DIRECT_LIMIT = 3;
    private final static int GROUP_LIMIT = 2;

    public GroupService(GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, MatchRequirementService matchRequirementService) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.matchRequirementService = matchRequirementService;
    }

    public DirectCreateRes getDirectMatching(Long userId, Long matchId) {
        DirectCreateRes result = groupRepository.findDirectCreateResults(userId, matchId);

        if (result == null) {
            throw new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND);
        }

        return result;
    }

    public DirectGetListRes getDirects(Long userId, LocalDate date) {
        validate(date);

        List<DirectGetBaseRes> result = groupRepository.findDirectGroupsByDate(userId, date);

        Map<Long, Integer> matchRateMap = matchRequirementService.getMatchings(userId).stream()
                .collect(Collectors.toMap(
                        MatchingScoreDto::targetUserId,
                        MatchingScoreDto::totalScore
                ));

        List<DirectGetRes> directGetRes = result.stream()
                .map(res -> res.withMatchRate(matchRateMap.getOrDefault(res.leaderId(), 0)))
                .map(DirectGetRes::from)
                .sorted(Comparator.comparingInt(DirectGetRes::matchRate).reversed())
                .toList();

        return new DirectGetListRes(directGetRes);
    }

    public GroupCreateRes getGroupMatching(Long userId, Long matchId) {
        return groupRepository.findGroupCreateRes(userId, matchId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND));
    }

    @Transactional
    public void requestMatching(Long userId, Long matchId) {
        Group group = groupRepository.findById(matchId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND));

        validateRequest(userId, group);

        groupMemberRepository.createGroupMember(userId, matchId);

        if (group.isGroup()) {
            groupMemberRepository.updateStatusForAllParticipants(group.getId(), GroupMemberStatus.NEW_REQUEST.getValue());
        } else {
            groupMemberRepository.updateLeaderStatus(
                    group.getLeader().getId(), group.getId(), GroupMemberStatus.NEW_REQUEST.getValue()
            );
        }
    }

    private void validateRequest(Long userId, Group group) {

        boolean hasFailed = groupMemberRepository.hasPreviousFailedRequest(
                userId, group.getId(), GroupMemberStatus.MATCH_FAILED
        );
        if (hasFailed) {
            throw new BusinessException(BusinessErrorCode.ALREADY_FAILED_REQUEST);
        }

        boolean alreadyRequested = groupMemberRepository.existsRequest(userId, group.getId());
        if (alreadyRequested) {
            throw new BusinessException(BusinessErrorCode.DUPLICATED_REQUEST);
        }

        boolean hasPendingRequest = groupMemberRepository.isPendingRequestExists(
                group.getId(),
                List.of(
                        GroupMemberStatus.NEW_REQUEST.getValue(),
                        GroupMemberStatus.AWAITING_APPROVAL.getValue()
                )
        );
        if (hasPendingRequest) {
            throw new BusinessException(BusinessErrorCode.ALREADY_HAS_PENDING_REQUEST);
        }

        LocalDate gameDate = group.getGameInformation().getGameDate();
        if (groupMemberRepository.hasNonFailedRequestOnSameDate(userId, gameDate)) {
            throw new BusinessException(BusinessErrorCode.DUPLICATE_REQUEST_ON_SAME_DATE);
        }

        int limit = group.isGroup() ? GROUP_LIMIT : DIRECT_LIMIT;
        long count = groupMemberRepository.countMatchingRequests(userId, group.isGroup());
        if (count >= limit) {
            throw new BusinessException(
                    group.isGroup() ? BusinessErrorCode.EXCEED_GROUP_MATCHING_LIMIT
                            : BusinessErrorCode.EXCEED_DIRECT_MATCHING_LIMIT
            );
        }
    }

    public GroupGetListRes getGroups(Long userId, LocalDate date) {
        validate(date);

        List<GroupGetBaseRes> baseList = groupRepository.findGroupsWithBaseInfo(date);
        List<Long> groupIds = baseList.stream().map(GroupGetBaseRes::id).toList();

        Map<Long, Integer> matchRateMap = matchRequirementService.getMatchings(userId).stream()
                .collect(Collectors.toMap(
                        MatchingScoreDto::targetUserId,
                        MatchingScoreDto::totalScore
                ));

        Map<Long, Integer> countMap = groupMemberRepository.findGroupMemberCountMap(groupIds);
        Map<Long, List<String>> imgMap = groupMemberRepository.findGroupMemberImgMap(groupIds);

        List<GroupGetRes> result = baseList.stream()
                .map(base -> {
                    int count = countMap.getOrDefault(base.id(), 1);
                    List<String> imgs = imgMap.getOrDefault(base.id(), List.of());
                    int score = matchRateMap.getOrDefault(base.id(), 0);
                    int avg = Math.round((float) score / count);
                    return GroupGetRes.from(base, avg, count, imgs);
                })
                .sorted(Comparator.comparingInt(GroupGetRes::matchRate).reversed())
                .toList();

        return new GroupGetListRes(result);
    }

    @Transactional
    public void permitRequest(Long userId, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND));

        if (GroupStatus.from(group.getStatus()) == GroupStatus.COMPLETED) {
            throw new BusinessException(BusinessErrorCode.ALREADY_COMPLETED_GROUP);
        }

        if (!groupMemberRepository.isUserParticipant(userId, groupId)) {
            throw new BusinessException(BusinessErrorCode.NOT_GROUP_MEMBER);
        }

        boolean isGroup = group.isGroup();
        Long requesterId = groupMemberRepository.findRequesterId(groupId);

        if (!isGroup) {
            groupMemberRepository.updateMemberStatus(userId, groupId, GroupMemberStatus.MATCHED.getValue());
            groupMemberRepository.updateStatusAndParticipant(requesterId, groupId, GroupMemberStatus.MATCHED.getValue());
            groupRepository.updateGroupStatus(groupId, GroupStatus.COMPLETED.getValue());
            return;
        }

        groupMemberRepository.updateMemberStatus(userId, groupId, GroupMemberStatus.AWAITING_APPROVAL.getValue());

        long totalGroupMembers = groupMemberRepository.countTotalGroupMembersExceptRequester(groupId);
        long approvedCount = groupMemberRepository.countMembersWithStatus(groupId, GroupMemberStatus.AWAITING_APPROVAL);
        if (approvedCount < totalGroupMembers) {
            return;
        }

        groupMemberRepository.updateStatusAndParticipant(requesterId, groupId, GroupMemberStatus.APPROVED.getValue());
        groupMemberRepository.updateStatusForApprovedMembers(groupId, GroupMemberStatus.PENDING_REQUEST.getValue());

        long participantCount = groupMemberRepository.countParticipants(groupId);
        if (participantCount == 4) {
            groupMemberRepository.updateStatusForAllMembers(groupId, GroupMemberStatus.MATCHED.getValue());
            groupRepository.updateGroupStatus(groupId, GroupStatus.COMPLETED.getValue());
        }
    }

    @Transactional
    public void rejectRequest(Long userId, Long matchId) {
        List<GroupMemberBaseRes> members = groupMemberRepository.getGroupMember(matchId);

        boolean isGroupMember = members.stream()
                .anyMatch(member -> member.userId().equals(userId));
        if (!isGroupMember) {
            throw new BusinessException(BusinessErrorCode.NOT_GROUP_MEMBER);
        }

        Long requesterId = members.stream()
                .filter(member -> member.status() == GroupMemberStatus.AWAITING_APPROVAL.getValue())
                .map(GroupMemberBaseRes::userId)
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.USER_NOT_FOUND));

        groupMemberRepository.updateAllGroupMemberStatus(matchId, requesterId);
    }
}
