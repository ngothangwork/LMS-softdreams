package dev.thangngo.lmssoftdreams.dtos.response.borrow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowResponse {
    private Long id;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private Long bookId;
    private Long bookCopyId;
    private String barcode;
    private String bookName;
    private UUID userId;
    private String username;
    private String status;
}
