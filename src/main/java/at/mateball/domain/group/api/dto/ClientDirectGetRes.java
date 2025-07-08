package at.mateball.domain.group.api.dto;

import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.team.core.TeamName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ClientDirectGetRes(
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
        @Schema(description = "매칭률")
        Integer matchRate,
        @Schema(description = "이미지 URL", nullable = true)
        String imgUrl
) {
    public static ClientDirectGetRes from(DirectGetRes raw) {
            int age = LocalDate.now().getYear() - raw.birthYear() + 1;

            return new ClientDirectGetRes(
                    raw.id(),
                    raw.nickname(),
                    age + "세",
                    Gender.from(raw.gender()).getLabel(),
                    TeamName.from(raw.team()).getLabel(),
                    Style.from(raw.style()).getLabel(),
                    raw.awayTeam(),
                    raw.homeTeam(),
                    raw.stadium(),
                    raw.date(),
                    raw.matchRate(),
                    raw.imgUrl()
            );
    }
}