package at.mateball.domain.matchrequirement.core.repository.querydsl;

import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRequirementRepositoryCustom {
    List<MatchingScoreDto> findMatchingUsers(Long userId);
}
