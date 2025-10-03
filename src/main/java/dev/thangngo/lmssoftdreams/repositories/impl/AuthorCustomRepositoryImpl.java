package dev.thangngo.lmssoftdreams.repositories.impl;

import dev.thangngo.lmssoftdreams.dtos.response.author.AuthorUpdateResponse;
import dev.thangngo.lmssoftdreams.repositories.AuthorCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class AuthorCustomRepositoryImpl implements AuthorCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public AuthorCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<AuthorUpdateResponse> findAllAuthorUpdateResponses() {
        String sql = "SELECT a.id, a.name FROM authors a WHERE a.is_active = 1";
        return entityManager.createNativeQuery(sql, "AuthorUpdateMapping").getResultList();
    }
}
