package dev.thangngo.lmssoftdreams.dtos.request.borrow;
import dev.thangngo.lmssoftdreams.enums.BorrowStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowUpdateRequest {
    private Long bookCopyId;
    private BorrowStatus status;
}
