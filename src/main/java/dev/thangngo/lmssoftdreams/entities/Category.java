package dev.thangngo.lmssoftdreams.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(MAX)")
    private String name;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String description;
}

