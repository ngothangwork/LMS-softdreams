package dev.thangngo.lmssoftdreams.dtos.request.book;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookUpdateRequest {

    @Size(max = 255, message = "Book name must be less than 255 characters")
    private String name;

    private String avatar;

    @Size(max = 50, message = "ISBN must be less than 50 characters")
    private String isbn;

    private Long publisherId;

    private Set<Long> authorIds;

    private Set<Long> categoryIds;
}
