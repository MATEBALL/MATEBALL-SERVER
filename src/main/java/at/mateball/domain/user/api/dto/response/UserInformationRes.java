package at.mateball.domain.user.api.dto.response;

import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.matchrequirement.core.constant.Gender;

import com.querydsl.core.Tuple;
import at.mateball.domain.user.core.QUser;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UserInformationRes(
        @Schema(description = "사용자의 닉네임")
        String nickname,
        @Schema(description = "사용자의 나이")
        String age,
        @Schema(description = "사용자의 성별")
        String gender,
        @Schema(description = "사용자의 직관 스타일")
        String style,
        @Schema(description = "사용자의 한 줄 소개")
        String introduction,
        @Schema(description = "사용자의 프로필 이미지")
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
