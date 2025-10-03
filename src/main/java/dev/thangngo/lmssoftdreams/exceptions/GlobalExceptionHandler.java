package dev.thangngo.lmssoftdreams.exceptions;

import dev.thangngo.lmssoftdreams.dtos.response.ApiResponse;
import dev.thangngo.lmssoftdreams.enums.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("Constraint violation: {}", e.getMessage());
        return buildResponse(ErrorCode.CONSTRAINT_VIOLATION,
                e.getMessage(),
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<List<String>>> handleGeneralException(Exception e) {
        log.error("Unexpected exception: ", e);
        return buildResponse(ErrorCode.UNEXPECTED_ERROR,
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                List.of(e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("JSON parse error: {}", e.getMessage());
        String userFriendlyMessage = "Dữ liệu nhập vào không hợp lệ. Vui lòng kiểm tra định dạng JSON và giá trị của các trường.";
        String causeMessage = e.getMostSpecificCause().getMessage();
        String errorDetail = causeMessage;
        if (causeMessage.contains("not one of the values accepted for Enum class")) {
            String field = extractFieldName(causeMessage);
            String values = extractEnumValues(causeMessage);
            errorDetail = String.format("Trường '%s' chỉ được phép có giá trị: %s", field, values);
        }

        return buildResponse(
                ErrorCode.JSON_PARSE_ERROR,
                userFriendlyMessage,
                HttpStatus.BAD_REQUEST,
                List.of(errorDetail)
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.error("Database constraint violation: ", e);
        return buildResponse(
                ErrorCode.DB_CONSTRAINT_VIOLATION,
                "Vi phạm ràng buộc dữ liệu",
                HttpStatus.CONFLICT,
                List.of(e.getMostSpecificCause().getMessage())
        );
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

    private String extractFieldName(String message) {
        int start = message.lastIndexOf("[\"");
        int end = message.lastIndexOf("\"]");
        if (start != -1 && end != -1 && start < end) {
            return message.substring(start + 2, end);
        }
        return "unknown_field";
    }

    private String extractEnumValues(String message) {
        int start = message.indexOf('[');
        int end = message.indexOf(']', start);
        if (start != -1 && end != -1 && start < end) {
            return message.substring(start + 1, end);
        }
        return "unknown_values";
    }

}
