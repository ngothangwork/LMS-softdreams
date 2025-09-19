package dev.thangngo.lmssoftdreams.repository;

import dev.thangngo.lmssoftdreams.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
