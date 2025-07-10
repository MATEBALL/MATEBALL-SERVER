package at.mateball.domain.user.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record KaKaoInformationRes(
        @NotNull
        @Schema(description = "사용자의 생년월일")
        int birthYear,
        @NotBlank
        @Schema(description = "사용자의 성별")
        String gender
) {
}
