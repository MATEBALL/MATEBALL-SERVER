package at.mateball.domain;

import at.mateball.domain.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public record KakaoUserRes(
        Long id,
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) {
    public User toEntity() {
        return new User(
                id,
                Optional.ofNullable(kakaoAccount).map(KakaoAccount::gender).orElse(null),
                Optional.ofNullable(kakaoAccount).map(KakaoAccount::birthyear).orElse(null)
        );
    }
}
