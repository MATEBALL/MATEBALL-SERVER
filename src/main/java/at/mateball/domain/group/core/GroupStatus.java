package at.mateball.domain.group.core;

import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum GroupStatus {
    PENDING(1, "대기 중"),
    COMPLETED(2, "완료"),
    FAILED(3, "실패");

    private final int value;
    private final String label;

    GroupStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static GroupStatus from(int value) {
        return Arrays.stream(values())
                .filter(g -> g.value == value)
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM));
    }

    public static GroupStatus fromCode(String label) {
        return Arrays.stream(values())
                .filter(g -> g.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM));
    }

    public static String labelOf(int value) {
        return Arrays.stream(values())
                .filter(status -> status.value == value)
                .map(status -> status.label)
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM));
    }

    public String toResponseLabel() {
        return switch (this) {
            case PENDING -> "그룹원 모집중";
            case COMPLETED -> "완료";
            case FAILED -> "실패";
        };
    }
}
