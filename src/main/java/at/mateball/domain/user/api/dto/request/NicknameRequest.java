package at.mateball.domain.user.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record NicknameRequest(
        @NotNull
        @Schema(description = "설정할 닉네임")
        String nickname
) {
}
