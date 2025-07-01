package at.mateball.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCode {
    // 400 BAD REQUEST
    MISSING_TOKEN(HttpStatus.BAD_REQUEST, "요청 쿠키에 토큰이 없습니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, "토큰 형식이 잘못되었습니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 유효하지 않습니다."),
    INVALID_KAKAO_TOKEN(HttpStatus.UNAUTHORIZED, "Kakao Access Token이 유효하지 않습니다."),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 404 NOT FOUND
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "요청에 해당하는 토큰이 존재하지 않습니다."),

    // 405 METHOD NOT ALLOWED
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않는 HTTP 메서드입니다.");

    private final HttpStatus status;
    private final String message;

    BusinessErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
