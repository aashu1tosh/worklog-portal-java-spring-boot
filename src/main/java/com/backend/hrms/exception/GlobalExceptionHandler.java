package com.backend.hrms.exception;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.backend.hrms.dto.ApiResponse.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
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
}
