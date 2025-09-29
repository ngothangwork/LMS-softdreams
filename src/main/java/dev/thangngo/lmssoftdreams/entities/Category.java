package dev.thangngo.lmssoftdreams.entities;

import dev.thangngo.lmssoftdreams.dtos.response.category.CategoryUpdateResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
@SqlResultSetMapping(
        name = "CategoryUpdateMapping",
        classes = @ConstructorResult(
                targetClass = CategoryUpdateResponse.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                }
        )
)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(MAX)")
    private String name;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String description;
}

