package dev.thangngo.travelmate.exceptions;

import dev.thangngo.travelmate.dtos.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<String>> handleAppException(AppException e) {
        log.warn("AppException: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return buildResponse(errorCode, errorCode.getMessage(), errorCode.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("Validation failed: {}", e.getMessage());

        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        ApiResponse<List<String>> apiResponse = ApiResponse.<List<String>>builder()
                .success(false)
                .code(ErrorCode.VALIDATION_FAILED.getCode())
                .message(ErrorCode.VALIDATION_FAILED.getMessage())
                .result(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("Constraint violation: {}", e.getMessage());
        return buildResponse(ErrorCode.CONSTRAINT_VIOLATION, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument: {}", e.getMessage());
        return buildResponse(ErrorCode.ILLEGAL_ARGUMENT, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<String>> handleNullPointerException(NullPointerException e) {
        log.error("Null pointer exception: ", e);
        return buildResponse(ErrorCode.NULL_POINTER, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalStateException(IllegalStateException e) {
        log.error("Illegal state exception: ", e);
        return buildResponse(ErrorCode.ILLEGAL_STATE, e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException e) {
        log.error("Runtime exception: ", e);
        return buildResponse(ErrorCode.RUNTIME_ERROR, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception e) {
        log.error("Unexpected exception: ", e);
        return buildResponse(ErrorCode.UNEXPECTED_ERROR, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiResponse<String>> buildResponse(ErrorCode errorCode, String message, HttpStatus status) {
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .success(false)
                .code(errorCode.getCode())
                .message(message != null ? message : errorCode.getMessage())
                .build();
        return ResponseEntity.status(status).body(apiResponse);
    }


}
