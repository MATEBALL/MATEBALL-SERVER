package at.mateball.domain.group.core.calculator.policy;

import at.mateball.domain.group.core.calculator.MatchingTarget;
import at.mateball.domain.matchrequirement.core.constant.StyleMatch;
import org.springframework.stereotype.Component;

@Component
public class StyleMatchingScorePolicy implements MatchingScorePolicy {

    private static final int SAME_SCORE = 40;
    private static final int PASSIONATE_FOODIE_SCORE = 35;
    private static final int DIFFERENT_SCORE = 30;
    private static final int EMPTY_SCORE = 0;

    @Override
    public int calculate(MatchingTarget loginUser, MatchingTarget member) {
        if (loginUser.style() == null || member.style() == null) {
            return EMPTY_SCORE;
        }

        if (loginUser.style().equals(member.style())) {
            return SAME_SCORE;
        }

        boolean passionateAndFoodie =
                (StyleMatch.isPassionateSupporter(loginUser.style()) && StyleMatch.isFoodieViewer(member.style()))
                        || (StyleMatch.isFoodieViewer(loginUser.style()) && StyleMatch.isPassionateSupporter(member.style()));

        if (passionateAndFoodie) {
            return PASSIONATE_FOODIE_SCORE;
        }

        return DIFFERENT_SCORE;
    }
}
