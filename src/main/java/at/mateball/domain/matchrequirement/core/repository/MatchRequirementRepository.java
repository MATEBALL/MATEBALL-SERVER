package at.mateball.domain.matchrequirement.core.repository;

import at.mateball.domain.matchrequirement.core.MatchRequirement;
import at.mateball.domain.matchrequirement.core.repository.querydsl.MatchRequirementRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRequirementRepository extends JpaRepository<MatchRequirement, Long>, MatchRequirementRepositoryCustom {
    Optional<MatchRequirement> findByUserId(Long userId);
}
