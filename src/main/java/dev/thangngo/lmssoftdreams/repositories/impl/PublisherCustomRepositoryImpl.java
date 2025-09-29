package dev.thangngo.lmssoftdreams.repositories.impl;

import dev.thangngo.lmssoftdreams.dtos.response.publisher.PublisherUpdateResponse;
import dev.thangngo.lmssoftdreams.repositories.PublisherCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PublisherCustomRepositoryImpl implements PublisherCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public  PublisherCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<PublisherUpdateResponse> findAllPublisherUpdateResponses() {
        String sql = "SELECT p.id, p.name FROM publishers p";
        return entityManager.createNativeQuery(sql, "PublisherUpdateMapping").getResultList();
    }
}
