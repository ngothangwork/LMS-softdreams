package dev.thangngo.lmssoftdreams.services;

import dev.thangngo.lmssoftdreams.dtos.request.book.BookCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.book.BookSearchRequest;
import dev.thangngo.lmssoftdreams.dtos.request.book.BookUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.PageResponse;
import dev.thangngo.lmssoftdreams.dtos.response.book.BookDetailResponse;
import dev.thangngo.lmssoftdreams.dtos.response.book.BookDetailResponseDTO;
import dev.thangngo.lmssoftdreams.dtos.response.book.BookResponse;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BookService {
    BookResponse createBook(BookCreateRequest request);
    BookResponse updateBook(Long id, BookUpdateRequest request);
    void deleteBook(Long id);
    BookDetailResponseDTO getBookById(Long id);
    List<BookResponse> getAllBooks();
    List<BookResponse> getBooksByName(String name);
    BookUpdateRequest getBookUpdateById(Long id);
    PageResponse<BookResponse> filterBooks(BookSearchRequest request, Pageable pageable);
    List<BookDetailResponseDTO> searchBooks(String name, Pageable pageable);
}
