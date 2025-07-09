package at.mateball.domain.groupmember.api.dto.base;

import java.time.LocalDate;

public record DetailMatchingBaseRes(
        Long id,
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
}
