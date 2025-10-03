package dev.thangngo.lmssoftdreams.repositories;

import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BorrowCustomRepository {
    List<BorrowResponse> searchBorrows(String keyword, Pageable pageable);
    long countSearchBorrows(String keyword);
}
