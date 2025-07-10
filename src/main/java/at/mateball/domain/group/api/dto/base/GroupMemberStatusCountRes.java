package at.mateball.domain.group.api.dto.base;

public record GroupMemberStatusCountRes(
        long totalParticipants,
        long awaitingApprovalCount,
        long approvedCount
) {
}
