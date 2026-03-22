package at.mateball.domain.groupmember.core.service;

import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.api.dto.GroupMatchSummaryRes;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupMemberV3Service {

    private final GroupMemberRepository groupMemberRepository;

    public void updateMemberStatusMatched(Long userId, Long groupId) {
        groupMemberRepository.updateStatusAndParticipant(userId, groupId, GroupMemberStatus.MATCHED.getValue());
    }

    public void updateLeaderStatusPending(Long userId, Long groupId) {
        groupMemberRepository.updateMemberStatus(userId, groupId, GroupMemberStatus.PENDING_REQUEST.getValue());
    }

    public void updateMemberStatusFailed(Long userId, Long groupId) {
        groupMemberRepository.updateMemberStatus(userId, groupId, GroupMemberStatus.MATCH_FAILED.getValue());
    }

    public GroupMatchSummaryRes getMatchSummary(Long groupId) {
        return groupMemberRepository.getMatchSummary(groupId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.REQUEST_NOT_FOUND));
    }
}
