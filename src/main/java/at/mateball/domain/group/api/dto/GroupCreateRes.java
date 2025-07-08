package at.mateball.domain.group.api.dto;

import at.mateball.domain.team.core.TeamName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record GroupCreateRes(
        @NotNull
        @Schema(description = "매칭 ID")
        Long id,
        @NotNull
        @Schema(description = "닉네임")
        String nickname,
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
        @Schema(description = "그룹매칭에 들어온 인원 수")
        Integer count,
        @Schema(description = "이미지 URL", nullable = true)
        List<String> imgUrl
) {
    public static GroupCreateRes from(GroupBaseDto base, Integer count, List<String> imgUrls) {
        return new GroupCreateRes(
                base.id(),
                base.nickname(),
                base.awayTeam(),
                base.homeTeam(),
                base.stadium(),
                base.date(),
                count,
                imgUrls
        );
    }
}
