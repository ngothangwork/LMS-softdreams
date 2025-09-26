package dev.thangngo.lmssoftdreams.repositories.impl;

import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;
import dev.thangngo.lmssoftdreams.repositories.BorrowCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BorrowCustomRepositoryImpl implements BorrowCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BorrowResponse> findAllBorrowResponses() {
        return entityManager
                .createNamedQuery("Borrow.findBorrowResponses", BorrowResponse.class)
                .getResultList();
    }
}
