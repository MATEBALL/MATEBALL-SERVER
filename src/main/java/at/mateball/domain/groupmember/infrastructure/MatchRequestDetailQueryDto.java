package at.mateball.domain.groupmember.infrastructure;

import at.mateball.domain.group.core.calculator.MatchingTarget;

import java.time.LocalDate;

public record MatchRequestDetailQueryDto(
        Long memberId,
        String nickname,
        Integer birthYear,
        String gender,
        Integer team,
        Integer style,
        String introduction,
        LocalDate date,
        String profileImageKey,
        Integer avgSeason
) {
    public MatchingTarget toMatchingTarget() {
        return new MatchingTarget(
                memberId,
                team,
                style,
                birthYear
        );
    }
}
