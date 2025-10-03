package dev.thangngo.lmssoftdreams.repositories.impl;

import dev.thangngo.lmssoftdreams.dtos.response.category.CategoryUpdateResponse;
import dev.thangngo.lmssoftdreams.entities.Category;
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
        String sql = "SELECT c.id, c.name FROM categories c WHERE c.is_active = 1";
        return entityManager.createNativeQuery(sql, "CategoryUpdateMapping").getResultList();
    }

    @Override
    public List<Category> getAllCategories() {
        StringBuilder sql = new StringBuilder("SELECT * FROM categories WHERE is_active = 1");
        return entityManager.createNativeQuery(sql.toString(), Category.class).getResultList();
    }
}
