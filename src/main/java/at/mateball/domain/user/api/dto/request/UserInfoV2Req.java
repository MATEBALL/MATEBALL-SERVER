package at.mateball.domain.user.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserInfoV2Req(
        @NotNull
        String nickname,

        @NotNull
        String introduction,

        @NotNull
<<<<<<< HEAD
        Integer birthYear,
=======
        int birthYear,
>>>>>>> 29c4d6b ([feat/#152] 온보딩 회원 정보 설정 api 구현)

        @NotNull
        String gender
) {
}
