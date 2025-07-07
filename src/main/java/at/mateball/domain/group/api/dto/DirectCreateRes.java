package at.mateball.domain.group.api.dto;

import at.mateball.domain.gameinformation.core.QGameInformation;
import at.mateball.domain.group.QGroup;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.team.core.TeamName;
import at.mateball.domain.user.core.QUser;
import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        QGroup group = QGroup.group;
        QUser user = QUser.user;
        QGameInformation gameInformation = QGameInformation.gameInformation;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;

        int birthYear = tuple.get(user.birthYear);
        int currentYear = LocalDateTime.now().getYear();
        int age = currentYear - birthYear + 1;

        LocalDate gameDate = tuple.get(gameInformation.gameDate);
        String formattedDate = gameDate.format(DateTimeFormatter.ofPattern("MM월 dd일"));

        return new DirectCreateRes(
                tuple.get(group.id),
                tuple.get(user.nickname),
                age + "세",
                Gender.from(tuple.get(user.gender)).getLabel(),
                TeamName.from(tuple.get(matchRequirement.team)).getLabel(),
                Style.from(tuple.get(matchRequirement.style)).getLabel(),
                tuple.get(gameInformation.awayTeamName),
                tuple.get(gameInformation.homeTeamName),
                tuple.get(gameInformation.stadiumName),
                formattedDate,
                tuple.get(user.imgUrl)
        );
    }
}
