package dev.thangngo.lmssoftdreams.dtos.response.bookcopy;

import dev.thangngo.lmssoftdreams.enums.BookStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookCopyResponse {
    private Long id;
    private String barcode;
    private Long bookId;
    private String bookTitle;
    private BookStatus status;
}
