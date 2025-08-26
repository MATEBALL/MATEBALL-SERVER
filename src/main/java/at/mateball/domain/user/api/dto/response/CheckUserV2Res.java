package at.mateball.domain.user.api.dto.response;

public record CheckUserV2Res(
        boolean nickname,
        boolean condition,
        boolean hasAccepted
) {
}