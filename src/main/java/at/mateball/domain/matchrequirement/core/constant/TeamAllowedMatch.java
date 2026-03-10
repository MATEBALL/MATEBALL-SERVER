package at.mateball.domain.matchrequirement.core.constant;

import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum TeamAllowedMatch {
    SAME_TEAM_ONLY(1, "같은 팀 메이트와 보고 싶어요"),
    NO_PREFERENCE(2, "상관없어요");

    private static final Map<Integer, TeamAllowedMatch> VALUE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(TeamAllowedMatch::getValue, Function.identity()));

    private static final Map<String, TeamAllowedMatch> LABEL_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(TeamAllowedMatch::getLabel, Function.identity()));

    private final int value;
    private final String label;

    TeamAllowedMatch(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static TeamAllowedMatch from(int value) {
        TeamAllowedMatch teamAllowedMatch = VALUE_MAP.get(value);
        if (teamAllowedMatch == null) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM);
        }
        return teamAllowedMatch;
    }

    public static TeamAllowedMatch fromLabel(String label) {

        // 프론트 축약 문자열 대응
        if ("같은 팀 메이트".equalsIgnoreCase(label)) {
            return SAME_TEAM_ONLY;
        }

        TeamAllowedMatch teamAllowedMatch = LABEL_MAP.get(label);
        if (teamAllowedMatch == null) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM);
        }
        return teamAllowedMatch;
    }

    public static boolean isSameTeamOnly(Integer value) {
        return value != null && SAME_TEAM_ONLY.value == value;
    }

    public static boolean isNoPreference(Integer value) {
        return value != null && NO_PREFERENCE.value == value;
    }
}