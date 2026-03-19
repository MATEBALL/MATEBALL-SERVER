package at.mateball.domain.group.api.dto;

import at.mateball.domain.group.core.MatchType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateMatchV3Req(
        @NotNull
        @Schema(description = "경기 ID")
        Long gameId,
        @NotNull
        @Schema(description = "매칭 유형")
        MatchType matchType
) {
}
