package dev.thangngo.lmssoftdreams.entities;

import dev.thangngo.lmssoftdreams.dtos.response.author.AuthorUpdateResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "authors")
@Getter
@Setter
@SqlResultSetMapping(
        name = "AuthorUpdateMapping",
        classes = @ConstructorResult(
                targetClass = AuthorUpdateResponse.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                }
        )
)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100 , columnDefinition = "NVARCHAR(MAX)")
    private String name;

    @Column(nullable = false, length = 100)
    private LocalDate dob;

    @Column(nullable = false, length = 100, columnDefinition = "NVARCHAR(MAX)")
    private String nationality;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(nullable = false)
    private Boolean isActive = true;
}

