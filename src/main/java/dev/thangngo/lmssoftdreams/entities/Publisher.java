package dev.thangngo.lmssoftdreams.entities;

import dev.thangngo.lmssoftdreams.dtos.response.publisher.PublisherUpdateResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "publishers")
@Getter
@Setter
@SqlResultSetMapping(
        name = "PublisherUpdateMapping",
        classes = @ConstructorResult(
                targetClass = PublisherUpdateResponse.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                }
        )
)
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150, columnDefinition = "NVARCHAR(MAX)")
    private String name;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String address;
    private String phone;
}

