package dev.thangngo.lmssoftdreams.repositories.impl;

import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;
import dev.thangngo.lmssoftdreams.repositories.BorrowCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BorrowCustomRepositoryImpl implements BorrowCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BorrowResponse> findAllBorrowResponses() {
        return entityManager
                .createNamedQuery("Borrow.findBorrowResponses", BorrowResponse.class)
                .getResultList();
    }

    @Override
    public List<BorrowResponse> searchBorrows(String keyword, Pageable pageable) {
        String sql = """
        SELECT b.id,
               u.username,
               bk.name AS bookName,
               b.borrow_date,
               b.return_date,
               b.status
        FROM borrows b
        JOIN users u ON b.user_id = u.id
        JOIN books bk ON b.book_id = bk.id
        WHERE u.username LIKE :kw
           OR bk.name LIKE :kw
           OR b.status LIKE :kw
        ORDER BY b.id
        OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY
    """;

        Query query = entityManager.createNativeQuery(sql, "BorrowResponseMapping");
        query.setParameter("kw", "%" + (keyword == null || keyword.isEmpty() ? "" : keyword) + "%");
        query.setParameter("offset", (int) pageable.getOffset());
        query.setParameter("limit", pageable.getPageSize());

        return query.getResultList();
    }

    @Override
    public long countSearchBorrows(String keyword) {
        String sql = """
                    SELECT COUNT(*)
                    FROM borrows b
                    JOIN users u ON b.user_id = u.id
                    JOIN books bk ON b.book_id = bk.id
                    WHERE u.username LIKE :kw
                       OR bk.name LIKE :kw
                       OR b.status LIKE :kw
                """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("kw", "%" + (keyword == null || keyword.isEmpty() ? "" : keyword) + "%");

        return ((Number) query.getSingleResult()).longValue();
    }

}
