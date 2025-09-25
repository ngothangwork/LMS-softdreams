package dev.thangngo.lmssoftdreams.repositories.authors;

import dev.thangngo.lmssoftdreams.entities.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByNameContaining(String keyword, Pageable pageable);
}
