package at.mateball.domain.groupmember.infrastructure.dto;

import at.mateball.domain.group.core.calculator.MatchingTarget;

public record MatchRequestDetailQueryDto(
        Long memberId,
        String nickname,
        Integer birthYear,
        String gender,
        Integer team,
        Integer teamAllowed,
        Integer style,
        String introduction,
        String profileImageKey,
        Integer avgSeason
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
