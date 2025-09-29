package dev.thangngo.lmssoftdreams.repositories;

import dev.thangngo.lmssoftdreams.dtos.response.category.CategoryUpdateResponse;

import java.util.List;

public interface CategoryCustomRepository {
    List<CategoryUpdateResponse> findAllCategoryUpdateResponses();
}
