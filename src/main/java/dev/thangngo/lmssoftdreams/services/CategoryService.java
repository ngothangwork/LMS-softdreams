package dev.thangngo.lmssoftdreams.services;

import dev.thangngo.lmssoftdreams.dtos.request.category.CategoryCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.category.CategoryUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.category.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest);
    CategoryResponse updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest);
    void deleteCategory(Long id);
    CategoryResponse getCategoryById(Long id);
    List<CategoryResponse> getCategoryByNameContaining(String name);
    List<CategoryResponse> getAllCategories();

}
