package dev.thangngo.lmssoftdreams.dtos.request.category;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateRequest {
    @Size(min = 4, max = 100, message = "Category name must be between 4 and 100 characters")
    private String name;

    private String description;
}
