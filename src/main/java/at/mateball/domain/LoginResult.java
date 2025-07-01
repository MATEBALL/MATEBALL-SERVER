package at.mateball.domain;

public record LoginResult(
        String accessToken,
        String refreshToken,
        String kakaoAccessToken,
        Long userId,
        String birthyear,
        String gender
) {

}
