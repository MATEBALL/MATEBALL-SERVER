package at.mateball.domain.auth.api.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccount(
        String gender,
        @JsonProperty("birthyear") String birthyear
) {
}
