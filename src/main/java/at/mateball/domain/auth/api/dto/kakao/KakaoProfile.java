package at.mateball.domain.auth.api.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoProfile(
        @JsonProperty("nickname") String nickname,
        @JsonProperty("profile_image_url") String profileImageUrl,
        @JsonProperty("thumbnail_image_url") String thumbnailImageUrl,
        @JsonProperty("is_default_image") boolean isDefaultImage
) {

}
