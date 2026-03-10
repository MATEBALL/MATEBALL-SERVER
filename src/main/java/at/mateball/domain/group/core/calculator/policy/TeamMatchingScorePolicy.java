package at.mateball.domain.group.core.calculator.policy;

import at.mateball.domain.group.core.calculator.MatchingTarget;
import at.mateball.domain.matchrequirement.core.constant.TeamAllowedMatch;
import org.springframework.stereotype.Component;

@Component
public class TeamMatchingScorePolicy implements MatchingScorePolicy {

    private static final int FULL_SCORE = 60;
    private static final int PARTIAL_SCORE = 40;
    private static final int EMPTY_SCORE = 0;

    @Override
    public int calculate(MatchingTarget loginUser, MatchingTarget member) {
        if (loginUser.teamAllowed() == null || member.teamAllowed() == null) {
            return EMPTY_SCORE;
        }

        boolean loginNoPreference = TeamAllowedMatch.isNoPreference(loginUser.teamAllowed());
        boolean memberNoPreference = TeamAllowedMatch.isNoPreference(member.teamAllowed());

        boolean loginSameTeamOnly = TeamAllowedMatch.isSameTeamOnly(loginUser.teamAllowed());
        boolean memberSameTeamOnly = TeamAllowedMatch.isSameTeamOnly(member.teamAllowed());

        boolean loginNoTeam = isNoTeam(loginUser.team());
        boolean memberNoTeam = isNoTeam(member.team());

        // A·B 모두 ‘상관없어요’ → 60점
        if (loginNoPreference && memberNoPreference) {
            return FULL_SCORE;
        }

        // ‘응원팀 없음’ 선택 시
        // 상대가 ‘상관없어요’여야만 60점, ‘같은 팀만’ 선택했다면 40점
        if (loginNoTeam || memberNoTeam) {
            boolean isFullScore =
                    (loginNoTeam && memberNoPreference) ||
                            (memberNoTeam && loginNoPreference);

            return isFullScore ? FULL_SCORE : PARTIAL_SCORE;
        }

        // A 또는 B 중 한쪽이라도 ‘같은 팀만’ 선택 시
        // A·B 응원팀이 같으면 60점, 다르면 40점
        if (loginSameTeamOnly || memberSameTeamOnly) {
            return loginUser.team().equals(member.team()) ? FULL_SCORE : PARTIAL_SCORE;
        }

        // 방어 로직
        return FULL_SCORE;
    }

    private boolean isNoTeam(Integer team) {
        return team == null;
    }
}
