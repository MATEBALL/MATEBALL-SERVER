package at.mateball.domain.group.core.repository.querydsl;

import at.mateball.domain.group.api.dto.ChattingAccessRes;
import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.group.api.dto.GroupCreateRes;
import at.mateball.domain.group.api.dto.GroupValidationRes;
import at.mateball.domain.group.api.dto.base.DirectGetBaseRes;
import at.mateball.domain.group.api.dto.base.GroupGetBaseRes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GroupRepositoryCustom {
    DirectCreateRes findDirectCreateResults(Long userId, Long matchId);

    Optional<GroupCreateRes> findGroupCreateRes(Long userId, Long matchId);

    List<DirectGetBaseRes> findDirectGroupsByDate(Long userId, LocalDate date);

    List<GroupGetBaseRes> findGroupsWithBaseInfo(Long userId, LocalDate date);

    long updateGroupStatus(Long groupId, int status);

    void assignChattingToGroup(Long groupId, Long chattingId);

    List<Long> findGroupIdsByGameDate(LocalDate date);

    int bulkUpdateGroupStatusToFailed(List<Long> groupIds);

    int bulkUpdateGroupMemberStatusToMatchFailed(List<Long> groupIds);

    ChattingAccessRes findChattingAccessInfo(Long userId, Long groupId);

    GroupValidationRes findValidateGroupData(Long groupId);
}
