package dev.thangngo.lmssoftdreams.repositories.impl;

import dev.thangngo.lmssoftdreams.dtos.response.category.CategoryUpdateResponse;
import dev.thangngo.lmssoftdreams.repositories.CategoryCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public CategoryCustomRepositoryImpl( EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<CategoryUpdateResponse> findAllCategoryUpdateResponses() {
        String sql = "SELECT c.id, c.name FROM categories c";
        return entityManager.createNativeQuery(sql, "CategoryUpdateMapping").getResultList();
    }
}
