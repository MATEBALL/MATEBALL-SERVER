package at.mateball.domain.user.api.dto.response;

public record InfoCheckRes(
        boolean nickname,
        boolean condition,
        boolean hasAccepted
) {
}