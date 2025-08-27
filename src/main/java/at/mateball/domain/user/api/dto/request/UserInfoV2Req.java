package at.mateball.domain.user.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserInfoV2Req(
        @NotNull
        String nickname,

        @NotNull
        String introduction,

        @NotNull
        Integer birthYear,

        @NotNull
        String gender
) {
}
