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
    public DirectStatusBaseRes withImgUrl(String imgUrl) {
        return new DirectStatusBaseRes(
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
                status,
                imgUrl
        );
    }
}
