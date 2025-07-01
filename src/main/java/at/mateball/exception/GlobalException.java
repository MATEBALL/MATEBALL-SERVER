package at.mateball.exception;

import at.mateball.common.MateballResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.security.access.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class GlobalException {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<MateballResponse<?>> handleBusinessException(BusinessException exception) {
        BusinessErrorCode code = exception.getBusinessErrorCode();
        return ResponseEntity.status(code.getStatus())
                .body(new MateballResponse<>(code.getStatus().value(), code.getMessage(), null));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MateballResponse<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        return toResponse(ErrorCode.INVALID_REQUEST_BODY);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MateballResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        return toResponse(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<MateballResponse<?>> handleMissingPathVariable(MissingPathVariableException exception) {
        return toResponse(ErrorCode.MISSING_PATH_VARIABLE);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<MateballResponse<?>> handleMissingRequestHeader(MissingRequestHeaderException exception) {
        return toResponse(ErrorCode.UNAUTHORIZED_HEADER);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<MateballResponse<?>> handleNotFound(NoResourceFoundException exception) {
        return toResponse(ErrorCode.NOT_FOUND_URL);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<MateballResponse<?>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException exception) {
        return toResponse(ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MateballResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        return toResponse(ErrorCode.VALIDATION_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MateballResponse<?>> handleValidation(ConstraintViolationException exception) {
        return toResponse(ErrorCode.VALIDATION_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MateballResponse<?>> handleIllegalArgument(IllegalArgumentException ex) {
        return toResponse(ErrorCode.ILLEGAL_ARGUMENT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MateballResponse<?>> handleAccessDenied(AccessDeniedException ex) {
        return toResponse(ErrorCode.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MateballResponse<?>> handleException(Exception ex) {
        log.error("서버 내부 오류 : ", ex);
        return toResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<MateballResponse<?>> toResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(MateballResponse.failure(errorCode));
    }
}
