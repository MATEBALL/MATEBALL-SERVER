package at.mateball.domain.group.api.dto;

import at.mateball.domain.gameinformation.core.QGameInformation;
import at.mateball.domain.group.api.dto.base.DirectCreateBaseRes;
import at.mateball.domain.group.core.QGroup;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.team.core.TeamName;
import at.mateball.domain.user.core.QUser;
import com.querydsl.core.Tuple;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DirectCreateRes(
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
        @Schema(description = "응원하고 있는 팀")
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
        @Schema(description = "이미지 URL", nullable = true)
        String imgUrl
) {
    public static DirectCreateRes from(DirectCreateBaseRes baseRes) {
        int age = LocalDate.now().getYear() - baseRes.birthYear() + 1;

        return new DirectCreateRes(
                baseRes.id(),
                baseRes.nickname(),
                age + "세",
                Gender.from(baseRes.gender()).getLabel(),
                TeamName.from(Integer.parseInt(baseRes.team().toString())).getLabel(),
                Style.from(baseRes.style()).getLabel(),
                baseRes.awayTeam(),
                baseRes.homeTeam(),
                baseRes.stadium(),
                baseRes.date(),
                baseRes.imgUrl()
        );
    }
}
