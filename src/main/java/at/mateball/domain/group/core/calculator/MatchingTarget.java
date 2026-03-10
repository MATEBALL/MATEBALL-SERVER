package at.mateball.domain.group.core.calculator;

public record MatchingTarget(
        Long userId,
        Integer team,
        Integer teamAllowed,
        Integer style
) {
}
