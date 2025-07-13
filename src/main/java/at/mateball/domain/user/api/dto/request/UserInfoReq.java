package at.mateball.domain.user.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserInfoReq(
        @NotNull
        @Schema(description = "성별")
        @Enumerated(EnumType.STRING)
        @NotBlank(message = "성별은 필수 입력해야합니다.")
        String gender,

        @NotNull
        @Schema(description = "출생 년도")
        @Min(value = 1900, message = "유효하지 않은 출생년도입니다.")
        @Max(value = 2025, message = "지금 태어났을리 없습니다. 미래의 출생년도는 입력할 수 없어요.")
        int birthYear
) {
}
