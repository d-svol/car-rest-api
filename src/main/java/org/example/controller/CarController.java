package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.example.dto.CarDto;
import org.example.service.CarService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping
    @Operation(summary = "create", description = "Create car")
    @ApiResponse(responseCode = "201", description = "Car created successfully")
    @ApiResponse(responseCode = "404", description = "Car creation failed")
    public ResponseEntity<Long> create(@RequestBody CarDto carDto) {
        try {
            return new ResponseEntity<>(carService.create(carDto), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @Operation(summary = "search", description = "Endpoint to get all cars")
    public Page<CarDto> getAll(@PageableDefault(sort = {"make", "id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return carService.getAll(pageable);
    }

    @GetMapping("/{objectId}")
    @Operation(summary = "search", description = "Endpoint to get cars by Object Id")
    public ResponseEntity<Optional<CarDto>> getByObjectId(@PathVariable("objectId") String objectId) {
        try {
            return new ResponseEntity<>(carService.getByObjectId(objectId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{objectId}")
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @Operation(summary = "update", description = "Update an existing car")
    public ResponseEntity<CarDto> update(@PathVariable String  objectId, @RequestBody CarDto carDto) {
        try {
            return new ResponseEntity<>(carService.update(objectId, carDto), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{objectId}")
    @Operation(summary = "delete", description = "Delete car by object id")
    public ResponseEntity<String> deleteByObjectId(@PathVariable("objectId") String objectId) {
        try {
            return new ResponseEntity<>(carService.deleteByObjectId(objectId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/makes/{make}")
    @Operation(summary = "search", description = "Endpoint to get cars by make")
    public Page<CarDto> getByMake(@PathVariable String make,
                                  @PageableDefault(sort = {"make", "id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return carService.getByMake(make, pageable);
    }

    @GetMapping("/makes/{make}/models/{model}")
    @Operation(summary = "search", description = "Endpoint to get cars by make and model")
    public Page<CarDto> getByMakeAndModel(
            @PathVariable("make") String make,
            @PathVariable("model") String model,
            @PageableDefault(sort = {"make", "model", "id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return carService.getByMakeAndModel(make, model, pageable);
    }


    @GetMapping("/makes/{make}/models/{model}/years/{year}")
    @Operation(summary = "search", description = "Endpoint to get cars by make, model, and year")
    public Page<CarDto> getByMakeAndModelAndYear(@PathVariable("make") String make,
                                                 @PathVariable("model") String model,
                                                 @PathVariable Year year,
                                                 @PageableDefault(sort = {"make", "model", "year", "id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return carService.getByMakeAndModelAndYear(make, model, year, pageable);
    }

    @GetMapping("/makes/{make}/models/{model}/min-years/{minYear}/max-years/{maxYear}")
    @Operation(summary = "search", description = "Endpoint to get cars by make, model, min year, and max year")
    public Page<CarDto> getByMakeAndModelAndMinYearAndMaxYear(@PathVariable("make") String make,
                                                              @PathVariable("model") String model,
                                                              @PathVariable Year minYear,
                                                              @PathVariable Year maxYear,
                                                              @PageableDefault(sort = {"make", "model", "year", "id"},
                                                                      direction = Sort.Direction.ASC) Pageable pageable) {
        return carService.getByMakeAndModelAndMinYearAndMaxYear(make, model, minYear, maxYear, pageable);
    }
}