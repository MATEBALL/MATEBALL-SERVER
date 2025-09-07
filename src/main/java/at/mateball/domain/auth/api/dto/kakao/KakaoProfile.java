package at.mateball.domain.auth.api.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoProfile(
        @JsonProperty("profile_image_url") String profileImageUrl
) {

}