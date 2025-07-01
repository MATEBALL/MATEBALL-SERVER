package at.mateball.domain.auth.api.dto.kako;

public record KakaoAccount(
        String email,
        String gender,
        String birthyear
) {
}
