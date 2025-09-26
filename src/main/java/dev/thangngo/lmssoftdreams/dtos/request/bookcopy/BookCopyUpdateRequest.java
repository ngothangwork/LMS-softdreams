package dev.thangngo.lmssoftdreams.dtos.request.bookcopy;

import dev.thangngo.lmssoftdreams.enums.BookStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCopyUpdateRequest {
    private String barcode;
    private String title;
    private BookStatus status;
}
