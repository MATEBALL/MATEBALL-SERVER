package at.mateball.domain.group.api.dto;

public record CreateMatchReq(
        Long gameId,
        String matchType
) {
}
