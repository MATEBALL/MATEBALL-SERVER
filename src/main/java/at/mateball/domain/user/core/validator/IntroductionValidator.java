package at.mateball.domain.user.core.validator;

import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;

public class IntroductionValidator {
    private static final Integer MIN_INTRODUCTION_LENGTH = 1;
    private static final Integer MAX_INTRODUCTION_LENGTH = 50;

    public static void validate(String introduction) {
        if (introduction == null || introduction.isBlank() || introduction.length() < MIN_INTRODUCTION_LENGTH || introduction.length() > MAX_INTRODUCTION_LENGTH) {
            throw new BusinessException(BusinessErrorCode.INVALID_INTRODUCTION_LENGTH);
        }
    }
}
