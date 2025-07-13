package at.mateball.domain.group.api.dto.base;

public record MatchingBaseRes(
        Long totalMatches,
        boolean hasMatchOnDate
) {
}
