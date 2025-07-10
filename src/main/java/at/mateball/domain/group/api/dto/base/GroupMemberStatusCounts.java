package at.mateball.domain.group.api.dto.base;

public record GroupMemberStatusCounts(
        long totalParticipants,
        long awaitingApprovalCount,
        long approvedCount
) {
}
