package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
    @Schema(description = "The object identifier of the car.", example = "a1B2c3D4")
    private String objectId;
    @Schema(description = "The make of the car", example = "Toyota")
    private String make;
    @Schema(description = "The year of manufacture of the car", example = "2022")
    private int year;
    @Schema(description = "The model of the car", example = "Camry")
    private String model;
    @Schema(description = "The categories associated with the car.", example = "[\"Sedan\"], [\"Pickup\"]")
    private List<String> categories;
}
