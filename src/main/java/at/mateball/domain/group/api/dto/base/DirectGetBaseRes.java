package at.mateball.domain.group.api.dto.base;

import java.time.LocalDate;

public record DirectGetBaseRes(
        Long id,
        Long leaderId,
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
    public DirectGetBaseRes withMatchRate(Integer matchRate) {
        return new DirectGetBaseRes(
                id,
                leaderId,
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
