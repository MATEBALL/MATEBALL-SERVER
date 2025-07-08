package at.mateball.domain.user.api.dto.response;

import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.matchrequirement.core.constant.Gender;

import com.querydsl.core.Tuple;
import at.mateball.domain.user.core.QUser;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;

import java.time.LocalDateTime;

public record UserInformationRes(
        String nickname,
        String age,
        String gender,
        String style,
        String introduction,
        String imgUrl
) {
    public static UserInformationRes from(Tuple tuple) {
        QUser user = QUser.user;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;

        int birthYear = tuple.get(user.birthYear);
        int currentYear = LocalDateTime.now().getYear();
        int age = currentYear - birthYear + 1;

        return new UserInformationRes(
                tuple.get(user.nickname),
                age + "세",
                Gender.from(tuple.get(user.gender)).getLabel(),
                Style.from(tuple.get(matchRequirement.style)).getLabel(),
                tuple.get(user.introduction),
                tuple.get(user.imgUrl)
        );
    }
}
