package dev.thangngo.lmssoftdreams.dtos.request.bookcopy;

import dev.thangngo.lmssoftdreams.enums.BookStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCopyUpdateRequest {
    @NotNull(message = "Status is required")
    private BookStatus status;
}
