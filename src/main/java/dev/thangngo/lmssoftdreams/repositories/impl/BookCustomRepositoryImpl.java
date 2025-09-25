package dev.thangngo.lmssoftdreams.repositories.impl;

import dev.thangngo.lmssoftdreams.entities.Book;
import dev.thangngo.lmssoftdreams.repositories.BookCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    public List<Book> searchByName(String name, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("""
            SELECT DISTINCT b FROM Book b
            LEFT JOIN FETCH b.authors
            LEFT JOIN FETCH b.categories
            LEFT JOIN FETCH b.publisher
            WHERE 1=1
        """);

        if (name != null && !name.isBlank()) {
            jpql.append(" AND b.name LIKE :name");
        }

        var query = entityManager.createQuery(jpql.toString(), Book.class);

        if (name != null && !name.isBlank()) {
            query.setParameter("name", "%" + name.toLowerCase() + "%");
        }

        return query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public long countByName(String name) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(b) FROM Book b WHERE 1=1");

        if (name != null && !name.isBlank()) {
            jpql.append(" AND b.name LIKE :name");
        }

        var query = entityManager.createQuery(jpql.toString(), Long.class);

        if (name != null && !name.isBlank()) {
            query.setParameter("name", "%" + name.toLowerCase() + "%");
        }

        return query.getSingleResult();
    }

    @Override
    public List<Book> searchByAuthor(String author, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("""
            SELECT DISTINCT b FROM Book b
            JOIN b.authors a
            LEFT JOIN FETCH b.categories
            LEFT JOIN FETCH b.publisher
            WHERE 1=1
        """);

        if (author != null && !author.isBlank()) {
            jpql.append(" AND a.name LIKE :author");
        }

        var query = entityManager.createQuery(jpql.toString(), Book.class);

        if (author != null && !author.isBlank()) {
            query.setParameter("author", "%" + author.toLowerCase() + "%");
        }

        return query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public long countByAuthor(String author) {
        StringBuilder jpql = new StringBuilder("""
            SELECT COUNT(DISTINCT b) FROM Book b
            JOIN b.authors a
            WHERE 1=1
        """);

        if (author != null && !author.isBlank()) {
            jpql.append(" AND a.name LIKE :author");
        }

        var query = entityManager.createQuery(jpql.toString(), Long.class);

        if (author != null && !author.isBlank()) {
            query.setParameter("author", "%" + author.toLowerCase() + "%");
        }

        return query.getSingleResult();
    }

    @Override
    public List<Book> searchByCategory(String category, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("""
            SELECT DISTINCT b FROM Book b
            JOIN b.categories c
            LEFT JOIN FETCH b.authors
            LEFT JOIN FETCH b.publisher
            WHERE 1=1
        """);

        if (category != null && !category.isBlank()) {
            jpql.append(" AND c.name LIKE :category");
        }

        var query = entityManager.createQuery(jpql.toString(), Book.class);

        if (category != null && !category.isBlank()) {
            query.setParameter("category", "%" + category.toLowerCase() + "%");
        }

        return query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public long countByCategory(String category) {
        StringBuilder jpql = new StringBuilder("""
            SELECT COUNT(DISTINCT b) FROM Book b
            JOIN b.categories c
            WHERE 1=1
        """);

        if (category != null && !category.isBlank()) {
            jpql.append(" AND c.name LIKE :category");
        }

        var query = entityManager.createQuery(jpql.toString(), Long.class);

        if (category != null && !category.isBlank()) {
            query.setParameter("category", "%" + category.toLowerCase() + "%");
        }

        return query.getSingleResult();
    }

    @Override
    public List<Book> searchByPublisher(String publisher, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("""
            SELECT DISTINCT b FROM Book b
            JOIN b.publisher p
            LEFT JOIN FETCH b.authors
            LEFT JOIN FETCH b.categories
            WHERE 1=1
        """);

        if (publisher != null && !publisher.isBlank()) {
            jpql.append(" AND p.name LIKE :publisher");
        }

        var query = entityManager.createQuery(jpql.toString(), Book.class);

        if (publisher != null && !publisher.isBlank()) {
            query.setParameter("publisher", "%" + publisher.toLowerCase() + "%");
        }

        return query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public long countByPublisher(String publisher) {
        StringBuilder jpql = new StringBuilder("""
            SELECT COUNT(DISTINCT b) FROM Book b
            JOIN b.publisher p
            WHERE 1=1
        """);

        if (publisher != null && !publisher.isBlank()) {
            jpql.append(" AND p.name LIKE :publisher");
        }

        var query = entityManager.createQuery(jpql.toString(), Long.class);

        if (publisher != null && !publisher.isBlank()) {
            query.setParameter("publisher", "%" + publisher.toLowerCase() + "%");
        }

        return query.getSingleResult();
    }
}
