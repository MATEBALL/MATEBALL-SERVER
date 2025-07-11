package at.mateball.domain.groupmember.api.dto.base;

public record GroupMatchBaseRes(
    Long userId,
    int status,
    boolean isParticipant
) {}
