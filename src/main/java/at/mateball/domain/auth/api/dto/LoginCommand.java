package at.mateball.domain.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record LoginCommand(
        @NotNull
        @Schema(description = "인가 코드")
        String code
) {
}
