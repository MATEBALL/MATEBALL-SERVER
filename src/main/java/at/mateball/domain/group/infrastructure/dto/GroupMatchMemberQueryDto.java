package at.mateball.domain.group.infrastructure.dto;

import at.mateball.domain.group.core.calculator.MatchingTarget;

public record GroupMatchMemberQueryDto(
        Long memberId,
        String gender,
        Integer birthYear,
        String nickname,
        String introduction,
        Integer team,
        Integer teamAllowed,
        Integer style,
        Integer avgSeason,
        String profileImageKey,
        Boolean isLeader
) {
    public MatchingTarget toMatchingTarget() {
        return new MatchingTarget(
                memberId,
                team,
                teamAllowed,
                style
        );
    }
}
