package at.mateball.domain.groupmember.core.repository.querydsl;


import at.mateball.domain.groupmember.api.dto.base.DirectStatusBaseRes;
import at.mateball.domain.groupmember.api.dto.base.GroupStatusBaseRes;

import java.util.List;
import java.util.Map;

public interface GroupMemberRepositoryCustom {
    Map<Long, Integer> findGroupMemberCountMap(List<Long> groupIds);

    Map<Long, List<String>> findGroupMemberImgMap(List<Long> groupIds);

    List<DirectStatusBaseRes> findDirectMatchingsByUserAndGroupStatus(Long userId, int groupStatus);

    List<DirectStatusBaseRes> findAllDirectMatchingsByUser(Long userId);

    List<GroupStatusBaseRes> findGroupMatchingsByUser(Long userId);

    List<GroupStatusBaseRes> findGroupMatchingsByUserAndStatus(Long userId, int groupStatus);
}
