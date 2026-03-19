package at.mateball.domain.groupmember.api.dto;

public record GroupMatchSummaryRes(
        Long requesterId,
        Long matchedParticipants
) {}
