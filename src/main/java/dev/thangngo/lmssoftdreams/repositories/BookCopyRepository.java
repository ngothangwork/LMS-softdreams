package dev.thangngo.lmssoftdreams.repositories;

import dev.thangngo.lmssoftdreams.entities.BookCopy;
import dev.thangngo.lmssoftdreams.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    boolean existsByBarcode(String barcode);
    BookStatus getBookCopyStatusByBarcode(String barcode);
}
