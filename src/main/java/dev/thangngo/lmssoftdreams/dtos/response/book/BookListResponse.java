package dev.thangngo.lmssoftdreams.dtos.response.book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookListResponse {
    private Long id;
    private String avatar;
    private String name;
    private String isbn;
}
