package at.mateball.domain.matchrequirement.core.constant;

import at.mateball.domain.group.core.GroupStatus;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Style {
    PASSIONATE_SUPPORTER(1, "열정 응원러"),
    FOCUSED_VIEWER(2, "경기 집중러"),
    FOODIE_VIEWER(3, "직관 먹방러");

    private final int value;
    private final String label;

    Style(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Style from(int value) {
        return Arrays.stream(values())
                .filter(g -> g.value == value)
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM));
    }
}
