package dev.thangngo.lmssoftdreams.repositories;

import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;

import java.awt.print.Pageable;
import java.util.List;

public interface BorrowCustomRepository {
    List<BorrowResponse> findAllBorrowResponses();
    List<BorrowResponse> filterBorrowResponses(String keyword, Pageable pageable);
    long countFilterBorrowResponses(String keyword);
}
