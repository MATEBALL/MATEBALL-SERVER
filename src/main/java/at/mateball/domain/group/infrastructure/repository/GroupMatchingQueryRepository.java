package at.mateball.domain.group.infrastructure.repository;

import at.mateball.domain.group.infrastructure.dto.GroupMemberMatchingQueryDto;

import java.util.List;

public interface GroupMatchingQueryRepository {
    List<GroupMemberMatchingQueryDto> findGroupMembersForMatching(Long loginUserId, Long groupId);
}
