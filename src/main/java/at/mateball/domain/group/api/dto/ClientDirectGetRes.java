package at.mateball.domain.group.api.dto;

import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.team.core.TeamName;

import java.time.LocalDate;

public record ClientDirectGetRes(
        Long id,
        String nickname,
        String age,
        String gender,
        String team,
        String style,
        String awayTeam,
        String homeTeam,
        String stadium,
        LocalDate date,
        Integer matchRate,
        String imgUrl
) {
    public static ClientDirectGetRes from(DirectGetRes raw) {
        return new ClientDirectGetRes(
                raw.id(),
                raw.nickname(),
                raw.age(),
                Gender.from(raw.gender()).getLabel(),
                TeamName.from(raw.team()).getLabel(),
                Style.from(raw.style()).getLabel(),
                raw.awayTeam(),
                raw.homeTeam(),
                raw.stadium(),
                raw.date(),
                raw.matchRate(),
                raw.imgUrl()
        );
    }
}