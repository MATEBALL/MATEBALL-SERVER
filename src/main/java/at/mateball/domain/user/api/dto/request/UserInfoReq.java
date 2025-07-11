package at.mateball.domain.user.api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserInfoReq(
        @NotBlank(message = "성별은 필수 입력해야합니다.")
        String gender,

        @Min(value = 1900, message = "유효하지 않은 출생년도입니다.")
        @Max(value = 2025, message = "지금 태어났을리 없습니다. 미래의 출생년도는 입력할 수 없어요.")
        int birthYear
) {
}
