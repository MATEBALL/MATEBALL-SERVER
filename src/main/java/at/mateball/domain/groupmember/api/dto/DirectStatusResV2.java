package at.mateball.domain.groupmember.api.dto;

import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.api.dto.base.DirectStatusBaseResV2;
import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.team.core.TeamName;
import at.mateball.util.AgeUtils;

import java.time.LocalDate;

public record DirectStatusResV2(
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
        String status,
        String imgUrl,
        boolean isCreated
) {
    public static DirectStatusResV2 fromV2(DirectStatusBaseResV2 baseRes, Long userId) {
        String age = null;
        if (baseRes.birthYear() != null) {
            age = AgeUtils.calculateAge(baseRes.birthYear());
        }

        boolean isCreated = baseRes.leaderId().equals(userId);

        return new DirectStatusResV2(
                baseRes.id(),
                baseRes.nickname(),
                age,
                Gender.from(baseRes.gender()).getLabel(),
                TeamName.from(baseRes.team()).getLabel(),
                Style.from(baseRes.style()).getLabel(),
                baseRes.awayTeam(),
                baseRes.homeTeam(),
                baseRes.stadium(),
                baseRes.date(),
                GroupMemberStatus.from(baseRes.status()).getLabel(),
                baseRes.imgUrl(),
                isCreated
        );
    }
}
