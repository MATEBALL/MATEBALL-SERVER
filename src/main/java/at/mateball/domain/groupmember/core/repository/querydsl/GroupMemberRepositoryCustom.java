package at.mateball.domain.groupmember.core.repository.querydsl;


import java.util.List;
import java.util.Map;

public interface GroupMemberRepositoryCustom {
    Map<Long, Integer> findGroupMemberCountMap(List<Long> groupIds);

    Map<Long, List<String>> findGroupMemberImgMap(List<Long> groupIds);
}
