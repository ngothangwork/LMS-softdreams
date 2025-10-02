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
        StringBuilder sql = new StringBuilder("""
                    SELECT b.id,
                           b.borrow_date AS borrowDate,
                           b.return_date AS returnDate,
                           b.book_id AS bookId,
                           bc.barcode AS barcode,
                           bk.name AS bookName,
                           u.id AS userId,
                           u.username,
                           b.status
                    FROM borrows b
                    JOIN users u ON b.user_id = u.id
                    JOIN books bk ON b.book_id = bk.id
                    LEFT JOIN book_copies bc ON b.book_copy_id = bc.id
                    WHERE u.username LIKE :keyword
                       OR bk.name LIKE :keyword
                       OR b.status LIKE :keyword
                """);
        if (pageable.getSort().isSorted()) {
            sql.append(" ORDER BY ");
            pageable.getSort().forEach(order -> {
                String property = order.getProperty();
                String direction = order.getDirection().name();
                switch (property) {
                    case "borrowDate" -> sql.append("b.borrow_date ").append(direction);
                    case "returnDate" -> sql.append("b.return_date ").append(direction);
                    case "bookName" -> sql.append("bk.name ").append(direction);
                    case "username" -> sql.append("u.username ").append(direction);
                    default -> sql.append("b.id ").append(direction);
                }
            });
        } else {
            sql.append(" ORDER BY b.id ASC");
        }

        sql.append(" OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY");

        Query query = entityManager.createNativeQuery(sql.toString(), "BorrowResponseMapping");
        query.setParameter("keyword", "%" + (keyword == null || keyword.isEmpty() ? "" : keyword) + "%");
        query.setParameter("offset", (int) pageable.getOffset());
        query.setParameter("limit", pageable.getPageSize());

        return query.getResultList();
    }


    @Override
    public long countSearchBorrows(String keyword) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ")
                .append("FROM borrows b ")
                .append("JOIN users u ON b.user_id = u.id ")
                .append("JOIN books bk ON b.book_id = bk.id ")
                .append("WHERE u.username LIKE :keyword ")
                .append("OR bk.name LIKE :keyword ")
                .append("OR b.status LIKE :keyword");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("keyword", "%" + (keyword == null || keyword.isEmpty() ? "" : keyword) + "%");

        return ((Number) query.getSingleResult()).longValue();
    }

}
