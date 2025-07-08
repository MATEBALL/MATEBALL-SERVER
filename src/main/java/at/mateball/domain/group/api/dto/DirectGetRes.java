package at.mateball.domain.group.api.dto;

import java.time.LocalDate;

public record DirectGetRes(
        Long id,
        String nickname,
        String age,
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
    public DirectGetRes withMatchRate(Integer matchRate) {
        return new DirectGetRes(
                id,
                nickname,
                age,
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
