package dev.thangngo.lmssoftdreams.dtos.request.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookCreateRequest {

    @NotBlank(message = "Book name is required")
    @Size(max = 255, message = "Book name must be less than 255 characters")
    private String name;

    private String avatar;

    @NotBlank(message = "ISBN is required")
    @Size(max = 50, message = "ISBN must be less than 50 characters")
    private String isbn;

    @NotNull(message = "Publisher ID is required")
    private Long publisherId;

    @NotEmpty(message = "At least one author is required")
    private Set<Long> authorIds;

    @NotEmpty(message = "At least one category is required")
    private Set<Long> categoryIds;
}
