package at.mateball.domain.auth.api.dto;

public record LoginResult(
        String accessToken,
        String refreshToken,
        String kakaoAccessToken,
        Long userId,
        String gender,

        String birthyear
) {

}
