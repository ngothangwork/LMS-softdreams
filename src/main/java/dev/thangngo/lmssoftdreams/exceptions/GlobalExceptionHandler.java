package dev.thangngo.lmssoftdreams.exceptions;

import dev.thangngo.lmssoftdreams.dtos.response.ApiResponse;
import dev.thangngo.lmssoftdreams.enums.ErrorCode;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleAppException(AppException e) {
        log.warn("AppException: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return buildResponse(errorCode, errorCode.getMessage(), errorCode.getStatus(), List.of(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("Validation failed: {}", e.getMessage());

        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        return buildResponse(ErrorCode.VALIDATION_FAILED,
                ErrorCode.VALIDATION_FAILED.getMessage(),
                HttpStatus.BAD_REQUEST,
                errors);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleBindException(BindException e) {
        log.warn("Binding error: {}", e.getMessage());
        List<String> errors = e.getBindingResult().getAllErrors()
                .stream().map(Object::toString).toList();
        return buildResponse(ErrorCode.VALIDATION_FAILED,
                "Binding error",
                HttpStatus.BAD_REQUEST,
                errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("Constraint violation: {}", e.getMessage());
        return buildResponse(ErrorCode.CONSTRAINT_VIOLATION,
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                List.of(e.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleMissingRequestParam(MissingServletRequestParameterException e) {
        log.warn("Missing request parameter: {}", e.getParameterName());
        return buildResponse(ErrorCode.VALIDATION_FAILED,
                "Missing required parameter: " + e.getParameterName(),
                HttpStatus.BAD_REQUEST,
                List.of(e.getMessage()));
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument: {}", e.getMessage());
        return buildResponse(ErrorCode.ILLEGAL_ARGUMENT,
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                List.of(e.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleNullPointerException(NullPointerException e) {
        log.error("Null pointer exception: ", e);
        return buildResponse(ErrorCode.NULL_POINTER,
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                List.of(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleIllegalStateException(IllegalStateException e) {
        log.error("Illegal state exception: ", e);
        return buildResponse(ErrorCode.ILLEGAL_STATE,
                e.getMessage(),
                HttpStatus.CONFLICT,
                List.of(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleRuntimeException(RuntimeException e) {
        log.error("Runtime exception: ", e);
        return buildResponse(ErrorCode.RUNTIME_ERROR,
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                List.of(e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleInvalidEnum(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getMostSpecificCause();

        String message = "Invalid request payload";
        List<String> errors = List.of(ex.getMessage());

        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException ife) {
            if (ife.getTargetType().isEnum()) {
                Object[] enumValues = ife.getTargetType().getEnumConstants();
                String allowed = Arrays.stream(enumValues)
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                String fieldName = ife.getPath().isEmpty() ? "unknown" : ife.getPath().get(0).getFieldName();
                message = "Invalid value for field '" + fieldName + "'. Allowed values: [" + allowed + "]";
                errors = List.of(message);
            }
        }

        return buildResponse(
                ErrorCode.VALIDATION_FAILED,
                message,
                HttpStatus.BAD_REQUEST,
                errors
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.warn("Method not allowed: {}", e.getMethod());
        return buildResponse(ErrorCode.INVALID_REQUEST,
                "Method not allowed: " + e.getMethod(),
                HttpStatus.METHOD_NOT_ALLOWED,
                List.of(e.getMessage()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        log.warn("Unsupported media type: {}", e.getContentType());
        return buildResponse(ErrorCode.INVALID_REQUEST,
                "Unsupported media type: " + e.getContentType(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                List.of(e.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleNoHandlerFound(NoHandlerFoundException e) {
        log.warn("No handler found for request: {} {}", e.getHttpMethod(), e.getRequestURL());
        return buildResponse(ErrorCode.NOT_FOUND,
                "No handler found for " + e.getHttpMethod() + " " + e.getRequestURL(),
                HttpStatus.NOT_FOUND,
                List.of(e.getMessage()));
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleAccessDenied(AccessDeniedException e) {
        log.warn("Access denied: {}", e.getMessage());
        return buildResponse(ErrorCode.ACCESS_DENIED,
                "You do not have permission to access this resource",
                HttpStatus.FORBIDDEN,
                List.of(e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.error("Data integrity violation: ", e);

        String message = "Database constraint violation";
        String detail = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : e.getMessage();

        return buildResponse(
                ErrorCode.CONSTRAINT_VIOLATION,
                message,
                HttpStatus.BAD_REQUEST,
                List.of(detail)
        );
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ApiResponse<List<String>>> handlePersistenceException(PersistenceException e) {
        log.error("Persistence exception: ", e);
        return buildResponse(
                ErrorCode.CONSTRAINT_VIOLATION,
                "Persistence error: " + e.getMessage(),
                HttpStatus.BAD_REQUEST,
                List.of(e.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<List<String>>> handleGeneralException(Exception e) {
        log.error("Unexpected exception: ", e);
        return buildResponse(ErrorCode.UNEXPECTED_ERROR,
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                List.of(e.getMessage()));
    }

    private ResponseEntity<ApiResponse<List<String>>> buildResponse(
            ErrorCode errorCode,
            String message,
            HttpStatus status,
            List<String> errors
    ) {
        ApiResponse<List<String>> apiResponse = ApiResponse.<List<String>>builder()
                .success(false)
                .code(errorCode.getCode())
                .message(message != null ? message : errorCode.getMessage())
                .result(errors)
                .build();

        return ResponseEntity.status(status).body(apiResponse);
    }
}
