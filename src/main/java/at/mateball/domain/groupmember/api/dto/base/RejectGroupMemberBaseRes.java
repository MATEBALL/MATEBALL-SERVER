package at.mateball.domain.groupmember.api.dto.base;

public record RejectGroupMemberBaseRes(
        Long userId,
        int status
) {
}
