package com.backend.hrms.exception;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.backend.hrms.dto.apiResponse.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${app.expose-errors:false}")
    private boolean exposeErrors;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest().body(
                Map.of("success", false, "message", "Validation Failed", "errors", errors));
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpException(HttpException ex) {
        ApiResponse<Void> body = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponse<Void> body = new ApiResponse<>(false,
                "Access Denied: You do not have permission to access this resource.", null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        ApiResponse<Void> body = new ApiResponse<>(false,
                "You are not authorized for this process.", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    // ✅ Common exception handlers below

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalState(IllegalStateException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({ EntityNotFoundException.class, NoSuchElementException.class })
    public ResponseEntity<ApiResponse<Void>> handleNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed: " + ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Database error: " + ex.getMostSpecificCause().getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadableBody(HttpMessageNotReadableException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = "Invalid value for parameter '" + ex.getName() + "': expected type " + ex.getRequiredType();
        return buildResponse(HttpStatus.BAD_REQUEST, msg);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, "HTTP method not allowed.");
    }

    @ExceptionHandler(jakarta.validation.UnexpectedTypeException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedType(jakarta.validation.UnexpectedTypeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                "There was an issue with the information you provided. Please check all fields and try again.");
    }

    // Fallback for any unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleUnhandled(Exception ex) {
        Map<String, Object> data = exposeErrors
                ? Map.of(
                        "orgError", ex.getClass().getName(),
                        "stackTrace", getStackTrace(ex))
                : Map.of();

        ApiResponse<Map<String, Object>> body = new ApiResponse<>(false, "Internal server error", data);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /* Helper: build response */
    private ResponseEntity<ApiResponse<Void>> buildResponse(HttpStatus status, String message) {
        ApiResponse<Void> body = new ApiResponse<>(false, message, null);
        return ResponseEntity.status(status).body(body);
    }

    /* Helper: stringify stack trace */
    private static String getStackTrace(Throwable t) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement el : t.getStackTrace()) {
            sb.append(el).append('\n');
        }
        return sb.toString();
    }
}
