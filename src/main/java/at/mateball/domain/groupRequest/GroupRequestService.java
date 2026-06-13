package at.mateball.domain.groupRequest;

import at.mateball.domain.alarm.common.AlarmType;
import at.mateball.domain.alarm.core.service.AlarmService;
import at.mateball.domain.chatting.core.Chatting;
import at.mateball.domain.chatting.core.service.ChattingV2Service;
import at.mateball.domain.group.api.dto.GroupValidationRes;
import at.mateball.domain.group.api.dto.RequestValidationRes;
import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.group.core.repository.GroupRepository;
import at.mateball.domain.group.core.service.GroupV3Service;
import at.mateball.domain.group.core.validator.GroupValidator;
import at.mateball.domain.group.infrastructure.repository.GroupV3RepositoryCustom;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.api.dto.GroupMatchSummaryRes;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import at.mateball.domain.groupmember.core.service.GroupMemberV3Service;
import at.mateball.exception.BusinessException;
import at.mateball.exception.ConstraintExceptionTranslator;
import at.mateball.exception.code.BusinessErrorCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupRequestService {

    private static final int TOTAL_GROUP_MEMBER = 4;

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupV3RepositoryCustom groupV3RepositoryCustom;
    private final AlarmService alarmService;
    private final GroupMemberV3Service groupMemberV3Service;
    private final GroupRequestValidator groupRequestValidator;
    private final ConstraintExceptionTranslator constraintExceptionTranslator;
    private final ChattingV2Service chattingV2Service;
    private final EntityManager entityManager;

    @Transactional
    public void createRequest(Long userId, Long groupId) {
        Group group = groupRepository.findGroupWithLock(groupId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND));

        GroupValidationRes validatedGroup = GroupValidationRes.from(group);
        RequestValidationRes validationData = groupV3RepositoryCustom.getValidation(userId, groupId);
        groupRequestValidator.validateCreateRequest(userId, validatedGroup, validationData);

        try {
            groupMemberRepository.createGroupMemberV3(userId, groupId);
            groupMemberRepository.updateMemberStatus(validatedGroup.leaderId(), groupId, GroupMemberStatus.NEW_REQUEST.getValue());
            alarmService.createAlarm(validatedGroup.leaderId(), AlarmType.NEW_REQUEST, groupId);
            entityManager.flush();
        } catch (Exception e) {
            throw constraintExceptionTranslator.translate(e);
        }
    }

    @Transactional
    public void permitRequest(Long userId, Long groupId) {
        Group group = getValidatedGroup(userId, groupId);
        boolean isGroup = group.isGroup();

        GroupMatchSummaryRes summary = groupMemberRepository.getMatchSummary(groupId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.REQUEST_NOT_FOUND));
        Long requesterId = Optional.ofNullable(summary.requesterId())
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.REQUESTER_NOT_FOUND));

        if (isGroup) processGroupMatch(group, userId, requesterId, groupId, summary);
        else processDirectMatch(group, userId, requesterId, groupId);
    }

    private Group getValidatedGroup(Long userId, Long groupId) {
        Group group = groupRepository.findGroupWithLock(groupId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND));

        GroupValidator.validate(group);

        if (!group.getLeader().getId().equals(userId)) {
            throw new BusinessException(BusinessErrorCode.NOT_MATCH_LEADER);
        }

        return group;
    }

    private void processDirectMatch(Group group, Long userId, Long requesterId, Long groupId) {
        groupMemberV3Service.updateMemberStatusMatched(userId, groupId);
        groupMemberV3Service.updateMemberStatusMatched(requesterId, groupId);

        updateGroupStatusCompleted(groupId);
        assignChattingIfAbsent(group);

        alarmService.notifyMatched(userId, groupId);
        alarmService.notifyMatched(requesterId, groupId);
    }

    private void processGroupMatch(Group group, Long userId, Long requesterId, Long groupId, GroupMatchSummaryRes summary) {
        groupMemberV3Service.updateMemberStatusMatched(requesterId, groupId);

        boolean isFull = isGroupFull(summary);

        if (isFull) {
            groupMemberV3Service.updateMemberStatusMatched(userId, groupId);
            updateGroupStatusCompleted(groupId);
            assignChattingIfAbsent(group);

            alarmService.notifyMatched(userId, groupId);
            alarmService.notifyMatched(requesterId, groupId);
        } else {
            groupMemberV3Service.updateLeaderStatusPending(userId, groupId);
            alarmService.readAllAlarms(userId);
            alarmService.notifyApproved(requesterId, groupId);
        }
    }

    public void updateGroupStatusCompleted(Long groupId) { // 순환참조 발생으로 request service 에 위치
        groupRepository.updateGroupStatus(groupId, GroupStatus.COMPLETED.getValue());
    }

    private void assignChattingIfAbsent(Group group) {
        if (group.getChatting() != null) {
            return;
        }

        Chatting chatting = chattingV2Service.assignChatting();
        groupRepository.assignChattingToGroup(group.getId(), chatting.getId());
    }

    private boolean isGroupFull(GroupMatchSummaryRes summary) {
        long totalMatched = summary.matchedParticipants() + 1;
        return totalMatched == TOTAL_GROUP_MEMBER;
    }

    @Transactional
    public void rejectRequest(Long userId, Long matchId) {
        getValidatedGroup(userId, matchId);

        GroupMatchSummaryRes summary = groupMemberV3Service.getMatchSummary(matchId);
        Long requesterId = Optional.ofNullable(summary.requesterId())
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.REQUESTER_NOT_FOUND));

        groupMemberV3Service.updateLeaderStatusPending(userId, matchId);
        groupMemberV3Service.updateMemberStatusFailed(requesterId, matchId);

        alarmService.readAllAlarms(userId);
    }
}
