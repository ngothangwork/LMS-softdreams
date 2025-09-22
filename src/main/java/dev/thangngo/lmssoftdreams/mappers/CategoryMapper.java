package dev.thangngo.lmssoftdreams.mappers;

import dev.thangngo.lmssoftdreams.dtos.request.category.CategoryCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.category.CategoryUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.category.CategoryResponse;
import dev.thangngo.lmssoftdreams.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);

    Category toCategory(CategoryCreateRequest categoryCreateRequest);

    void updateCategoryFromDto(CategoryUpdateRequest dto, @MappingTarget Category entity);
}
