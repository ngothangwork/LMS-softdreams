package dev.thangngo.lmssoftdreams.services.impl;

import dev.thangngo.lmssoftdreams.dtos.request.category.CategoryCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.category.CategoryUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.category.CategoryResponse;
import dev.thangngo.lmssoftdreams.entities.Category;
import dev.thangngo.lmssoftdreams.enums.ErrorCode;
import dev.thangngo.lmssoftdreams.exceptions.AppException;
import dev.thangngo.lmssoftdreams.mappers.CategoryMapper;
import dev.thangngo.lmssoftdreams.repositories.categories.CategoryRepository;
import dev.thangngo.lmssoftdreams.services.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("categoryService")
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        Category category = categoryRepository.findByName(categoryCreateRequest.getName()).orElse(null);
        if (category != null) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        category = categoryMapper.toCategory(categoryCreateRequest);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if (categoryRepository.findByName(categoryUpdateRequest.getName())
                .filter(c -> !c.getId().equals(id))
                .isPresent()) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        categoryMapper.updateCategoryFromDto(categoryUpdateRequest, category);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toCategoryResponse)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public List<CategoryResponse> getCategoryByNameContaining(String name) {
        return categoryRepository.findByNameContaining(name)
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryResponse).toList();
    }
}
