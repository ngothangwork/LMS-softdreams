package dev.thangngo.lmssoftdreams.dtos.response.book;

import dev.thangngo.lmssoftdreams.dtos.response.author.AuthorResponse;
import dev.thangngo.lmssoftdreams.dtos.response.category.CategoryResponse;
import dev.thangngo.lmssoftdreams.dtos.response.publisher.PublisherResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookUpdateResponse {
    private Long id;
    private String name;
    private String avatar;
    private String isbn;
    private Long publisherId;
    private Set<Long> authorIds;
    private Set<Long> categoryIds;
}
