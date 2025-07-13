package at.mateball.domain.group.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateMatchRes(
        @Schema(description = "매칭 ID")
        Long matchId
) {
}
