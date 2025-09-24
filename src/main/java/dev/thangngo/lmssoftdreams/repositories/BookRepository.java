package dev.thangngo.lmssoftdreams.repositories;

import dev.thangngo.lmssoftdreams.entities.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"authors", "categories", "publisher"})
    @Query("""
        SELECT b FROM Book b
        WHERE (:name IS NULL OR b.name LIKE LOWER(CONCAT('%', :name, '%')))
        """)
    List<Book> searchByName(@Param("name") String name, Pageable pageable);

    @Query("""
        SELECT COUNT(b) FROM Book b
        WHERE (:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')))
        """)
    long countByName(@Param("name") String name);

    @EntityGraph(attributePaths = {"authors", "categories", "publisher"})
    @Query("""
        SELECT b FROM Book b
        JOIN b.authors a
        WHERE (:author IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :author, '%')))
        """)
    List<Book> searchByAuthor(@Param("author") String author, Pageable pageable);

    @Query("""
        SELECT COUNT(DISTINCT b) FROM Book b
        JOIN b.authors a
        WHERE (:author IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :author, '%')))
        """)
    long countByAuthor(@Param("author") String author);

    @EntityGraph(attributePaths = {"authors", "categories", "publisher"})
    @Query("""
        SELECT b FROM Book b
        JOIN b.categories c
        WHERE (:category IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :category, '%')))
        """)
    List<Book> searchByCategory(@Param("category") String category, Pageable pageable);

    @Query("""
        SELECT COUNT(DISTINCT b) FROM Book b
        JOIN b.categories c
        WHERE (:category IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :category, '%')))
        """)
    long countByCategory(@Param("category") String category);

    @EntityGraph(attributePaths = {"authors", "categories", "publisher"})
    @Query("""
        SELECT b FROM Book b
        JOIN b.publisher p
        WHERE (:publisher IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :publisher, '%')))
        """)
    List<Book> searchByPublisher(@Param("publisher") String publisher, Pageable pageable);

    @Query("""
        SELECT COUNT(DISTINCT b) FROM Book b
        JOIN b.publisher p
        WHERE (:publisher IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :publisher, '%')))
        """)
    long countByPublisher(@Param("publisher") String publisher);

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);
}
