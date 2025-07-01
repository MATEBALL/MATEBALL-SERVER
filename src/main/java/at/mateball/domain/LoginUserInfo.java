package at.mateball.domain;

public record LoginUserInfo(
        Long userId,
        String gender,
        String birthyear) {}
