package at.mateball.domain.user.core.validator;

import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;

import java.util.regex.Pattern;

public class NicknameValidator {

    private static final Pattern KOREAN_ONLY = Pattern.compile("^[가-힣]+$");
    private static final Pattern ENGLISH_ONLY = Pattern.compile("^[a-zA-Z]+$");
    private static final int MIN_NICKNAME_LENGTH = 2;
    private static final int MAX_NICKNAME_LENGTH = 6;

    public static void validate(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new BusinessException(BusinessErrorCode.BLANKED_NICKNAME);
        }

        if (nickname.length() < MIN_NICKNAME_LENGTH || nickname.length() > MAX_NICKNAME_LENGTH) {
            throw new BusinessException(BusinessErrorCode.INVALID_NICKNAME_LENGTH);
        }

        if (nickname.contains(" ")) {
            throw new BusinessException(BusinessErrorCode.NICKNAME_CONTAINS_WHITESPACE);
        }

        boolean isKorean = KOREAN_ONLY.matcher(nickname).matches();
        boolean isEnglish = ENGLISH_ONLY.matcher(nickname).matches();

        if (!isKorean && !isEnglish) {
            throw new BusinessException(BusinessErrorCode.INVALID_NICKNAME_CHARACTER);
        }
    }
}
