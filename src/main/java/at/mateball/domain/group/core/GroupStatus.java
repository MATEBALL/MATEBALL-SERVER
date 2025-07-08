package at.mateball.domain.group.core;

import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum GroupStatus {
    PENDING(1, "대기중"),
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
}
