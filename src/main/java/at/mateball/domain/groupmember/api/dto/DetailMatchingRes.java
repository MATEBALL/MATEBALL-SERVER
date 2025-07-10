package at.mateball.domain.groupmember.api.dto;

import at.mateball.domain.groupmember.api.dto.base.DetailMatchingBaseRes;
import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.team.core.TeamName;

import java.time.LocalDate;

public record DetailMatchingRes(
        Long id,
        String nickname,
        String age,
        String gender,
        String team,
        String style,
        String introduction,
        String awayTeam,
        String homeTeam,
        String stadium,
        LocalDate date,
        String imgUrl,
        Integer matchRate
) {
    public static DetailMatchingRes from(DetailMatchingBaseRes base, Integer matchRate) {
        String age = null;
        if (base.birthYear() != null) {
            age = (LocalDate.now().getYear() - base.birthYear() + 1) + "세";
        }

        return new DetailMatchingRes(
                base.id(),
                base.nickname(),
                age,
                Gender.from(base.gender()).getLabel(),
                TeamName.from(base.team()).getLabel(),
                Style.from(base.style()).getLabel(),
                base.introduction(),
                base.awayTeam(),
                base.homeTeam(),
                base.stadium(),
                base.date(),
                base.imgUrl(),
                matchRate
        );
    }

}
