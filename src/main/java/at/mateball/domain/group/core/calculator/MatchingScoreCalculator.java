package at.mateball.domain.group.core.calculator;

import at.mateball.domain.group.core.calculator.policy.StyleMatchingScorePolicy;
import at.mateball.domain.group.core.calculator.policy.TeamMatchingScorePolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchingScoreCalculator {

    private final TeamMatchingScorePolicy teamMatchingScorePolicy;
    private final StyleMatchingScorePolicy styleMatchingScorePolicy;

    public int calculate(MatchingTarget loginUser, MatchingTarget member) {
        int teamScore = teamMatchingScorePolicy.calculate(loginUser, member);
        int styleScore = styleMatchingScorePolicy.calculate(loginUser, member);
        return teamScore + styleScore;
    }
}