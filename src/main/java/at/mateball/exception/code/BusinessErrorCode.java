package at.mateball.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCode implements ErrorCode {
    // 400 BAD REQUEST
    MISSING_TOKEN(HttpStatus.BAD_REQUEST, "요청 쿠키에 토큰이 없습니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, "토큰 형식이 잘못되었습니다."),
    KAKAO_TOKEN_FETCH_FAILED(HttpStatus.BAD_REQUEST, "카카오 인가 코드로 토큰을 가져오는 데 실패했습니다."),
    KAKAO_USER_INFO_FETCH_FAILED(HttpStatus.BAD_REQUEST, "카카오 사용자 정보를 가져오는 데 실패했습니다."),
    BAD_REQUEST_ENUM(HttpStatus.BAD_REQUEST, "존재하지 않는 enum값입니다."),
    BAD_REQUEST_MONDAY(HttpStatus.BAD_REQUEST, "월요일은 경기가 없습니다."),
    BAD_REQUEST_PAST(HttpStatus.BAD_REQUEST,"과거에 머물러있지 마십쇼. 이미 종료된 경기입니다."),
    BAD_REQUEST_DATE(HttpStatus.BAD_REQUEST, "매칭신청은 2일 전까지 가능합니다."),
    EXCEED_GROUP_MATCHING_LIMIT(HttpStatus.BAD_REQUEST, "그룹 매칭 요청은 최대 2건까지 가능합니다."),
    EXCEED_DIRECT_MATCHING_LIMIT(HttpStatus.BAD_REQUEST, "솔로 매칭 요청은 최대 3건까지 가능합니다."),
    ALREADY_HAS_PENDING_REQUEST(HttpStatus.BAD_REQUEST, "이미 요청이 있는 매칭입니다."),

    // 401 UNAUTHORIZED
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"만료된 토큰입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 유효하지 않습니다."),
    INVALID_KAKAO_TOKEN(HttpStatus.UNAUTHORIZED, "Kakao Access Token이 유효하지 않습니다."),
    INVALID_SERVER_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 서버 JWT입니다."),
    KAKAO_CLIENT_ERROR(HttpStatus.UNAUTHORIZED, "카카오 JWT 파싱 중 오류가 발생했습니다."),

    // 403 FORBIDDEN
    AGE_NOT_APPROPRIATE(HttpStatus.FORBIDDEN, "만 19세 이상부터 가입이 가능합니다."),

    // 404 NOT FOUND
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "요청에 해당하는 토큰이 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 사용자입니다."),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 매칭입니다."),

    // 409 CONFLICT
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "중복된 닉네임입니다.");

    private final HttpStatus status;
    private final String message;

    BusinessErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
