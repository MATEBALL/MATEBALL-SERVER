package at.mateball.domain.groupmember.core.repository;

import at.mateball.domain.groupmember.core.GroupMemberStatus;

import java.time.LocalDate;
import java.util.List;

public interface GroupMemberRepositoryCustom {
    boolean existsRequest(Long userId, Long groupId);

    boolean isPendingRequestExists(Long matchId, List<Integer> status);

    boolean hasNonFailedRequestOnSameDate(Long userId, LocalDate date);

    boolean hasPreviousFailedRequest(Long userId, Long matchId, GroupMemberStatus status);

    long countMatchingRequests(Long userId, boolean isGroup);

    void createGroupMember(Long userId, Long matchId);

    void updateLeaderStatus(Long userId, Long matchId, int status);

    void updateStatusForAllParticipants(Long matchId, int status);
}
