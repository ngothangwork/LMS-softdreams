package dev.thangngo.lmssoftdreams.dtos.request.borrow;
import dev.thangngo.lmssoftdreams.enums.BorrowStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BorrowUpdateRequest {
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private Long bookCopyId;
    private UUID userId;
    private BorrowStatus status;
}
