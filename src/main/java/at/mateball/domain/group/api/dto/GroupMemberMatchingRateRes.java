package at.mateball.domain.group.api.dto;

public record GroupMemberMatchingRateRes(
        Long memberId,
        int matchingRate
) {
}
