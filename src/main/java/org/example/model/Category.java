package org.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The private identifier of the car.", example = "1")
    private Long id;

    @Column(name = "name")
    @Schema(description = "The categories associated with the car.", example = "Sedan")
    private String name;
}
