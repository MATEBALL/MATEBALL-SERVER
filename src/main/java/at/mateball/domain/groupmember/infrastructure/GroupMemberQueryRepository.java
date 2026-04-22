package at.mateball.domain.groupmember.infrastructure;

import java.util.List;

public interface GroupMemberQueryRepository {

    boolean existsAccessibleRequestMatch(Long userId, Long matchId);

    List<MatchRequestDetailQueryDto> findMatchRequestDetailMembers(Long matchId);
}
