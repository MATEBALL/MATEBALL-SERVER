package at.mateball.domain.group.api.dto.base;

import java.time.LocalDate;

public record DirectCreateBaseRes(
        Long id,
        String nickname,
        Integer birthYear,
        String gender,
        Integer team,
        Integer style,
        String awayTeam,
        String homeTeam,
        String stadium,
        LocalDate date,
        String imgUrl
) {
}
