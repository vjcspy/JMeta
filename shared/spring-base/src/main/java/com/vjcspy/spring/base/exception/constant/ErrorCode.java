package com.vjcspy.spring.base.exception.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // System Errors (SYS-XXX)
    SYSTEM_ERROR("SYS-001", "system.error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT("SYS-002", "invalid.input", HttpStatus.BAD_REQUEST),

    // Authentication Errors (AUTH-XXX)
    UNAUTHORIZED("AUTH-001", "unauthorized", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("AUTH-002", "token.expired", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("AUTH-003", "AccessDenied", HttpStatus.FORBIDDEN),

    // Business Errors (BUS-XXX)
    USER_NOT_FOUND("BUS-001", "user.not.found", HttpStatus.NOT_FOUND),
    INSUFFICIENT_BALANCE("BUS-002", "insufficient.balance", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
