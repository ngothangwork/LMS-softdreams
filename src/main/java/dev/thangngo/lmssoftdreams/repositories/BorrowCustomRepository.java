package dev.thangngo.lmssoftdreams.repositories;

import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;

import java.util.List;

public interface BorrowCustomRepository {
    public List<BorrowResponse> findAllBorrowResponses();
}
