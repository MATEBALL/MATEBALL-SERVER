package at.mateball.domain.group.api.dto;

import com.querydsl.core.Tuple;

public record DirectCreateRes(
        Long id,
        String nickname,
        String age,
        String gender,
        String team,
        String style,
        String awayTeam,
        String homeTeam,
        String stadium,
        String date,
        String imgUrl
) {
    public static DirectCreateRes from(Tuple tuple) {

    }
}
