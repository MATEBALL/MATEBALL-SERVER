package at.mateball.domain.group.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateMatchRes(
        @Schema(description = "그룹 ID")
        Long matchId
) {
}
