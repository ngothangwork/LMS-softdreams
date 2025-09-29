package dev.thangngo.lmssoftdreams.repositories.impl;

import dev.thangngo.lmssoftdreams.dtos.response.book.BookDetailResponseDTO;
import dev.thangngo.lmssoftdreams.repositories.BookCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class BookCustomRepositoryImpl implements BookCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;



    @Override
    public List<BookDetailResponseDTO> searchBooksByName(String name, Pageable pageable) {
        String baseSql = """
        SELECT b.id,
               b.name,
               b.isbn,
               b.avatar,
               p.name AS publisherName,
               authors.authors,
               categories.categories,
               ISNULL(borrowed.count_borrowed, 0) AS numberOfBorrowed,
               ISNULL(available.count_available, 0) AS numberOfAvailable
        FROM books b
        LEFT JOIN publishers p ON b.publisher_id = p.id
        CROSS APPLY (
            SELECT STRING_AGG(a.name, ', ') AS authors
            FROM book_authors ba
            JOIN authors a ON ba.author_id = a.id
            WHERE ba.book_id = b.id
        ) authors
        CROSS APPLY (
            SELECT STRING_AGG(c.name, ', ') AS categories
            FROM book_categories bc
            JOIN categories c ON bc.category_id = c.id
            WHERE bc.book_id = b.id
        ) categories
        LEFT JOIN (
            SELECT book_id, COUNT(*) AS count_borrowed
            FROM book_copies
            WHERE status = 'BORROWED'
            GROUP BY book_id
        ) borrowed ON borrowed.book_id = b.id
        LEFT JOIN (
            SELECT book_id, COUNT(*) AS count_available
            FROM book_copies
            WHERE status = 'AVAILABLE'
            GROUP BY book_id
        ) available ON available.book_id = b.id
        WHERE b.name LIKE :name
        ORDER BY %s
        OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY
    """;

        String orderByColumn = "b.name";
        boolean asc = true;
        if (pageable != null && pageable.getSort() != null) {
            for (org.springframework.data.domain.Sort.Order order : pageable.getSort()) {
                if ("name".equalsIgnoreCase(order.getProperty())) {
                    asc = order.isAscending();
                    break;
                }
            }
        }

        String orderClause = orderByColumn + (asc ? " ASC" : " DESC");
        String finalSql = String.format(baseSql, orderClause);

        Query query = entityManager.createNativeQuery(finalSql, "BookDetailMapping");
        query.setParameter("name", "%" + name + "%");
        query.setParameter("offset", pageable.getOffset());
        query.setParameter("limit", pageable.getPageSize());

        @SuppressWarnings("unchecked")
        List<BookDetailResponseDTO> results = query.getResultList();
        return results;
    }

    @Override
    public List<BookDetailResponseDTO> filterBooks(String type, String keyword, Pageable pageable) {
        String baseSql = """
        SELECT b.id,
               b.name,
               b.isbn,
               b.avatar,
               p.name AS publisherName,
               authors.authors,
               categories.categories,
               ISNULL(borrowed.count_borrowed, 0) AS numberOfBorrowed,
               ISNULL(available.count_available, 0) AS numberOfAvailable
        FROM books b
        LEFT JOIN publishers p ON b.publisher_id = p.id
        CROSS APPLY (
            SELECT STRING_AGG(a.name, ', ') AS authors
            FROM book_authors ba
            JOIN authors a ON ba.author_id = a.id
            WHERE ba.book_id = b.id
        ) authors
        CROSS APPLY (
            SELECT STRING_AGG(c.name, ', ') AS categories
            FROM book_categories bc
            JOIN categories c ON bc.category_id = c.id
            WHERE bc.book_id = b.id
        ) categories
        LEFT JOIN (
            SELECT book_id, COUNT(*) AS count_borrowed
            FROM book_copies
            WHERE status = 'BORROWED'
            GROUP BY book_id
        ) borrowed ON borrowed.book_id = b.id
        LEFT JOIN (
            SELECT book_id, COUNT(*) AS count_available
            FROM book_copies
            WHERE status = 'AVAILABLE'
            GROUP BY book_id
        ) available ON available.book_id = b.id
        WHERE %s LIKE :keyword
        ORDER BY %s
        OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY
    """;
        String filterColumn;
        switch (type.toLowerCase()) {
            case "name" -> filterColumn = "b.name";
            case "author" -> filterColumn = "(SELECT STRING_AGG(a.name, ', ') FROM book_authors ba JOIN authors a ON ba.author_id = a.id WHERE ba.book_id = b.id)";
            case "category" -> filterColumn = "(SELECT STRING_AGG(c.name, ', ') FROM book_categories bc JOIN categories c ON bc.category_id = c.id WHERE bc.book_id = b.id)";
            case "publisher" -> filterColumn = "p.name";
            default -> throw new IllegalArgumentException("Invalid search type: " + type);
        }

        String orderByColumn = "b.name";
        boolean asc = true;
        if (pageable != null && pageable.getSort() != null) {
            for (org.springframework.data.domain.Sort.Order order : pageable.getSort()) {
                orderByColumn = switch (order.getProperty().toLowerCase()) {
                    case "name" -> "b.name";
                    case "isbn" -> "b.isbn";
                    case "publisher" -> "p.name";
                    case "author" -> "authors.authors";
                    case "category" -> "categories.categories";
                    default -> "b.name";
                };
                asc = order.isAscending();
                break;
            }
        }
        String orderClause = orderByColumn + (asc ? " ASC" : " DESC");
        String finalSql = String.format(baseSql, filterColumn, orderClause);

        Query query = entityManager.createNativeQuery(finalSql, "BookDetailMapping");
        query.setParameter("keyword", "%" + keyword + "%");
        query.setParameter("offset", pageable.getOffset());
        query.setParameter("limit", pageable.getPageSize());

        @SuppressWarnings("unchecked")
        List<BookDetailResponseDTO> results = query.getResultList();
        return results;
    }


    @Override
    public long countFilterBooks(String type, String keyword) {
        String countSql = """
        SELECT COUNT(*)
        FROM books b
        LEFT JOIN publishers p ON b.publisher_id = p.id
        WHERE %s LIKE :keyword
    """;

        String filterColumn;
        switch (type.toLowerCase()) {
            case "name" -> filterColumn = "b.name";
            case "author" -> filterColumn = "(SELECT STRING_AGG(a.name, ', ') FROM book_authors ba JOIN authors a ON ba.author_id = a.id WHERE ba.book_id = b.id)";
            case "category" -> filterColumn = "(SELECT STRING_AGG(c.name, ', ') FROM book_categories bc JOIN categories c ON bc.category_id = c.id WHERE bc.book_id = b.id)";
            case "publisher" -> filterColumn = "p.name";
            default -> throw new IllegalArgumentException("Invalid search type: " + type);
        }

        String finalSql = String.format(countSql, filterColumn);

        Query query = entityManager.createNativeQuery(finalSql);
        query.setParameter("keyword", "%" + keyword + "%");

        return ((Number) query.getSingleResult()).longValue();
    }





}
