package at.mateball.domain.team.core;

import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum TeamNameMatch {
    KIA(1, "KIA"),
    SAMSUNG(2, "삼성"),
    LG(3, "LG"),
    DOOSAN(4, "두산"),
    KT(5, "KT"),
    SSG(6, "SSG"),
    LOTTE(7, "롯데"),
    HANWHA(8, "한화"),
    NC(9, "NC"),
    KIWOOM(10, "키움"),
    NONE(11, "응원하는 팀이 없어요.");

    private static final Map<Integer, TeamNameMatch> VALUE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(TeamNameMatch::getValue, Function.identity()));

    private static final Map<String, TeamNameMatch> LABEL_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(TeamNameMatch::getLabel, Function.identity()));

    private final int value;
    private final String label;

    TeamNameMatch(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static TeamNameMatch from(int value) {
        TeamNameMatch teamNameMatch = VALUE_MAP.get(value);
        if (teamNameMatch == null) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM);
        }
        return teamNameMatch;
    }

    public static TeamNameMatch fromLabel(String label) {
        if (label == null || label.isBlank()) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM);
        }

        TeamNameMatch teamNameMatch = LABEL_MAP.get(label);
        if (teamNameMatch == null) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM);
        }
        return teamNameMatch;
    }
}
