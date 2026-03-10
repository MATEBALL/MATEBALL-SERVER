package at.mateball.domain.group.core.calculator.policy;

import at.mateball.domain.group.core.calculator.MatchingTarget;

public interface MatchingScorePolicy {
    int calculate(MatchingTarget loginUser, MatchingTarget member);
}
