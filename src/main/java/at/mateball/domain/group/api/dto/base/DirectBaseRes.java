package at.mateball.domain.group.api.dto.base;

import java.time.LocalDate;

public record DirectBaseRes(
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
        Integer matchRate,
        String imgUrl
) {
    public DirectBaseRes withMatchRate(Integer matchRate) {
        return new DirectBaseRes(
                id,
                nickname,
                birthYear,
                gender,
                team,
                style,
                awayTeam,
                homeTeam,
                stadium,
                date,
                matchRate,
                imgUrl
        );
    }
}
