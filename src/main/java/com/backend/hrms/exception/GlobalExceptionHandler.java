package com.backend.hrms.exception;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.backend.hrms.dto.apiResponse.ApiResponse;

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

        return ResponseEntity
                .status(ex.getStatus())
                .body(body);
    }

        @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        // You can customize the message here
        ApiResponse<Void> body = new ApiResponse<>(false, "Access Denied: You do not have permission to access this resource.", null);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN) // Use 403 Forbidden for access denied
                .body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        ApiResponse<Void> body = new ApiResponse<>(false, "Authentication Required: Please provide valid credentials.", null);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // Use 401 Unauthorized
                .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleUnhandled(Exception ex) {

        Map<String, Object> data = exposeErrors
                ? Map.of(
                        "orgError", ex.getClass().getName(),
                        "stackTrace", getStackTrace(ex)
                )
                : Map.of();

        ApiResponse<Map<String, Object>> body = new ApiResponse<>(false, "Internal server error", data);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
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
