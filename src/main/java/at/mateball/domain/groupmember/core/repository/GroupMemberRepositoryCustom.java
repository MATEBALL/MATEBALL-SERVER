package at.mateball.domain.groupmember.core.repository;

import at.mateball.domain.groupmember.core.GroupMemberStatus;

import java.util.List;

public interface GroupMemberRepositoryCustom {
    void createGroupMember(Long userId, Long matchId);

    long countMatchingRequests(Long userId, boolean isGroup);

    void updateLeaderStatus(Long userId, Long matchId, int status);

    void updateStatusForAllParticipants(Long matchId, int status);

    boolean isPendingRequestExists(Long matchId, List<Integer> status);

    boolean hasPreviousFailedRequest(Long userId, Long matchId, GroupMemberStatus status);
}
