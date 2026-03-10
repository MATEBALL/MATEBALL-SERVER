package at.mateball.domain.group.infrastructure.dto;

import at.mateball.domain.group.core.calculator.MatchingTarget;

public record GroupMemberMatchingQueryDto(
        Long memberId,
        Integer memberTeam,
        Integer memberTeamAllowed,
        Integer memberStyle,
        Integer loginUserTeam,
        Integer loginUserTeamAllowed,
        Integer loginUserStyle
) {

    public MatchingTarget toMemberTarget() {
        return new MatchingTarget(
                memberId,
                memberTeam,
                memberTeamAllowed,
                memberStyle
        );
    }

    public MatchingTarget toLoginUserTarget() {
        return new MatchingTarget(
                null,
                loginUserTeam,
                loginUserTeamAllowed,
                loginUserStyle
        );
    }
}