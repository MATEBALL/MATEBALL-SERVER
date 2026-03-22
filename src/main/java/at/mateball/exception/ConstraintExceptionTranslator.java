package at.mateball.exception;

import at.mateball.exception.code.BusinessErrorCode;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConstraintExceptionTranslator {

    private static final Map<String, BusinessErrorCode> CONSTRAINT_MAP = Map.of(
            "uk_group_chatting", BusinessErrorCode.CHATTING_ALREADY_USED,
            "uk_group_member_user_group", BusinessErrorCode.EXCEED_MATCHING_LIMIT
    );

    public BusinessException translate(Exception e) {
        String constraint = extractConstraintName(e);

        BusinessErrorCode errorCode = CONSTRAINT_MAP.get(constraint);
        if (errorCode != null) {
            return new BusinessException(errorCode);
        }

        return new BusinessException(BusinessErrorCode.ERROR_UNKNOWN_ERROR);
    }

    private String extractConstraintName(Throwable e) {
        Throwable cause = e;

        while (cause != null) {
            if (cause instanceof ConstraintViolationException exception) {
                return exception.getConstraintName();
            }
            cause = cause.getCause();
        }

        return null;
    }
}
