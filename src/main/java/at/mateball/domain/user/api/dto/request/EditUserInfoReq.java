package at.mateball.domain.user.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record EditUserInfoReq(
        @NotNull
        String field,

        @NotNull
        String value
) {
}
