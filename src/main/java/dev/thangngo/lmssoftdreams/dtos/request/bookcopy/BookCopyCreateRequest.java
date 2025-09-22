package dev.thangngo.lmssoftdreams.dtos.request.bookcopy;

import dev.thangngo.lmssoftdreams.enums.BookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCopyCreateRequest {
    @NotBlank(message = "Barcode is required")
    private String barcode;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Status is required")
    private BookStatus status;
}
