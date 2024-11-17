package com.vjcspy.spring.base.exception

import com.vjcspy.spring.base.dto.response.ErrorResponse
import com.vjcspy.spring.base.dto.response.toResponseEntity
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.nio.file.AccessDeniedException

/**
 * Global exception handler for the application.
 * Handles all uncaught exceptions and converts them to appropriate ErrorResponse objects.
 */
@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(
        ex: BusinessException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val path = request.getRequestPath()
        val error = ErrorResponse.of(ex.errorCode, path).let {
            if (!isProduction()) it.withStackTrace(ex) else it
        }

        when {
            error.httpStatus >= 500 -> log.error("Business error occurred: {}", error, ex)
            else -> log.warn("Business error occurred: {}", error)
        }

        return error.toResponseEntity()
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        ex: AccessDeniedException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val path = request.getRequestPath()
        val error = ErrorResponse.of(ErrorCode.ACCESS_DENIED, path).let {
            if (!isProduction()) it.withStackTrace(ex) else it
        }

        log.warn("Access denied error occurred: {}", error)

        return error.toResponseEntity()
    }

    @ExceptionHandler(Exception::class)
    fun handleAllUncaughtException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val path = request.getRequestPath()
        val error = ErrorResponse.of(ErrorCode.SYSTEM_ERROR, path).let {
            if (!isProduction()) it.withStackTrace(ex) else it
        }

        log.error("Uncaught error occurred: ", ex)

        return error.toResponseEntity()
    }

    companion object {
        /**
         * Check if the application is running in production environment
         */
        private fun isProduction(): Boolean {
            // Implement logic to check environment
            return false
        }
    }
}

/**
 * Extension function to get request path from WebRequest
 */
private fun WebRequest.getRequestPath(): String =
    (this as ServletWebRequest).request.requestURI
