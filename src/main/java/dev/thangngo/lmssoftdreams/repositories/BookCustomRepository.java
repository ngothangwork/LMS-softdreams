package dev.thangngo.lmssoftdreams.repositories;

import dev.thangngo.lmssoftdreams.entities.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookCustomRepository {
    List<Book> searchByName(String name, Pageable pageable);
    long countByName(String name);

    List<Book> searchByAuthor(String author, Pageable pageable);
    long countByAuthor(String author);

    List<Book> searchByCategory(String category, Pageable pageable);
    long countByCategory(String category);

    List<Book> searchByPublisher(String publisher, Pageable pageable);
    long countByPublisher(String publisher);
}
