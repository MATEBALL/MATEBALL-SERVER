package at.mateball.domain.group.api.dto.base;

public record MatchingBaseRes(
        long totalMatches,
        boolean hasMatchOnDate
) {
}
