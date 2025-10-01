package dev.thangngo.lmssoftdreams.repositories.impl;

import dev.thangngo.lmssoftdreams.dtos.response.bookcopy.BookCopyListResponse;
import dev.thangngo.lmssoftdreams.repositories.BookCopyCustomRepository;
import dev.thangngo.lmssoftdreams.repositories.BookCopyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookCopyCustomRepositoryImpl implements BookCopyCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public BookCopyCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<BookCopyListResponse> findAllBookCopyByBookId(Long bookId) {
        String sql = """
                    SELECT bc.id, bc.barcode, bc.status
                    FROM book_copies bc
                    WHERE bc.book_id = ?1
                """;

        return entityManager.createNativeQuery(sql, "BookCopyMapping")
                .setParameter(1, bookId)
                .getResultList();
    }

    @Override
    public List<BookCopyListResponse> findAllBookCopyByBookIdAndStatus(Long bookId, String status) {
        String sql = """
            SELECT bc.id, bc.barcode, bc.status
            FROM book_copies bc
            WHERE bc.book_id = ?1 AND bc.status = ?2
            """;

        return entityManager.createNativeQuery(sql, "BookCopyMapping")
                .setParameter(1, bookId)
                .setParameter(2, status)
                .getResultList();
    }



}
