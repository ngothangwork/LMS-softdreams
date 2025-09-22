package dev.thangngo.lmssoftdreams.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    APP_EXCEPTION(1001, "Application exception", HttpStatus.BAD_REQUEST),
    VALIDATION_FAILED(1002, "Validation failed", HttpStatus.BAD_REQUEST),
    CONSTRAINT_VIOLATION(1003, "Constraint violation", HttpStatus.BAD_REQUEST),
    ILLEGAL_ARGUMENT(1004, "Invalid argument", HttpStatus.BAD_REQUEST),
    NULL_POINTER(1005, "Null pointer exception", HttpStatus.INTERNAL_SERVER_ERROR),
    ILLEGAL_STATE(1006, "Illegal state", HttpStatus.CONFLICT),
    RUNTIME_ERROR(1000, "Runtime error", HttpStatus.BAD_REQUEST),
    UNEXPECTED_ERROR(9999, "Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR),
    MEMBER_ALREADY_EXISTS(1007, "User is already a member of this plan", HttpStatus.CONFLICT),
    USER_NOT_FOUND(1008, "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1009, "User already exists", HttpStatus.CONFLICT),
    BOOK_NOT_FOUND(1010, "Book not found", HttpStatus.NOT_FOUND),
    BOOK_ALREADY_EXISTS(1011, "Book already exists", HttpStatus.CONFLICT),
    START_DATE_AFTER_END_DATE(1012, "Start date must be before end date", HttpStatus.BAD_REQUEST),
    BUSY_IN_THIS_TIME(1013, "You are busy in this time", HttpStatus.CONFLICT),
    INVALID_DATE_FORMAT(1014, "Invalid date format", HttpStatus.BAD_REQUEST),
    PASS_WORD_NOT_MATCH(1015, "Password not match", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(1016, "Email already exists", HttpStatus.CONFLICT),
    PHONE_NUMBER_ALREADY_EXISTS(1017, "Phone number already exists", HttpStatus.CONFLICT),
    AUTHOR_NOT_FOUND(1018, "Author not found", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(1019, "Category not found", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS(1020, "Category already exists", HttpStatus.CONFLICT),
    BOOK_COPY_NOT_FOUND(1021, "Book copy not found", HttpStatus.NOT_FOUND),
    BORROW_NOT_FOUND(1022, "Borrow not found", HttpStatus.NOT_FOUND),
    BOOK_COPY_ALREADY_BORROWED(1023, "Book copy is already borrowed", HttpStatus.CONFLICT),
    BARCODE_ALREADY_EXISTS(1024, "Barcode already exists", HttpStatus.CONFLICT),
    PUBLISHER_NOT_FOUND(1025, "Publisher not found", HttpStatus.NOT_FOUND),
    PUBLISHER_ALREADY_EXISTS(1026, "Publisher already exists", HttpStatus.CONFLICT),
    ISBN_ALREADY_EXISTS(1027, "ISBN already exists", HttpStatus.CONFLICT),
    BOOK_COPY_NOT_AVAILABLE(1028, "Book copy is not available", HttpStatus.CONFLICT),
    BOOK_COPY_NOT_FOUND_OR_NOT_AVAILABLE(1029, "Book copy not found or not available", HttpStatus.NOT_FOUND),
    START_DATE_BEFORE_CURRENT_DATE(1030, "Start date must be after current date", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus status;
}
