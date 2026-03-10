package at.mateball.domain.group.infrastructure.dto;

import at.mateball.domain.group.core.calculator.MatchingTarget;

public record LoginUserMatchRequirementDto(
        Integer team,
        Integer teamAllowed,
        Integer style
) {
    public MatchingTarget toTarget(Long userId) {
        return new MatchingTarget(userId, team, teamAllowed, style);
    }
}
