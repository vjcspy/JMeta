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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.stream.Collectors;

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
            error.addDetail("exception", ex.getClass().getName());
            error.addDetail("stackTrace", getStackTraceAsString(ex));
        }

        return new ResponseEntity<>(error, ex.getErrorCode().getHttpStatus());
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
//            ConstraintViolationException ex, WebRequest request) {
//        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
//        ErrorResponse error = ErrorResponse.of(ErrorCode.VALIDATION_ERROR, path);
//
//        Map<String, String> violations = ex.getConstraintViolations().stream()
//                .collect(Collectors.toMap(
//                        violation -> violation.getPropertyPath().toString(),
//                        violation -> violation.getMessage(),
//                        (existing, replacement) -> existing
//                ));
//
//        error.addDetail("violations", violations);
//        log.warn("Validation error occurred: {}", error);
//
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
//            DataIntegrityViolationException ex, WebRequest request) {
//        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
//        ErrorResponse error = ErrorResponse.of(ErrorCode.DATA_INTEGRITY_ERROR, path);
//
//        log.error("Data integrity error occurred: {}", ex.getMessage(), ex);
//
//        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
//    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponse error = ErrorResponse.of(ErrorCode.ACCESS_DENIED, path);

        log.warn("Access denied error occurred: {}", error);

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
//            BadCredentialsException ex, WebRequest request) {
//        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
//        ErrorResponse error = ErrorResponse.of(ErrorCode.INVALID_CREDENTIALS, path);
//
//        log.warn("Invalid credentials error occurred for path: {}", path);
//
//        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
//    }

//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException ex,
//            HttpHeaders headers,
//            HttpStatus status,
//            WebRequest request) {
//        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
//        ErrorResponse error = ErrorResponse.of(ErrorCode.VALIDATION_ERROR, path);
//
//        Map<String, String> validationErrors = new HashMap<>();
//        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
//            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
//        }
//
//        error.addDetail("validationErrors", validationErrors);
//        log.warn("Validation error occurred: {}", error);
//
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }

//    @Override
//    protected ResponseEntity<Object> handleMissingServletRequestParameter(
//            MissingServletRequestParameterException ex,
//            HttpHeaders headers,
//            HttpStatus status,
//            WebRequest request) {
//        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
//        ErrorResponse error = ErrorResponse.of(ErrorCode.MISSING_PARAMETER, path);
//        error.addDetail("parameterName", ex.getParameterName());
//        error.addDetail("parameterType", ex.getParameterType());
//
//        log.warn("Missing parameter error occurred: {}", error);
//
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(
            Exception ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponse error = ErrorResponse.of(ErrorCode.SYSTEM_ERROR, path);

        log.error("Uncaught error occurred: ", ex);

        // Trong môi trường production, không nên expose stack trace
        if (!isProduction()) {
            error.addDetail("exception", ex.getClass().getName());
            error.addDetail("stackTrace", getStackTraceAsString(ex));
        }

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isProduction() {
        // Implement logic to check environment
        return false;
    }

    private String getStackTraceAsString(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}