package at.mateball.domain.matchrequirement.core.repository.querydsl;

import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import at.mateball.domain.matchrequirement.core.MatchRequirement;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRequirementRepositoryCustom {
    List<MatchingScoreDto> findMatchingUsers(Long userId);
    Optional<MatchRequirement> findUserMatchRequirement(Long userId);
}
