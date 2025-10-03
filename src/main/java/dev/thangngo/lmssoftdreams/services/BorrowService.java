package dev.thangngo.lmssoftdreams.services;

import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowSearchRequest;
import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.PageResponse;
import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BorrowService {
    BorrowResponse createBorrow(BorrowCreateRequest request);
    BorrowResponse getBorrowById(Long id);
    BorrowResponse updateBorrow(Long id, BorrowUpdateRequest request);
    BorrowResponse updateBorrowStatus(Long id, String status);
    PageResponse<BorrowResponse> searchBorrow(BorrowSearchRequest request, Pageable pageable);
    List<BorrowResponse> searchBorrows(BorrowSearchRequest request, Pageable pageable);
}
