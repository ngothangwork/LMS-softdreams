package dev.thangngo.lmssoftdreams.entities;

import dev.thangngo.lmssoftdreams.dtos.response.bookcopy.BookCopyListResponse;
import dev.thangngo.lmssoftdreams.enums.BookStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book_copies")
@Getter
@Setter

@SqlResultSetMapping(
        name = "BookCopyMapping",
        classes = @ConstructorResult(
                targetClass = BookCopyListResponse.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "barcode", type = String.class),
                        @ColumnResult(name = "status", type = String.class),
                }
        )
)
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String barcode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookStatus status;
}
