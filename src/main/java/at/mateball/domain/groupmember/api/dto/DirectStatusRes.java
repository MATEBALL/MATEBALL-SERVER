package at.mateball.domain.groupmember.api.dto;

import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.api.dto.base.DirectStatusBaseRes;
import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.team.core.TeamName;
import at.mateball.util.AgeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DirectStatusRes(
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
        @Schema(description = "매칭 상태")
        String status,
        @Schema(description = "프로필", nullable = true)
        String imgUrl
) {
    public static DirectStatusRes from(DirectStatusBaseRes baseRes) {
        String age = null;
        if (baseRes.birthYear() != null) {
            age = AgeUtils.calculateAge(baseRes.birthYear());
        }

        return new DirectStatusRes(
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
                baseRes.imgUrl()
        );
    }
}
