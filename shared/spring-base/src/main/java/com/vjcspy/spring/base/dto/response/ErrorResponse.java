package com.vjcspy.spring.base.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vjcspy.spring.base.exception.constant.ErrorCode;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
public class ErrorResponse implements BaseResponse<Object> {
    @Builder.Default
    private final boolean success = false;

    private final String code;                   // Internal error code
    private final String message;                // Error message
    private final int httpStatus;                // HTTP status code
    private final String httpStatusText;         // HTTP status text

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private final LocalDateTime timestamp;       // Error timestamp

    private final String path;                   // Request path

    @Builder.Default
    private final Object data = null;            // Implement BaseResponse interface

    @Builder.Default
    private final List<ValidationError> errors = new ArrayList<>();  // Validation errors

    @Builder.Default
    private final List<String> stackTrace = new ArrayList<>();      // Stack trace for debugging

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ValidationError {
        private final String field;
        private final String code;
        private final String message;
        private final Object rejectedValue;
    }

    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .httpStatus(errorCode.getHttpStatus().value())
                .httpStatusText(errorCode.getHttpStatus().getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }

    public static ErrorResponse of(String code, String message, HttpStatus status, String path) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .httpStatus(status.value())
                .httpStatusText(status.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }

    public static ErrorResponse of(String code, String message, HttpStatus status, String path,
                                   List<ValidationError> errors) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .httpStatus(status.value())
                .httpStatusText(status.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .path(path)
                .errors(errors)
                .build();
    }

    public ErrorResponse withStackTrace(Throwable ex) {
        List<String> stackTrace = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
        return ErrorResponse.builder()
                .code(this.code)
                .message(this.message)
                .httpStatus(this.httpStatus)
                .httpStatusText(this.httpStatusText)
                .timestamp(this.timestamp)
                .path(this.path)
                .errors(this.errors)
                .stackTrace(stackTrace)
                .build();
    }

    public ErrorResponse addValidationError(String field, String code, String message, Object rejectedValue) {
        ValidationError error = ValidationError.builder()
                .field(field)
                .code(code)
                .message(message)
                .rejectedValue(rejectedValue)
                .build();
        this.errors.add(error);
        return this;
    }

    public List<ValidationError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public List<String> getStackTrace() {
        return Collections.unmodifiableList(stackTrace);
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Object getData() {
        return this.data;
    }
}