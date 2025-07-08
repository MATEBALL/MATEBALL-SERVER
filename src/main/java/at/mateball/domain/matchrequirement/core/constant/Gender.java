package at.mateball.domain.matchrequirement.core.constant;

import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Gender {
    MALE(1, "남성", "male"),
    FEMALE(2, "여성", "female"),
    NO_PREFERENCE(3, "상관없어요", "none");

    private final int value;
    private final String label;
    private final String raw;

    Gender(int value, String label, String raw) {
        this.value = value;
        this.label = label;
        this.raw = raw;
    }

    public static Gender from(String raw) {
        return Arrays.stream(values())
                .filter(g -> g.raw.equalsIgnoreCase(raw))
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM));
    }

    public static Gender fromLabel(String label) {
        return Arrays.stream(values())
                .filter(e -> e.label.equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM));
    }
}
