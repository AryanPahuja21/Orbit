package com.exceptions.orbit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
                        "Not Found", ex.getMessage(), request.getRequestURI()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderItemNotFound(OrderItemNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        ), HttpStatus.NOT_FOUND);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Handle business logic exceptions
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.error("Business error: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(),
                        "Business Error", ex.getMessage(), request.getRequestURI()),
                HttpStatus.CONFLICT
        );
    }

    // Handle unauthorized access
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        log.warn("Unauthorized: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(),
                        "Unauthorized", ex.getMessage(), request.getRequestURI()),
                HttpStatus.UNAUTHORIZED
        );
    }

    // Catch-all handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error: ", ex);
        return new ResponseEntity<>(
                new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal Server Error", "Something went wrong, please try again later.", request.getRequestURI()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
