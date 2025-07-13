package at.mateball.domain.group.core.service;

import at.mateball.domain.gameinformation.core.GameInformation;
import at.mateball.domain.gameinformation.core.repository.GameInformationRepository;
import at.mateball.domain.group.api.dto.*;
import at.mateball.domain.group.api.dto.base.DirectGetBaseRes;
import at.mateball.domain.group.api.dto.base.GroupGetBaseRes;
import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.group.core.repository.GroupRepository;
import at.mateball.domain.group.core.validator.GroupValidator;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.api.dto.base.*;
import at.mateball.domain.groupmember.core.GroupMember;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import at.mateball.domain.matchrequirement.core.service.MatchRequirementService;
import at.mateball.domain.user.core.User;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final static int TOTAL_GROUP_MEMBER = 4;

    private final EntityManager entityManager;

    public GroupService(GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, GameInformationRepository gameInformationRepository, MatchRequirementService matchRequirementService, EntityManager entityManager) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.matchRequirementService = matchRequirementService;
        this.entityManager = entityManager;
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

        validatePermitRequest(userId, group);

        groupMemberRepository.createGroupMember(userId, matchId);

        if (group.isGroup()) {
            groupMemberRepository.updateStatusForAllParticipants(group.getId(), GroupMemberStatus.NEW_REQUEST.getValue());
        } else {
            groupMemberRepository.updateLeaderStatus(
                    group.getLeader().getId(), group.getId(), GroupMemberStatus.NEW_REQUEST.getValue()
            );
        }
    }

    private void validatePermitRequest(Long userId, Group group) {
        LocalDate gameDate = group.getGameInformation().getGameDate();
        boolean isGroup = group.isGroup();

        List<PermitRequestBaseRes> permitRequestBaseRes =
                groupMemberRepository.findPermitValidationData(userId, gameDate);

        if (permitRequestBaseRes.stream().anyMatch(dto ->
                dto.groupId().equals(group.getId()) &&
                        dto.status() == GroupMemberStatus.MATCH_FAILED.getValue()
        )) {
            throw new BusinessException(BusinessErrorCode.ALREADY_FAILED_REQUEST);
        }

        if (permitRequestBaseRes.stream().anyMatch(dto ->
                dto.groupId().equals(group.getId()) &&
                        dto.status() == GroupMemberStatus.AWAITING_APPROVAL.getValue()
        )) {
            throw new BusinessException(BusinessErrorCode.DUPLICATED_REQUEST);
        }

        if (permitRequestBaseRes.stream().anyMatch(dto ->
                !dto.groupId().equals(group.getId()) &&
                        dto.status() == GroupMemberStatus.AWAITING_APPROVAL.getValue()
        )) {
            throw new BusinessException(BusinessErrorCode.ALREADY_HAS_PENDING_REQUEST);
        }

        if (permitRequestBaseRes.stream().anyMatch(dto ->
                dto.status() != GroupMemberStatus.MATCH_FAILED.getValue()
        )) {
            throw new BusinessException(BusinessErrorCode.DUPLICATE_MATCHING_ON_SAME_DATE);
        }

        long pendingRequestCount = permitRequestBaseRes.stream()
                .filter(dto -> dto.isGroup() == isGroup && dto.groupStatus() == GroupStatus.PENDING.getValue())
                .count();
        int requestLimit = isGroup ? GROUP_LIMIT : DIRECT_LIMIT;
        if (pendingRequestCount >= requestLimit) {
            throw new BusinessException(
                    isGroup ? BusinessErrorCode.EXCEED_GROUP_MATCHING_LIMIT
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

    @Transactional
    public CreateMatchRes createMatch(Long userId, Long gameId, String matchType) {
        validateMatchType(matchType);

        boolean isGroup = matchType.equalsIgnoreCase("GROUP");

       GroupMemberBaseRes info = groupMemberRepository.getMatchingInfo(userId, gameId, isGroup)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GAME_NOT_FOUND));

        if (isGroup ? info.totalMatches() >= GROUP_LIMIT : info.totalMatches() >= DIRECT_LIMIT) {
            throw new BusinessException(
                    isGroup ? BusinessErrorCode.EXCEED_GROUP_MATCHING_LIMIT
                            : BusinessErrorCode.EXCEED_DIRECT_MATCHING_LIMIT
            );
        }

        if (LocalDate.now().isAfter(info.gameDate())) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_PAST);
        }

        if (LocalDate.now().isBefore(info.gameDate().minusDays(2))) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_MATCHING_DATE);
        }

        if (info.hasMatchOnSameDate()) {
            throw new BusinessException(BusinessErrorCode.DUPLICATE_MATCHING_ON_SAME_DATE);
        }

        return new CreateMatchRes(createGroup(userId, gameId, isGroup));
    }

    private void validateMatchType(String matchType) {
        if (!"GROUP".equalsIgnoreCase(matchType) && !"DIRECT".equalsIgnoreCase(matchType)) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_MATCH_TYPE);
        }
    }

    private Long createGroup(Long userId, Long gameId, boolean isGroup) {
        User user = entityManager.getReference(User.class, userId);
        GameInformation game = entityManager.getReference(GameInformation.class, gameId);

        Group group = new Group(user, game, LocalDateTime.now(), GroupStatus.PENDING.getValue(), isGroup);
        entityManager.persist(group);

        GroupMember leader = new GroupMember(user, group, true, GroupMemberStatus.PENDING_REQUEST.getValue());
        entityManager.persist(leader);

        return group.getId();
    }
}
