package dev.thangngo.lmssoftdreams.controllers;

import dev.thangngo.lmssoftdreams.dtos.request.category.CategoryCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.category.CategoryUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.ApiResponse;
import dev.thangngo.lmssoftdreams.dtos.response.category.CategoryResponse;
import dev.thangngo.lmssoftdreams.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categoryResponseList = categoryService.getAllCategories();
        return ResponseEntity.ok(
                ApiResponse.<List<CategoryResponse>>builder()
                        .message("Get all categories")
                        .code(200)
                        .success(true)
                        .result(categoryResponseList)
                        .build()
        );
    }

    @GetMapping("name/{name}")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategoryByName(@PathVariable String name) {
        List<CategoryResponse> categoryResponseList;
        if( name == null || name.isEmpty() ){
            categoryResponseList = categoryService.getAllCategories();
        }else{
            categoryResponseList = categoryService.getCategoryByNameContaining(name);
        }
        return ResponseEntity.ok(
                ApiResponse.<List<CategoryResponse>>builder()
                        .message("Get list category contain string " + name)
                        .code(200)
                        .success(true)
                        .result(categoryResponseList)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        CategoryResponse categoryResponse = categoryService.createCategory(categoryCreateRequest);
        return ResponseEntity.status(201).body(ApiResponse.<CategoryResponse>builder()
                .message("Create Category Successfully")
                .code(201)
                .success(true)
                .result(categoryResponse)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@Valid @RequestBody CategoryUpdateRequest categoryUpdateRequest, @PathVariable Long id) {
        CategoryResponse categoryResponse = categoryService.updateCategory(id, categoryUpdateRequest);
        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder()
                .message("Update Category Successfully")
                .code(200)
                .success(true)
                .result(categoryResponse)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("Delete Category Successfully")
                .code(200)
                .success(true)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById (@PathVariable Long id) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder()
                .message("Get Category Successfully")
                .code(200)
                .success(true)
                .result(categoryResponse)
                .build());
    }
}
