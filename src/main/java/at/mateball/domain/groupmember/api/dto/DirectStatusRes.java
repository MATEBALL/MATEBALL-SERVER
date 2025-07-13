package at.mateball.domain.groupmember.api.dto;

import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.api.dto.base.DirectStatusBaseRes;
import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.team.core.TeamName;

import java.time.LocalDate;

public record DirectStatusRes(
        Long id,
        String nickname,
        String age,
        String gender,
        String team,
        String style,
        String awayTeam,
        String homeTeam,
        LocalDate date,
        String status,
        String imgUrl
) {
    public static DirectStatusRes from(DirectStatusBaseRes baseRes) {
        String age = null;
        if (baseRes.birthYear() != null) {
            age = (LocalDate.now().getYear() - baseRes.birthYear() + 1) + "세";
        }

        return new DirectStatusRes(
                baseRes.id(),
                baseRes.nickname(),
                age,
                Gender.from(baseRes.gender()).getLabel(),
                TeamName.from(baseRes.team()).getLabel(),
                Style.from(baseRes.style()).getLabel(),
                baseRes.awayTeam(),
                baseRes.homeTeam(),
                baseRes.date(),
                GroupMemberStatus.from(baseRes.status()).getLabel(),
                baseRes.imgUrl()
        );
    }
}
