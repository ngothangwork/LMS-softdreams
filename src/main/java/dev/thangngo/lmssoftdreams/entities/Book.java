package dev.thangngo.lmssoftdreams.entities;

import dev.thangngo.lmssoftdreams.dtos.response.book.BookDetailResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@ToString(exclude = {"publisher", "authors", "categories", "copies"})
@SqlResultSetMapping(
        name = "BookDetailMapping",
        classes = @ConstructorResult(
                targetClass = BookDetailResponseDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "isbn", type = String.class),
                        @ColumnResult(name = "avatar", type = String.class),
                        @ColumnResult(name = "publisherName", type = String.class),
                        @ColumnResult(name = "authors", type = String.class),
                        @ColumnResult(name = "categories", type = String.class),
                        @ColumnResult(name = "numberOfBorrowed", type = Integer.class),
                        @ColumnResult(name = "numberOfAvailable", type = Integer.class)
                }
        )
)

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String isbn;

    private String avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors;

    @ManyToMany
    @JoinTable(
            name = "book_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookCopy> copies;

    private boolean isActive;
}
