package dev.thangngo.lmssoftdreams.repositories;

import dev.thangngo.lmssoftdreams.entities.BookCopy;
import dev.thangngo.lmssoftdreams.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long>, BookCopyCustomRepository {
    boolean existsByBarcode(String barcode);
    int countByStatusAndBookId(BookStatus bookStatus, Long bookId);
}
