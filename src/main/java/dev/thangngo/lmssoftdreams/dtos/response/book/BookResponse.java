package dev.thangngo.lmssoftdreams.dtos.response.book;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookResponse {
    private Long id;
    private String name;
    private String avatar;
    private String isbn;
    private Long publisherId;
    private Set<Long> authorIds;
    private Set<Long> categoryIds;
}
