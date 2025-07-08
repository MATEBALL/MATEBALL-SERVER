package at.mateball.domain.groupmember.api.dto.base;

import java.time.LocalDate;

public record DirectStatusBaseRes(
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
        Integer status,
        String imgUrl
) {
}
