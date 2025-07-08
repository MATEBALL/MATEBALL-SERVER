package at.mateball.common.swagger;

import at.mateball.exception.code.ErrorCode;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

import static at.mateball.exception.code.BusinessErrorCode.DUPLICATED_NICKNAME;
import static at.mateball.exception.code.BusinessErrorCode.USER_NOT_FOUND;
import static at.mateball.exception.code.BusinessErrorCode.GROUP_NOT_FOUND;
import static at.mateball.exception.code.CommonErrorCode.*;

@Getter
public enum SwaggerResponseDescription {

    UPDATE_NICKNAME(
            new LinkedHashSet<>(Set.of(USER_NOT_FOUND, DUPLICATED_NICKNAME))
    ),

    DIRECT_MATCHING(new LinkedHashSet<>(Set.of(GROUP_NOT_FOUND)));

    private final Set<ErrorCode> commonErrorCodeList;

    SwaggerResponseDescription(Set<ErrorCode> errorCodeList) {
        errorCodeList.addAll(Set.of(
                INVALID_REQUEST_BODY,
                MISSING_PATH_VARIABLE,
                METHOD_ARGUMENT_TYPE_MISMATCH,
                UNAUTHORIZED,
                UNAUTHORIZED_HEADER,
                FORBIDDEN,
                NOT_FOUND_URL,
                METHOD_NOT_ALLOWED,
                INTERNAL_SERVER_ERROR,
                VALIDATION_ERROR,
                ILLEGAL_ARGUMENT
        ));

        this.commonErrorCodeList = errorCodeList;
    }

}
