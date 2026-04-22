package at.mateball.domain.groupmember.infrastructure.repository;

import at.mateball.domain.groupmember.infrastructure.dto.MatchRequestDetailQueryDto;

import java.util.List;

public interface GroupMemberQueryRepository {

    boolean existsAccessibleRequestMatch(Long userId, Long matchId);

    List<MatchRequestDetailQueryDto> findMatchRequestDetailMembers(Long userId, Long matchId);
}
