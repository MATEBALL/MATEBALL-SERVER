package at.mateball.domain.groupmember.api.dto;

import at.mateball.domain.groupmember.api.dto.base.DetailMatchingBaseRes;
import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.team.core.TeamName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DetailMatchingRes(
        @NotNull
        @Schema(description = "매칭 ID")
        Long id,
        @NotNull
        @Schema(description = "닉네임")
        String nickname,
        @NotNull
        @Schema(description = "나이")
        String age,
        @NotNull
        @Schema(description = "성별")
        String gender,
        @NotNull
        @Schema(description = "응원 팀")
        String team,
        @NotNull
        @Schema(description = "응원 스타일")
        String style,
        @NotNull
        @Schema(description = "한줄 소개")
        String introduction,
        @NotNull
        @Schema(description = "원정 팀")
        String awayTeam,
        @NotNull
        @Schema(description = "홈 팀")
        String homeTeam,
        @NotNull
        @Schema(description = "경기장")
        String stadium,
        @NotNull
        @Schema(description = "경기 날짜")
        LocalDate date,
        @NotNull
        @Schema(description = "프로필")
        String imgUrl,
        @NotNull
        @Schema(description = "매칭률")
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
