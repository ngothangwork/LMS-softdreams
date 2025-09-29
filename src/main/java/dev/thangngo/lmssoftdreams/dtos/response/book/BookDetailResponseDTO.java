package dev.thangngo.lmssoftdreams.dtos.response.book;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookDetailResponseDTO {
    private Long id;
    private String name;
    private String isbn;
    private String avatar;
    private String publisherName;
    private String authors;
    private String categories;
    private Integer numberOfBorrowed;
    private Integer numberOfAvailable;

    public BookDetailResponseDTO(Long id, String name, String isbn, String avatar,
                                 String publisherName, String authors, String categories,
                                 Integer numberOfBorrowed, Integer numberOfAvailable) {
        this.id = id;
        this.name = name;
        this.isbn = isbn;
        this.avatar = avatar;
        this.publisherName = publisherName;
        this.authors = authors;
        this.categories = categories;
        this.numberOfBorrowed = numberOfBorrowed;
        this.numberOfAvailable = numberOfAvailable;
    }
}

