package dev.thangngo.lmssoftdreams.repositories;

import dev.thangngo.lmssoftdreams.dtos.response.category.CategoryUpdateResponse;
import dev.thangngo.lmssoftdreams.entities.Category;

import java.util.List;

public interface CategoryCustomRepository {
    List<CategoryUpdateResponse> findAllCategoryUpdateResponses();
    List<Category> getAllCategories();

}
