package at.mateball.domain.auth.api.dto;

public record LoginUserInfo(
        Long userId,
        String gender,
        int birthyear
) {}
