package at.mateball.domain.user.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NicknameRequest(
        @NotBlank
        @Schema(description = "설정할 닉네임")
        String nickname
) {
}
