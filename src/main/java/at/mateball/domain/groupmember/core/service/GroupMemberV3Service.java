package at.mateball.domain.groupmember.core.service;

import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
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
}
