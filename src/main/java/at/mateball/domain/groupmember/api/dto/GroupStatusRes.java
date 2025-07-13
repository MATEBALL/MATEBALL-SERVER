package at.mateball.domain.groupmember.api.dto;

import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.api.dto.base.GroupStatusBaseRes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record GroupStatusRes(
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
        @Schema(description = "매칭 상태")
        String status,
        @NotNull
        @Schema(description = "총 인원 수, 사용자 외 몇명은 -1 해야합니다.")
        Integer count,
        @Schema(description = "그룹원 프로필", nullable = true)
        List<String> imgUrl
) {
    public static GroupStatusRes from(GroupStatusBaseRes base, Integer count, List<String> imgUrls) {
        return new GroupStatusRes(
                base.id(),
                base.nickname(),
                base.awayTeam(),
                base.homeTeam(),
                base.stadium(),
                base.date(),
                GroupMemberStatus.from(base.status()).getLabel(),
                count,
                imgUrls
        );
    }
}
