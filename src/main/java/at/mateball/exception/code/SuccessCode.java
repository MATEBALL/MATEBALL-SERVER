package at.mateball.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {

    // 200 OK
    OK(HttpStatus.OK, "요청이 성공적으로 처리되었습니다."),

    // 201 CREATED
    CREATED(HttpStatus.CREATED, "리소스가 성공적으로 생성되었습니다."),

    // 204 No Content
    NO_CONTENT(HttpStatus.NO_CONTENT, "응답 본문이 없습니다.");

    private final HttpStatus status;
    private final String message;

    SuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
