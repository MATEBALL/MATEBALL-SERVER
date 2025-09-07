package at.mateball.domain.auth.api.dto;

import at.mateball.domain.user.core.User;

public record LoginResult(
        String accessToken,
        String refreshToken,
        String kakaoAccessToken,
        Long userId,
        String email,
        String profileImage
) {
    public static LoginResult from(User user, String accessToken, String refreshToken, String kakaoAccessToken) {
        String profileImage = (user.getImgUrl() != null)
                ? user.getImgUrl()
                : User.DEFAULT_PROFILE_IMAGE_URL;

        return new LoginResult(
                accessToken,
                refreshToken,
                kakaoAccessToken,
                user.getId(),
                user.getEmail(),
                profileImage
        );
    }
}
