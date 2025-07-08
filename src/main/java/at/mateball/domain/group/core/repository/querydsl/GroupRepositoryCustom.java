package at.mateball.domain.group.core.repository.querydsl;

import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.group.api.dto.GroupCreateRes;
import at.mateball.domain.group.api.dto.GroupGetRes;
import at.mateball.domain.group.api.dto.base.DirectGetBaseRes;
import at.mateball.domain.group.api.dto.base.GroupGetBaseRes;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface GroupRepositoryCustom {
    DirectCreateRes findDirectCreateResults(Long userId, Long matchId);

    Optional<GroupCreateRes> findGroupCreateRes(Long matchId);
    List<DirectGetBaseRes> findDirectGroupsByDate(LocalDate date);

    List<GroupGetBaseRes> findGroupsWithBaseInfo(LocalDate date);

    Map<Long, Integer> findGroupMemberCountMap(List<Long> groupIds);

    Map<Long, List<String>> findGroupMemberImgMap(List<Long> groupIds);
}
