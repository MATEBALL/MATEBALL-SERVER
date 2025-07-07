package at.mateball.domain.user.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record NicknameReq(
        @NotBlank
        @Schema(description = "설정할 닉네임")
        String nickname
) {
}
