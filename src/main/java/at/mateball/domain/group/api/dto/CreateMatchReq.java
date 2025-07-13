package at.mateball.domain.group.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateMatchReq(
        @NotNull
        @Schema(description = "경기 ID")
        Long gameId,
        @NotNull
        @Schema(description = "매칭 유형")
        String matchType
) {
}
