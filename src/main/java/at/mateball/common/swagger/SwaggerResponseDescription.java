package at.mateball.common.swagger;

import at.mateball.exception.ErrorCode;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

import static at.mateball.exception.ErrorCode.*;

@Getter
public enum SwaggerResponseDescription {

    SAMPLE_GET(new LinkedHashSet<>(Set.of(
    ))),
    SAMPLE_POST(new LinkedHashSet<>(Set.of(
            // enum을 사용하려는 api에서 해당되는 에러만 추가 -> ex) 300자 이상 입력해야합니다
    )));

    private final Set<ErrorCode> errorCodeList;

    SwaggerResponseDescription(Set<ErrorCode> errorCodeList) {
        errorCodeList.addAll(new LinkedHashSet<>(Set.of(
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
        )));

        this.errorCodeList = errorCodeList;
    }

}
