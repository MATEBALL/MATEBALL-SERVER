package at.mateball.domain.user.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record MatchRequirementReq(
        @Schema(description = "응원하는 팀 이름")
        String team,
        @Schema(description = "응원 팀 허용 여부", nullable = true)
        String teamAllowed,
        @Schema(description = "관람스타일")
        String style,
        @Schema(description = "선호 성별")
        String genderPreference
) {
}
