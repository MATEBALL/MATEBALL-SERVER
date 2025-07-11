package at.mateball.domain.groupmember.api.dto.base;

public record GroupMemberInfoDto(
    Long userId,
    int status,
    boolean isParticipant
) {}
