package dev.thangngo.lmssoftdreams.services;

import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;

import java.util.List;

public interface BorrowService {
    BorrowResponse createBorrow(BorrowCreateRequest request);
    BorrowResponse getBorrowById(Long id);
    BorrowResponse updateBorrow(Long id, BorrowUpdateRequest request);
    List<BorrowResponse> getAllBorrows();
}
