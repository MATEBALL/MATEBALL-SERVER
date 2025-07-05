package at.mateball.domain.auth.api.dto.kakao;

public record KakaoAccount(
        String email,
        String gender,
        String birthyear
) {
}
