package at.mateball.domain.group.api.dto;

import jakarta.validation.constraints.NotNull;

public record CreateMatchReq(
        @NotNull
        Long gameId,
        @NotNull
        String matchType
) {
}
