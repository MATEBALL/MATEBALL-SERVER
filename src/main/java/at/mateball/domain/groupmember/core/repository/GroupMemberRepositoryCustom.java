package at.mateball.domain.groupmember.core.repository;

import java.util.List;

public interface GroupMemberRepositoryCustom {
    void createGroupMember(Long userId,Long groupId);

    long countMatchingRequests(Long userId, boolean isGroup);

    void updateLeaderStatus(Long userId, Long groupId, int status);

    void updateStatusForAllParticipants(Long groupId, int status);

    boolean isPendingRequestExists(Long groupId, List<Integer> statuses);
}
