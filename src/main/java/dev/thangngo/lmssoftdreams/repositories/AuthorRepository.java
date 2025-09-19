package dev.thangngo.lmssoftdreams.repositories;

import dev.thangngo.lmssoftdreams.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorService extends JpaRepository<Author, Long> {
}
