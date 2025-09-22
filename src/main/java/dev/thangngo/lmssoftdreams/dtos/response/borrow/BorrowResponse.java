package dev.thangngo.lmssoftdreams.dtos.response.borrow;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BorrowResponse {
    private Long id;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private Long bookCopyId;
    private UUID userId;
    private String status;
}
