package com.vjcspy.spring.base.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vjcspy.spring.base.exception.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private final String code;                   // Internal error code
    private final String message;                // Error message
    private final int httpStatus;                // HTTP status code
    private final String httpStatusText;         // HTTP status text

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private final LocalDateTime timestamp;       // Error timestamp

    private final String path;                   // Request path
    private final Map<String, Object> details;   // Additional error details

    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .httpStatus(errorCode.getHttpStatus().value())
                .httpStatusText(errorCode.getHttpStatus().getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .path(path)
                .details(new HashMap<>())
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
                .details(new HashMap<>())
                .build();
    }

    public ErrorResponse withDetail(String key, Object value) {
        Map<String, Object> newDetails = new HashMap<>(this.details);
        newDetails.put(key, value);

        return ErrorResponse.builder()
                .code(this.code)
                .message(this.message)
                .httpStatus(this.httpStatus)
                .httpStatusText(this.httpStatusText)
                .timestamp(this.timestamp)
                .path(this.path)
                .details(newDetails)
                .build();
    }

    public Map<String, Object> getDetails() {
        return Collections.unmodifiableMap(details);
    }

    public ErrorResponse addDetail(String key, Object value) {
        this.details.put(key, value);

        return this;
    }
}