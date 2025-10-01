package dev.thangngo.lmssoftdreams.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    APP_EXCEPTION(1001, "Lỗi ứng dụng", HttpStatus.BAD_REQUEST),
    VALIDATION_FAILED(1002, "Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST),
    CONSTRAINT_VIOLATION(1003, "Vi phạm ràng buộc dữ liệu", HttpStatus.BAD_REQUEST),
    ILLEGAL_ARGUMENT(1004, "Tham số không hợp lệ", HttpStatus.BAD_REQUEST),
    NULL_POINTER(1005, "Lỗi null pointer", HttpStatus.INTERNAL_SERVER_ERROR),
    ILLEGAL_STATE(1006, "Trạng thái không hợp lệ", HttpStatus.CONFLICT),
    RUNTIME_ERROR(1000, "Lỗi chạy chương trình", HttpStatus.BAD_REQUEST),
    UNEXPECTED_ERROR(9999, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    MEMBER_ALREADY_EXISTS(1007, "Người dùng đã là thành viên của gói này", HttpStatus.CONFLICT),
    USER_NOT_FOUND(1008, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1009, "Người dùng đã tồn tại", HttpStatus.CONFLICT),
    BOOK_NOT_FOUND(1010, "Không tìm thấy sách", HttpStatus.NOT_FOUND),
    BOOK_ALREADY_EXISTS(1011, "Sách đã tồn tại", HttpStatus.CONFLICT),
    START_DATE_AFTER_END_DATE(1012, "Ngày bắt đầu phải trước ngày kết thúc", HttpStatus.BAD_REQUEST),
    BUSY_IN_THIS_TIME(1013, "Bạn đã bận trong khoảng thời gian này", HttpStatus.CONFLICT),
    INVALID_DATE_FORMAT(1014, "Định dạng ngày không hợp lệ", HttpStatus.BAD_REQUEST),
    PASS_WORD_NOT_MATCH(1015, "Mật khẩu không khớp", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(1016, "Email đã tồn tại", HttpStatus.CONFLICT),
    PHONE_NUMBER_ALREADY_EXISTS(1017, "Số điện thoại đã tồn tại", HttpStatus.CONFLICT),
    AUTHOR_NOT_FOUND(1018, "Không tìm thấy tác giả", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(1019, "Không tìm thấy danh mục", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS(1020, "Danh mục đã tồn tại", HttpStatus.CONFLICT),
    BOOK_COPY_NOT_FOUND(1021, "Không tìm thấy bản sao sách", HttpStatus.NOT_FOUND),
    BORROW_NOT_FOUND(1022, "Không tìm thấy bản ghi mượn sách", HttpStatus.NOT_FOUND),
    BOOK_COPY_ALREADY_BORROWED(1023, "Bản sao sách đã được mượn", HttpStatus.CONFLICT),
    BARCODE_ALREADY_EXISTS(1024, "Mã vạch đã tồn tại", HttpStatus.CONFLICT),
    PUBLISHER_NOT_FOUND(1025, "Không tìm thấy nhà xuất bản", HttpStatus.NOT_FOUND),
    PUBLISHER_ALREADY_EXISTS(1026, "Nhà xuất bản đã tồn tại", HttpStatus.CONFLICT),
    ISBN_ALREADY_EXISTS(1027, "ISBN đã tồn tại", HttpStatus.CONFLICT),
    BOOK_COPY_NOT_AVAILABLE(1028, "Bản sao sách không có sẵn", HttpStatus.CONFLICT),
    BOOK_COPY_NOT_FOUND_OR_NOT_AVAILABLE(1029, "Không tìm thấy hoặc bản sao sách không có sẵn", HttpStatus.NOT_FOUND),
    START_DATE_BEFORE_CURRENT_DATE(1030, "Ngày bắt đầu phải sau ngày hiện tại", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1031, "Token không hợp lệ", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_EXPIRED(1033, "Refresh token hết hạn", HttpStatus.BAD_REQUEST),
    INVALID_REFRESH_TOKEN(1032, "Refresh token không hợp lệ", HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED(1040, "Phương thức HTTP không được phép", HttpStatus.METHOD_NOT_ALLOWED),
    UNSUPPORTED_MEDIA_TYPE(1041, "Content type không được hỗ trợ", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    MISSING_PARAMETER(1042, "Thiếu tham số yêu cầu", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED(1043, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    NOT_FOUND(1044, "Không tìm thấy tài nguyên", HttpStatus.NOT_FOUND),
    INVALID_REQUEST(1045, "Request không hợp lệ", HttpStatus.BAD_REQUEST);


    private final int code;
    private final String message;
    private final HttpStatus status;
}
