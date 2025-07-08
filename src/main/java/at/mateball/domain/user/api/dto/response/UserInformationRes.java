package at.mateball.domain.user.api.dto.response;

import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import io.swagger.v3.oas.annotations.media.Schema;

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
    public static UserInformationRes fromBase(BaseUserInformationRes baseUserInformationRes) {
        int age = java.time.LocalDate.now().getYear() - baseUserInformationRes.birthYear() + 1;
        return new UserInformationRes(
                baseUserInformationRes.nickname(),
                age + "세",
                Gender.from(baseUserInformationRes.gender()).getLabel(),
                Style.from(baseUserInformationRes.style()).getLabel(),
                baseUserInformationRes.introduction(),
                baseUserInformationRes.imgUrl()
        );
    }
}
