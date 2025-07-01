package at.mateball.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400 BAD REQUEST
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "요청 본문이 잘못되었습니다."),
    MISSING_PATH_VARIABLE(HttpStatus.BAD_REQUEST, "경로 변수가 누락된 잘못된 URL입니다."),
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "요청 파라미터 타입이 잘못되었습니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    UNAUTHORIZED_HEADER(HttpStatus.UNAUTHORIZED, "요청 헤더가 올바르지 않는 토큰입니다."),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 404 NOT FOUND
    NOT_FOUND_URL(HttpStatus.NOT_FOUND, "요청한 URL을 찾을 수 없습니다."),

    // 405 METHOD NOT ALLOWED
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않는 HTTP 메서드입니다."),

    // 500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    // VALIDATION
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "요청값이 유효하지 않습니다."),
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "잘못된 인자가 전달되었습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
