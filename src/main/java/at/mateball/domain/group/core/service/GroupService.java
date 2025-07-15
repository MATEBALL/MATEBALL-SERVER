package at.mateball.domain.group.core.service;

import at.mateball.domain.group.api.dto.*;
import at.mateball.domain.group.api.dto.base.DirectGetBaseRes;
import at.mateball.domain.group.api.dto.base.GroupGetBaseRes;
import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.core.GroupExecutor;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.group.core.repository.GroupRepository;
import at.mateball.domain.group.core.validator.AgeValidator;
import at.mateball.domain.group.core.validator.DateValidator;
import at.mateball.domain.group.core.validator.GroupValidator;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.api.dto.GroupMemberRes;
import at.mateball.domain.groupmember.api.dto.base.DirectMatchBaseRes;
import at.mateball.domain.groupmember.api.dto.base.GroupInformationRes;
import at.mateball.domain.groupmember.api.dto.base.GroupMatchBaseRes;
import at.mateball.domain.groupmember.api.dto.base.RejectGroupMemberBaseRes;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import at.mateball.domain.matchrequirement.core.service.MatchRequirementService;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static at.mateball.domain.group.core.MatchType.DIRECT;
import static at.mateball.domain.group.core.MatchType.GROUP;
import static at.mateball.domain.group.core.validator.DateValidator.validate;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final MatchRequirementService matchRequirementService;
    private final GroupExecutor groupExecutor;
    private final AgeValidator ageValidator;

    private final static int MAX_DIRECT_COUNT = 3;
    private final static int MAX_GROUP_COUNT = 2;
    private final static int TOTAL_GROUP_MEMBER = 4;

    public GroupService(GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, MatchRequirementService matchRequirementService, GroupExecutor groupExecutor, AgeValidator ageValidator) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.matchRequirementService = matchRequirementService;
        this.groupExecutor = groupExecutor;
        this.ageValidator = ageValidator;
    }

    public DirectCreateRes getDirectMatching(Long userId, Long matchId) {
        DirectCreateRes result = groupRepository.findDirectCreateResults(userId, matchId);

        if (result == null) {
            throw new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND);
        }

        if (result.team() == null || result.style() == null) {
            throw new BusinessException(BusinessErrorCode.MATCH_REQUIREMENT_NOT_FOUND);
        }

        return result;
    }

    public DirectGetListRes getDirects(Long userId, LocalDate date) {
        validate(date);

        List<DirectGetBaseRes> result = groupRepository.findDirectGroupsByDate(userId, date);

        List<DirectGetBaseRes> filtered = result.stream()
                .filter(res -> res.team() != null && res.style() != null)
                .filter(res-> ageValidator.isAgeWithinRange(userId,res.birthYear()))
                .toList();

        Map<Long, Integer> matchRateMap = matchRequirementService.getMatchings(userId).stream()
                .collect(Collectors.toMap(
                        MatchingScoreDto::targetUserId,
                        MatchingScoreDto::totalScore
                ));

        List<DirectGetRes> directGetRes = filtered.stream()
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
    public void createRequest(Long userId, Long matchId) {
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
        LocalDate gameDate = group.getGameInformation().getGameDate();
        boolean isGroup = group.isGroup();

        List<GroupInformationRes> groupInformationRes =
                groupMemberRepository.findGroupInformation(userId, gameDate);

        groupInformationRes.stream()
                .map(GroupInformationRes::gameDate)
                .forEach(DateValidator::validate);

        if (groupInformationRes.stream().anyMatch(dto ->
                dto.groupId().equals(group.getId()) &&
                        dto.status() == GroupMemberStatus.MATCH_FAILED.getValue()
        )) {
            throw new BusinessException(BusinessErrorCode.ALREADY_FAILED_REQUEST);
        }

        if (groupInformationRes.stream().anyMatch(dto ->
                dto.groupId().equals(group.getId()) &&
                        dto.groupStatus() == GroupStatus.PENDING.getValue()
        )) {
            throw new BusinessException(BusinessErrorCode.DUPLICATED_REQUEST);
        }

        if (groupInformationRes.stream().anyMatch(dto ->
                dto.status() != GroupMemberStatus.MATCH_FAILED.getValue()
                        && dto.gameDate().equals(gameDate)
        )) {
            throw new BusinessException(BusinessErrorCode.DUPLICATE_MATCHING_ON_SAME_DATE);
        }

        long pendingRequestCount = groupInformationRes.stream()
                .filter(dto -> dto.isGroup() == isGroup && dto.groupStatus() == GroupStatus.PENDING.getValue())
                .count();
        int requestLimit = isGroup ? MAX_GROUP_COUNT : MAX_DIRECT_COUNT;
        if (pendingRequestCount >= requestLimit) {
            throw new BusinessException(
                    isGroup ? BusinessErrorCode.EXCEED_GROUP_MATCHING_LIMIT
                            : BusinessErrorCode.EXCEED_DIRECT_MATCHING_LIMIT
            );
        }

        if (groupMemberRepository.findAllGroupMemberInfo(group.getId()).stream()
                .anyMatch(m -> m.status() == GroupMemberStatus.AWAITING_APPROVAL.getValue() || m.status() == GroupMemberStatus.NEW_REQUEST.getValue())) {
            throw new BusinessException(BusinessErrorCode.ALREADY_HAS_PENDING_REQUEST);
        }
    }

    public GroupGetListRes getGroups(Long userId, LocalDate date) {
        validate(date);

        List<GroupGetBaseRes> groupBases = groupRepository.findGroupsWithBaseInfo(userId, date);

        List<GroupGetBaseRes> ageFiltered = groupBases.stream()
                .filter(groupBase -> ageValidator.isAgeWithinRange(userId, groupBase.birthYear()))
                .toList();

        if (ageFiltered.isEmpty()) {
            throw new BusinessException(BusinessErrorCode.NOT_ALLOWED_AGE);
        }

        List<Long> groupIds = groupBases.stream().map(GroupGetBaseRes::id).toList();

        Map<Long, List<Long>> groupToUserIds = groupMemberRepository.findUserIdsGroupedByGroupIds(groupIds);
        Map<Long, Integer> userMatchScores = matchRequirementService.getMatchings(userId).stream()
                .collect(Collectors.toMap(MatchingScoreDto::targetUserId, MatchingScoreDto::totalScore));

        Map<Long, Integer> groupToMemberCount = groupMemberRepository.findGroupMemberCountMap(groupIds);
        Map<Long, List<String>> groupToImgUrls = groupMemberRepository.findGroupMemberImgMap(groupIds);

        Map<Long, Integer> groupToAvgMatchRate = calculateGroupAvgMatchRates(groupToUserIds, userMatchScores);

        List<GroupGetRes> result = groupBases.stream()
                .map(groupBase -> {
                    Long groupId = groupBase.id();
                    int count = groupToMemberCount.getOrDefault(groupId, 1);
                    List<String> imgs = groupToImgUrls.getOrDefault(groupId, List.of());
                    int avgMatchRate = groupToAvgMatchRate.getOrDefault(groupId, 0);

                    return GroupGetRes.from(groupBase, avgMatchRate, count, imgs);
                })
                .sorted(Comparator.comparingInt(GroupGetRes::matchRate).reversed())
                .toList();

        return new GroupGetListRes(result);
    }

    private Map<Long, Integer> calculateGroupAvgMatchRates(Map<Long, List<Long>> groupUserMap,
                                                           Map<Long, Integer> matchRateMap) {
        Map<Long, Integer> result = new HashMap<>();

        for (Map.Entry<Long, List<Long>> entry : groupUserMap.entrySet()) {
            Long groupId = entry.getKey();
            List<Long> userIds = entry.getValue();

            int total = 0, count = 0;

            for (Long userId : userIds) {
                Integer score = matchRateMap.get(userId);
                if (score != null) {
                    total += score;
                    count++;
                }
            }

            int avg = count > 0 ? (int) Math.round((double) total / count) : 0;
            result.put(groupId, avg);
        }

        return result;
    }

    @Transactional
    public void permitRequest(Long userId, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND));

        if (!groupMemberRepository.isUserParticipant(userId, groupId)) {
            throw new BusinessException(BusinessErrorCode.NOT_GROUP_MEMBER);
        }

        GroupValidator.validate(group);

        if (!group.isGroup()) {
            processDirect(userId, groupId);
        } else {
            processGroup(userId, groupId);
        }
    }

    private void processDirect(Long userId, Long groupId) {
        List<DirectMatchBaseRes> members = groupMemberRepository.findDirectMatchMembers(groupId);

        Long requesterId = members.stream()
                .filter(m -> m.status() == GroupMemberStatus.AWAITING_APPROVAL.getValue())
                .map(DirectMatchBaseRes::userId)
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.REQUESTER_NOT_FOUND));

        groupMemberRepository.updateStatusesForDirectMatching(userId, requesterId, groupId, GroupMemberStatus.MATCHED.getValue());
        groupRepository.updateGroupStatus(groupId, GroupStatus.COMPLETED.getValue());
    }

    private void processGroup(Long userId, Long groupId) {
        groupMemberRepository.updateMemberStatus(userId, groupId, GroupMemberStatus.AWAITING_APPROVAL.getValue());
        List<GroupMatchBaseRes> members = groupMemberRepository.findAllGroupMemberInfo(groupId);

        Long requesterId = null;
        Long totalParticipants = 0L;
        Long awaitingApprovals = 0L;

        for (GroupMatchBaseRes m : members) {
            int status = m.status();
            boolean isParticipant = m.isParticipant();

            if (status == GroupMemberStatus.AWAITING_APPROVAL.getValue()) {
                if (!isParticipant && requesterId == null) {
                    requesterId = m.userId();
                }
                if (isParticipant) {
                    awaitingApprovals++;
                }
                totalParticipants++;
            } else if (status == GroupMemberStatus.NEW_REQUEST.getValue()) {
                totalParticipants++;
            }
        }

        if (requesterId == null) {
            throw new BusinessException(BusinessErrorCode.REQUESTER_NOT_FOUND);
        }

        if (awaitingApprovals < totalParticipants - 1) {
            return;
        }

        groupMemberRepository.updateStatusAfterRequestApproval(
                groupId, requesterId, GroupMemberStatus.APPROVED.getValue()
        );

        groupMemberRepository.updateStatusForApprovedMembers(groupId, GroupMemberStatus.PENDING_REQUEST.getValue());

        Long participantCount = members.stream()
                .filter(GroupMatchBaseRes::isParticipant)
                .count();

        if (participantCount + 1 == TOTAL_GROUP_MEMBER) {
            groupMemberRepository.updateStatusForAllMembers(groupId, GroupMemberStatus.MATCHED.getValue());
            groupRepository.updateGroupStatus(groupId, GroupStatus.COMPLETED.getValue());
        }
    }

    @Transactional
    public void rejectRequest(Long userId, Long matchId) {
        List<RejectGroupMemberBaseRes> members = groupMemberRepository.getGroupMember(matchId);

        boolean isGroupMember = members.stream()
                .anyMatch(member -> member.userId().equals(userId));
        if (!isGroupMember) {
            throw new BusinessException(BusinessErrorCode.NOT_GROUP_MEMBER);
        }

        Long requesterId = members.stream()
                .filter(member -> member.status() == GroupMemberStatus.AWAITING_APPROVAL.getValue())
                .map(RejectGroupMemberBaseRes::userId)
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.USER_NOT_FOUND));

        groupMemberRepository.updateAllGroupMemberStatus(matchId, requesterId);
    }

    public CreateMatchRes createMatch(Long userId, Long gameId, String matchType) {
        validateMatchType(matchType);

        boolean isGroup = matchType.equalsIgnoreCase(String.valueOf(GROUP));

        GroupMemberRes info = groupMemberRepository.getMatchingInfo(userId, gameId, isGroup)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GAME_NOT_FOUND));

        validate(info.gameDate());

        if (info.hasMatchOnSameDate()) {
            throw new BusinessException(BusinessErrorCode.DUPLICATE_MATCHING_ON_SAME_DATE);
        }

        if (isGroup ? info.totalMatches() >= MAX_GROUP_COUNT : info.totalMatches() >= MAX_DIRECT_COUNT) {
            throw new BusinessException(
                    isGroup ? BusinessErrorCode.EXCEED_GROUP_MATCHING_LIMIT
                            : BusinessErrorCode.EXCEED_DIRECT_MATCHING_LIMIT
            );
        }

        return new CreateMatchRes(groupExecutor.createGroup(userId, gameId, isGroup));
    }

    private void validateMatchType(String matchType) {
        if (!String.valueOf(GROUP).equalsIgnoreCase(matchType) && !String.valueOf(DIRECT).equalsIgnoreCase(matchType)) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_MATCH_TYPE);
        }
    }
}
