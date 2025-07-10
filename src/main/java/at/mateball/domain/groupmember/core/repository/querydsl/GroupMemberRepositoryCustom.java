package at.mateball.domain.groupmember.core.repository.querydsl;


import at.mateball.domain.group.api.dto.base.GroupMemberStatusCounts;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.api.dto.GroupMemberCountRes;
import at.mateball.domain.groupmember.api.dto.base.DetailMatchingBaseRes;
import at.mateball.domain.groupmember.api.dto.base.DirectStatusBaseRes;
import at.mateball.domain.groupmember.api.dto.base.GroupMemberBaseRes;
import at.mateball.domain.groupmember.api.dto.base.GroupStatusBaseRes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface GroupMemberRepositoryCustom {
    Map<Long, Integer> findGroupMemberCountMap(List<Long> groupIds);

    Map<Long, List<String>> findGroupMemberImgMap(List<Long> groupIds);

    List<DirectStatusBaseRes> findDirectMatchingsByUserAndGroupStatus(Long userId, int groupStatus);

    List<DirectStatusBaseRes> findAllDirectMatchingsByUser(Long userId);

    List<GroupStatusBaseRes> findGroupMatchingsByUser(Long userId);

    List<GroupStatusBaseRes> findGroupMatchingsByUserAndStatus(Long userId, int groupStatus);

    GroupMemberCountRes countGroupMember(Long groupId);

    List<DetailMatchingBaseRes> findGroupMatesByMatchId(Long userId, Long matchId);

    boolean existsRequest(Long userId, Long groupId);

    boolean isPendingRequestExists(Long matchId, List<Integer> status);

    boolean hasNonFailedRequestOnSameDate(Long userId, LocalDate date);

    boolean hasPreviousFailedRequest(Long userId, Long matchId, GroupMemberStatus status);

    long countMatchingRequests(Long userId, boolean isGroup);

    void createGroupMember(Long userId, Long matchId);

    void updateLeaderStatus(Long userId, Long matchId, int status);

    void updateStatusForAllParticipants(Long matchId, int status);

    void updateMemberStatus(Long userId, Long groupId, int status);

    void updateStatusAndParticipant(Long userId, Long groupId, int status);

    void updateStatusForApprovedMembers(Long groupId, int status);

    void updateStatusForAllMembers(Long groupId, int status);

    long countParticipants(Long groupId);

    GroupMemberStatusCounts countGroupMemberStatus(Long groupId);

    Long findRequesterId(Long groupId);

    boolean isUserParticipant(Long userId, Long groupId);

    List<GroupMemberBaseRes> getGroupMember(Long groupId);

    void updateAllGroupMemberStatus(Long groupId, Long requesterId);

    boolean updateMyStatusFromApprovedToRequest(Long userId, Long matchId);
}
