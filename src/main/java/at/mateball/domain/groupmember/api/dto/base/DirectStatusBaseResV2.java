package at.mateball.domain.groupmember.api.dto.base;

import java.time.LocalDate;

public record DirectStatusBaseResV2(
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
        Integer status,
        String imgUrl
) {
    public DirectStatusBaseResV2 withImgUrl(String imgUrl) {
        return new DirectStatusBaseResV2(
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
                status,
                imgUrl
        );
    }
}
