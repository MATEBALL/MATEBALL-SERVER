package at.mateball.common;

import at.mateball.exception.ErrorCode;
import at.mateball.exception.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;

public record MateballResponse<T>(
        int status,
        String message,
        @JsonInclude(value = JsonInclude.Include.NON_NULL)
        T data
) {
    public static <T> MateballResponse<T> success(SuccessCode successCode, T data) {
        return new MateballResponse<>(successCode.getStatus().value(), successCode.getMessage(), data);
    }

    public static <T> MateballResponse<T> successWithNoData(SuccessCode successCode) {
        return new MateballResponse<>(successCode.getStatus().value(), successCode.getMessage(), null);
    }

    public static <T> MateballResponse<T> failure(ErrorCode failureCode) {
        return new MateballResponse<>(failureCode.getStatus().value(), failureCode.getMessage(), null);
    }
}
