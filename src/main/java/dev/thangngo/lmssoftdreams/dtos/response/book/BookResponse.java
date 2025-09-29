package dev.thangngo.lmssoftdreams.dtos.response.book;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BookResponse implements Serializable{
    private Long id;
    private String name;
    private String avatar;
    private String isbn;
}
