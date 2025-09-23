package dev.thangngo.lmssoftdreams.dtos.request.book;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSearchRequest {

    @NotNull(message = "Type is required")
    private String type;
    private String keyWord;
}
