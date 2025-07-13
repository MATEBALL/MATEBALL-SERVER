package at.mateball.domain.group.api.dto;

import at.mateball.domain.group.api.dto.base.GroupGetBaseRes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record GroupGetRes(
        @NotNull
        @Schema(description = "매칭 ID")
        Long id,
        @NotNull
        @Schema(description = "닉네임")
        String nickname,
        @NotNull
        @Schema(description = "원정팀")
        String awayTeam,
        @NotNull
        @Schema(description = "홈팀")
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
        @NotNull
        @Schema(description = "총 인원 수, 사용자 외 몇명은 -1 해야합니다.")
        Integer count,
        @Schema(description = "프로필", nullable = true)
        List<String> imgUrl
) {
    public static GroupGetRes from(GroupGetBaseRes base, Integer matchRate, Integer count, List<String> imgUrls) {
        return new GroupGetRes(
                base.id(),
                base.nickname(),
                base.awayTeam(),
                base.homeTeam(),
                base.stadium(),
                base.date(),
                matchRate,
                count,
                imgUrls
        );
    }
}
