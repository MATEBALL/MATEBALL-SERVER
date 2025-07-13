package at.mateball.domain.groupmember.core.repository.querydsl;


import at.mateball.domain.groupmember.api.dto.GroupMemberCountRes;
import at.mateball.domain.groupmember.api.dto.base.*;
import at.mateball.domain.groupmember.api.dto.base.DetailMatchingBaseRes;
import at.mateball.domain.groupmember.api.dto.base.DirectStatusBaseRes;
import at.mateball.domain.groupmember.api.dto.base.GroupMemberBaseRes;
import at.mateball.domain.groupmember.api.dto.base.GroupStatusBaseRes;
import at.mateball.domain.groupmember.api.dto.base.PermitRequestBaseRes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GroupMemberRepositoryCustom {
    Map<Long, Integer> findGroupMemberCountMap(List<Long> groupIds);

    Map<Long, List<String>> findGroupMemberImgMap(List<Long> groupIds);

    List<DirectStatusBaseRes> findDirectMatchingsByUserAndGroupStatus(Long userId, int groupStatus);

    List<DirectStatusBaseRes> findAllDirectMatchingsByUser(Long userId);

    List<GroupStatusBaseRes> findGroupMatchingsByUser(Long userId);

    List<GroupStatusBaseRes> findGroupMatchingsByUserAndStatus(Long userId, int groupStatus);

    GroupMemberCountRes countGroupMember(Long groupId);

    List<DetailMatchingBaseRes> findGroupMatesByMatchId(Long userId, Long matchId);

    List<DirectMatchBaseRes> findDirectMatchMembers(Long groupId);

    void createGroupMember(Long userId, Long matchId);

    void updateLeaderStatus(Long userId, Long matchId, int status);

    void updateStatusForAllParticipants(Long matchId, int status);

    void updateStatusesForDirectMatching(Long userId, Long requesterId, Long groupId, int status);

    void updateMemberStatus(Long userId, Long groupId, int status);

    void updateStatusForApprovedMembers(Long groupId, int status);

    void updateStatusForAllMembers(Long groupId, int status);

    boolean isUserParticipant(Long userId, Long groupId);

    List<PermitRequestBaseRes> findPermitValidationData(Long userId, LocalDate date);

    void updateStatusAfterRequestApproval(Long groupId, Long requesterId, int approvedStatus);

    List<GroupMatchBaseRes> findAllGroupMemberInfo(Long groupId);

    List<RejectGroupMemberBaseRes> getGroupMember(Long groupId);

    void updateAllGroupMemberStatus(Long groupId, Long requesterId);

    boolean updateMyStatusFromApprovedToRequest(Long userId, Long matchId);

    Optional<GroupMemberBaseRes> getMatchingInfo(Long userId, Long gameId, boolean isGroup);
}
