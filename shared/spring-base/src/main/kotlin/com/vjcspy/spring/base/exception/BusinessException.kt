package com.vjcspy.spring.base.exception

/**
 * Base exception class for business logic errors
 *
 * @property errorCode The error code associated with this exception
 * @property message The error message from ErrorCode (overridden from Throwable)
 */
class BusinessException : RuntimeException {
    val errorCode: ErrorCode

    constructor(errorCode: ErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }

    constructor(errorCode: ErrorCode, cause: Throwable) : super(errorCode.message, cause) {
        this.errorCode = errorCode
    }

    companion object {
        /**
         * Creates a BusinessException with the given error code
         */
        fun of(errorCode: ErrorCode) = BusinessException(errorCode)

        /**
         * Creates a BusinessException with the given error code and cause
         */
        fun of(errorCode: ErrorCode, cause: Throwable) = BusinessException(errorCode, cause)
    }
}