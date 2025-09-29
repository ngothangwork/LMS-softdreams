package dev.thangngo.lmssoftdreams.repositories;

import dev.thangngo.lmssoftdreams.dtos.response.book.BookDetailResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookCustomRepository {
    List<BookDetailResponseDTO> searchBooksByName(String name, Pageable pageable);
    long countFilterBooks(String type, String keyword);
    List<BookDetailResponseDTO> filterBooks(String type, String keyword, Pageable pageable);
}
