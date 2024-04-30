package org.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "car")
@Schema(description = "Details about a car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The private identifier of the car.", example = "1")
    private Long id;

    @Column(name = "object_id")
    @NotBlank
    @Schema(description = "The object identifier of the car.", example = "a1B2c3D4")
    private String objectId;

    @Column(name = "make", nullable = false)
    @Schema(description = "The make of the car", example = "Toyota")
    private String make;

    @Column(name = "year", nullable = false)
    @Schema(description = "The year of manufacture of the car", example = "2022")
    private Year year;

    @Column(name = "model", nullable = false)
    @Schema(description = "The model of the car", example = "Camry")
    private String model;

    @ManyToMany(fetch = FetchType.EAGER, cascade =
            {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "car_category",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @Schema(description = "The categories associated with the car.", example = "[\"Sedan\"], [\"Pickup\"]")
    private List<Category> categories = new ArrayList<>();
}
