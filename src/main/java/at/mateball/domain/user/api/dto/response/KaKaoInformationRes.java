package at.mateball.domain.user.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record KaKaoInformationRes(
        @Schema(description = "사용자의 생년월일")
        Integer birthYear,
        @Schema(description = "사용자의 성별")
        String gender
) {
}
