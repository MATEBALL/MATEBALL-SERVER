package at.mateball.domain.user.api.dto.response;

public record CheckUserRes(
        boolean nickname,
        boolean condition
) {
}
