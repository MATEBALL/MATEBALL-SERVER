package at.mateball.domain.matchrequirement.core.constant;

import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum StyleMatch {
    PASSIONATE_SUPPORTER(1, "열정응원러"),
    FOCUSED_VIEWER(2, "경기집중러"),
    FOODIE_VIEWER(3, "직관먹방러");

    private static final Map<Integer, StyleMatch> VALUE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(StyleMatch::getValue, Function.identity()));

    private static final Map<String, StyleMatch> LABEL_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(StyleMatch::getLabel, Function.identity()));

    private final int value;
    private final String label;

    StyleMatch(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static StyleMatch from(int value) {
        StyleMatch styleMatch = VALUE_MAP.get(value);
        if (styleMatch == null) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM);
        }
        return styleMatch;
    }

    public static StyleMatch fromLabel(String label) {
        StyleMatch styleMatch = LABEL_MAP.get(label);
        if (styleMatch == null) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM);
        }
        return styleMatch;
    }

    public static boolean isPassionateSupporter(Integer value) {
        return value != null && PASSIONATE_SUPPORTER.value == value;
    }

    public static boolean isFoodieViewer(Integer value) {
        return value != null && FOODIE_VIEWER.value == value;
    }
}