package dev.thangngo.lmssoftdreams.entities;

import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;
import dev.thangngo.lmssoftdreams.enums.BorrowStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "borrows")
@Getter
@Setter
@SqlResultSetMapping(
        name = "BorrowResponseMapping",
        classes = @ConstructorResult(
                targetClass = BorrowResponse.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "borrowDate", type = LocalDate.class),
                        @ColumnResult(name = "returnDate", type = LocalDate.class),
                        @ColumnResult(name = "bookId", type = Long.class),
                        @ColumnResult(name = "barcode", type = String.class),
                        @ColumnResult(name = "bookName", type = String.class),
                        @ColumnResult(name = "userId", type = java.util.UUID.class),
                        @ColumnResult(name = "username", type = String.class),
                        @ColumnResult(name = "status", type = String.class)
                }
        )
)

@NamedNativeQuery(
        name = "Borrow.findBorrowResponses",
        query = """
                    SELECT
                        b.id,
                        b.borrow_date AS borrowDate,
                        b.return_date AS returnDate,
                        b.book_id AS bookId,
                        bc.id AS bookCopyId,
                        bc.barcode AS barcode,
                        bk.name AS bookName,
                        u.id AS userId,
                        u.username,
                        b.status
                    FROM borrows b
                    JOIN books bk ON b.book_id = bk.id
                    JOIN users u ON b.user_id = u.id
                    LEFT JOIN book_copies bc ON b.book_copy_id = bc.id
                    ORDER BY b.borrow_date DESC;
                """,
        resultSetMapping = "BorrowResponseMapping"
)


public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate borrowDate;
    private LocalDate returnDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_copy_id")
    private BookCopy bookCopy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BorrowStatus status;
}

