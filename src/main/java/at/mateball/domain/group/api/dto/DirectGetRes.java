package at.mateball.domain.group.api.dto;

import at.mateball.domain.gameinformation.core.QGameInformation;
import at.mateball.domain.group.core.QGroup;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.team.core.TeamName;
import at.mateball.domain.user.core.QUser;
import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DirectGetRes(
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
    public static DirectGetRes from(Tuple tuple, Integer matchRate) {
        QGroup group = QGroup.group;
        QUser user = QUser.user;
        QGameInformation gameInformation = QGameInformation.gameInformation;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;

        int birthYear = tuple.get(user.birthYear);
        int currentYear = LocalDateTime.now().getYear();
        int age = currentYear - birthYear + 1;

        return new DirectGetRes(
                tuple.get(group.id),
                tuple.get(user.nickname),
                age + "세",
                Gender.from(tuple.get(user.gender)).getLabel(),
                TeamName.from(tuple.get(matchRequirement.team)).getLabel(),
                Style.from(tuple.get(matchRequirement.style)).getLabel(),
                tuple.get(gameInformation.awayTeamName),
                tuple.get(gameInformation.homeTeamName),
                tuple.get(gameInformation.stadiumName),
                tuple.get(gameInformation.gameDate),
                matchRate,
                tuple.get(user.imgUrl)
        );
    }
}
