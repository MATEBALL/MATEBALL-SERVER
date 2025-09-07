package at.mateball.domain.auth.api.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccount(
/*        String gender,
        @JsonProperty("birthyear") String birthyear*/
        String email,
        @JsonProperty("profile") KakaoProfile profile,
        @JsonProperty("profile_nickname_needs_agreement") boolean profileNicknameNeedsAgreement,
        @JsonProperty("profile_image_needs_agreement") boolean profileImageNeedsAgreement
) {

}
