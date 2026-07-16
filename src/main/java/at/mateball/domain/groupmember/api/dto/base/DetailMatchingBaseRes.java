package at.mateball.domain.groupmember.api.dto.base;

import java.time.LocalDate;

public record DetailMatchingBaseRes(
        Long id,
        Long userId,
        String nickname,
        Integer birthYear,
        String gender,
        Integer team,
        Integer style,
        String introduction,
        String awayTeam,
        String homeTeam,
        String stadium,
        LocalDate date,
        String imgUrl
) {
    public DetailMatchingBaseRes withImgUrl(String imgUrl) {
        return new DetailMatchingBaseRes(
                id,
                userId,
                nickname,
                birthYear,
                gender,
                team,
                style,
                introduction,
                awayTeam,
                homeTeam,
                stadium,
                date,
                imgUrl
        );
    }
}
