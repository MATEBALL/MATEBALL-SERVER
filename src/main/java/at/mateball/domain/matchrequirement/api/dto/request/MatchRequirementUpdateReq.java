package at.mateball.domain.matchrequirement.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record MatchRequirementUpdateReq(
        @Schema(description = "응원하는 팀 이름")
        String team,
        @Schema(description = "응원 팀 허용 여부")
        String teamAllowed,
        @Schema(description = "시즌 평균 직관 수")
        Integer avgSeason,
        @Schema(description = "관람스타일")
        String style
) {
}

