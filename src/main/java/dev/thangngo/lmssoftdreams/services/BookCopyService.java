package dev.thangngo.lmssoftdreams.services;

import dev.thangngo.lmssoftdreams.dtos.request.bookcopy.BookCopyCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.bookcopy.BookCopyUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.bookcopy.BookCopyListResponse;
import dev.thangngo.lmssoftdreams.dtos.response.bookcopy.BookCopyResponse;

import java.util.List;

public interface BookCopyService {
    BookCopyResponse createBookCopy(BookCopyCreateRequest request);
    BookCopyResponse updateBookCopy(Long id, BookCopyUpdateRequest request);
    void deleteBookCopy(Long id);
    List<BookCopyResponse> getAllBookCopies();
    BookCopyResponse getBookCopyById(Long id);
    List<BookCopyListResponse> getListBookCopyResponse(Long bookId);
    List<BookCopyListResponse> getListBookCopyResponseByStatus(String status, Long bookId);
}
