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
    BAD_REQUEST_PAST(HttpStatus.BAD_REQUEST, "과거에 머물러있지 마십쇼. 이미 종료된 경기입니다."),
    BAD_REQUEST_DATE(HttpStatus.BAD_REQUEST, "매칭생성 및 신청은 2일 전까지 가능합니다."),
    ALREADY_HAS_PENDING_REQUEST(HttpStatus.BAD_REQUEST, "이미 요청이 존재하는 매칭입니다."),
    DUPLICATE_MATCHING_ON_SAME_DATE(HttpStatus.BAD_REQUEST, "같은 날짜의 경기에는 하나의 매칭만 참여할 수 있습니다."),
    ALREADY_COMPLETED_GROUP(HttpStatus.BAD_REQUEST, "이미 매칭이 완료된 그룹입니다."),
    INVALID_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, "닉네임은 2자 이상 6자 이하로 입력해야 합니다."),
    NICKNAME_CONTAINS_WHITESPACE(HttpStatus.BAD_REQUEST, "닉네임에 공백이 포함될 수 없습니다."),
    INVALID_NICKNAME_CHARACTER(HttpStatus.BAD_REQUEST, "닉네임은 한글 또는 영어만 사용할 수 있으며, 특수문자는 사용할 수 없습니다."),
    BAD_REQUEST_MATCH_TYPE(HttpStatus.BAD_REQUEST, "요청 matchType이 잘못되었습니다."),
    NOT_ALLOWED_AGE(HttpStatus.BAD_REQUEST, "매칭 가능한 나이가 아닙니다."),
    INVALID_GROUP_STATUS(HttpStatus.BAD_REQUEST, "매칭 완료 상태가 아닙니다."),
    GROUP_NOT_FOUND_OR_ALREADY_UPDATED(HttpStatus.BAD_REQUEST, "업데이트할 그룹이 없거나 이미 업데이트된 상태입니다."),
    INVALID_INTRODUCTION_LENGTH(HttpStatus.BAD_REQUEST, "한줄소개는 1자 이상 50자 이하로 입력해야 합니다."),
    INVALID_TEAM_ALLOWED(HttpStatus.BAD_REQUEST, "'응원하는 팀이 없어요'는 '상관 없어요'만 선택가능합니다."),
    EMPTY_PROFILE_IMAGE(HttpStatus.BAD_REQUEST, "업로드할 프로필 이미지가 없습니다."),
    INVALID_PROFILE_IMAGE_FORMAT(HttpStatus.BAD_REQUEST, "프로필 이미지는 jpg, png, webp 형식만 업로드할 수 있습니다."),
    INVALID_PROFILE_IMAGE_SIZE(HttpStatus.BAD_REQUEST, "프로필 이미지는 5MB 이하만 업로드할 수 있습니다."),
    OWN_MATCH_MEMBER_VIEW_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "내가 만든 매칭은 매칭현황에서 확인 가능해요."),
    INVALID_AVG_SEASON(HttpStatus.BAD_REQUEST, "시즌 평균 직관 수는 0-999까지 입력 가능합니다."),
    WAITING_MATE_ACCEPTANCE(HttpStatus.BAD_REQUEST,"메이트의 수락을 기다리는 중입니다."),
    INVALID_CHATTING_REQUEST_MEMBER(HttpStatus.BAD_REQUEST, "생성자와 승인된 요청자만 채팅방에 입장할 수 있습니다."),
    ALREADY_FINISHED_MATCH(HttpStatus.BAD_REQUEST, "이미 종료된 매칭입니다."),
    CANNOT_REQUEST_OWN_MATCH(HttpStatus.BAD_REQUEST, "내가 만든 매칭에는 요청을 보낼 수 없습니다."),

    // 401 UNAUTHORIZED
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 유효하지 않습니다."),
    INVALID_KAKAO_TOKEN(HttpStatus.UNAUTHORIZED, "Kakao Access Token이 유효하지 않습니다."),
    INVALID_SERVER_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 서버 JWT입니다."),
    KAKAO_CLIENT_ERROR(HttpStatus.UNAUTHORIZED, "카카오 JWT 파싱 중 오류가 발생했습니다."),
    LOGGED_OUT_TOKEN(HttpStatus.UNAUTHORIZED, "이미 로그아웃된 토큰입니다."),

    // 403 FORBIDDEN
    AGE_NOT_APPROPRIATE(HttpStatus.FORBIDDEN, "만 19세 이상부터 가입이 가능합니다."),
    NOT_GROUP_MEMBER(HttpStatus.FORBIDDEN, "요청을 처리할 권한이 없습니다. 그룹의 참가자가 아닙니다."),
    NOT_MATCH_LEADER(HttpStatus.FORBIDDEN, "요청을 처리할 권한이 없습니다. 매칭 생성자가 아닙니다."),

    // 404 NOT FOUND
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "요청에 해당하는 토큰이 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 매칭입니다."),
    REQUESTER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 요청자입니다."),
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 경기입니다."),
    MATCH_REQUIREMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "매칭 조건 정보가 존재하지 않습니다."),
    CHATTING_NOT_FOUND(HttpStatus.NOT_FOUND, "오픈채팅이 존재하지 않습니다."),
    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다."),
    NO_GAME_SCHEDULED(HttpStatus.NOT_FOUND, "선택한 날짜에 경기가 존재하지 않습니다."),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "요청 정보를 찾을 수 없습니다."),

    // 409 CONFLICT
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "중복된 닉네임입니다."),
    ALREADY_FAILED_REQUEST(HttpStatus.CONFLICT, "이미 요청이 실패한 매칭입니다."),
    DUPLICATED_REQUEST(HttpStatus.CONFLICT, "이미 요청을 전송한 매칭입니다."),
    DUPLICATED_INFO(HttpStatus.BAD_REQUEST, "이미 사용자 정보가 존재합니다. 사용자 정보 설정은 최초 한 번만 가능합니다."),
    DUPLICATED_MATCH_REQUIREMENT(HttpStatus.CONFLICT, "이미 매칭 조건이 존재합니다."),
    CHATTING_ALREADY_USED(HttpStatus.CONFLICT, "이미 배정된 오픈채팅입니다. 매칭에 오픈채팅을 할당할 수 없습니다."),
    DUPLICATED_MATCH_REQUEST(HttpStatus.CONFLICT, "요청 이력이 있는 매칭입니다."),

    // 429 TOO MANY REQUESTS
    EXCEED_GROUP_MATCHING_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "그룹 매칭은 최대 2개까지만 가능합니다."),
    EXCEED_DIRECT_MATCHING_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "1대1 매칭은 최대 3개까지만 가능합니다."),
    EXCEED_MATCHING_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "한 경기에는 하나의 매칭만 생성할 수 있습니다."),

    // 501 Not Supported Error
    ERROR_UNKNOWN_ERROR(HttpStatus.NOT_IMPLEMENTED, "서버 내부 오류: 에러 코드를 추출할 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    BusinessErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
