package dev.thangngo.lmssoftdreams.repositories;

import dev.thangngo.lmssoftdreams.dtos.response.bookcopy.BookCopyListResponse;

import java.util.List;

public interface BookCopyCustomRepository {
    List<BookCopyListResponse> findAllBookCopyByBookId(Long BookId);
    List<BookCopyListResponse> findAllBookCopyByBookIdAndStatus(Long BookId, String status);

}
