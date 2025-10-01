package dev.thangngo.lmssoftdreams.dtos.request.bookcopy;

import dev.thangngo.lmssoftdreams.enums.BookStatus;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCopyUpdateRequest {
    @Size(max = 50, message = "Barcode must be less than 50 characters")
    private String barcode;

    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    private BookStatus status;
}
