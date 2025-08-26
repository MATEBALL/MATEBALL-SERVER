package at.mateball.domain.user.core;

import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;

import java.util.Arrays;

public enum UserInfoField {
    NICKNAME("닉네임"),
    INTRODUCTION("소개");

    private final String label;

    UserInfoField(String label) {
        this.label = label;
    }

    public static UserInfoField fromLabel(String label) {
        return Arrays.stream(values())
                .filter(f -> f.label.equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM));
    }
}
