package at.mateball.domain.user.api.dto.response;

import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.team.core.TeamName;
import io.swagger.v3.oas.annotations.media.Schema;

public record MyPageInformationRes(
        @Schema(description = "사용자의 닉네임")
        String nickname,
        @Schema(description = "응원 팀")
        String team,
        @Schema(description = "사용자의 직관 스타일")
        String style,
        @Schema(description = "사용자의 프로필 이미지")
        String imgUrl,
        @Schema(description = "함께한 매칭")
        Long matchCnt,
        @Schema(description = "시즌 평균 직관")
        Integer avgSeason
) {
    public static MyPageInformationRes fromBase(MyPageInformationBaseRes myPageInformationBaseRes) {
        return new MyPageInformationRes(
                myPageInformationBaseRes.nickname(),
                TeamName.from(myPageInformationBaseRes.team()).getLabel(),
                Style.from(myPageInformationBaseRes.style()).getLabel(),
                myPageInformationBaseRes.imgUrl(),
                myPageInformationBaseRes.matchCnt(),
                myPageInformationBaseRes.avgSeason()
        );
    }

}
