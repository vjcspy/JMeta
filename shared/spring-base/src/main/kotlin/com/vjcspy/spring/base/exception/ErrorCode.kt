package com.vjcspy.spring.base.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: String,
    val message: String,
    val httpStatus: HttpStatus
) {
    // System Errors (SYS-XXX)
    SYSTEM_ERROR(
        code = "SYS-001",
        message = "system.error",
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    ),
    INVALID_INPUT(
        code = "SYS-002",
        message = "invalid.input",
        httpStatus = HttpStatus.BAD_REQUEST
    ),

    // Authentication Errors (AUTH-XXX)
    UNAUTHORIZED(
        code = "AUTH-001",
        message = "unauthorized",
        httpStatus = HttpStatus.UNAUTHORIZED
    ),
    TOKEN_EXPIRED(
        code = "AUTH-002",
        message = "token.expired",
        httpStatus = HttpStatus.UNAUTHORIZED
    ),
    ACCESS_DENIED(
        code = "AUTH-003",
        message = "AccessDenied",
        httpStatus = HttpStatus.FORBIDDEN
    ),

    // Business Errors (BUS-XXX)
    USER_NOT_FOUND(
        code = "BUS-001",
        message = "user.not.found",
        httpStatus = HttpStatus.NOT_FOUND
    ),
    INSUFFICIENT_BALANCE(
        code = "BUS-002",
        message = "insufficient.balance",
        httpStatus = HttpStatus.BAD_REQUEST
    );

    companion object {
        /**
         * Find ErrorCode by code string
         */
        fun fromCode(code: String): ErrorCode? =
            ErrorCode.entries.firstOrNull { it.code == code }

        /**
         * Check if code exists
         */
        fun isValidCode(code: String): Boolean =
            ErrorCode.entries.any { it.code == code }
    }
}

fun ErrorCode.isSystemError(): Boolean =
    this.code.startsWith("SYS-")

fun ErrorCode.isAuthError(): Boolean =
    this.code.startsWith("AUTH-")

fun ErrorCode.isBusinessError(): Boolean =
    this.code.startsWith("BUS-")