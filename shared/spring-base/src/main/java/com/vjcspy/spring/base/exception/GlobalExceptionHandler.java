package com.vjcspy.spring.base.exception;

import com.vjcspy.spring.base.dto.response.ErrorResponse;
import com.vjcspy.spring.base.exception.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponse error = ErrorResponse.of(ex.getErrorCode(), path);

        // Log error with appropriate level based on HTTP status
        if (error.getHttpStatus() >= 500) {
            log.error("Business error occurred: {}", error, ex);
        } else {
            log.warn("Business error occurred: {}", error);
        }

        if (!isProduction()) {
            error = error.withStackTrace(ex);
        }

        return new ResponseEntity<>(error, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponse error = ErrorResponse.of(ErrorCode.ACCESS_DENIED, path);
        log.warn("Access denied error occurred: {}", error);

        if (!isProduction()) {
            error = error.withStackTrace(ex);
        }

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(
            Exception ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponse error = ErrorResponse.of(ErrorCode.SYSTEM_ERROR, path);
        log.error("Uncaught error occurred: ", ex);

        if (!isProduction()) {
            error = error.withStackTrace(ex);
        }

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isProduction() {
        // Implement logic to check environment
        return false;
    }
}