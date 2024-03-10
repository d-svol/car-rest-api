package org.example.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.dto.CarDto;
import org.example.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> create(@RequestBody CarDto carDto) {
        try {
            return new ResponseEntity<>(carService.create(carDto), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public Page<CarDto> getAll(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @Max(100) int size,
            @RequestParam(name = "direction", defaultValue = "DESC", required = false) String direction) {
        Sort sort = Sort.by(direction.equalsIgnoreCase("ASC") ? Direction.ASC : Direction.DESC,
                "make", "model", "year", "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        return carService.getAll(pageable);
    }

    @GetMapping("/{objectId}")
    @PreAuthorize("permitAll()")
    public Optional<CarDto> getByObjectId(@PathVariable("objectId") String objectId) {
        return carService.getByObjectId(objectId);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{objectId}")
    public ResponseEntity<CarDto> update(@PathVariable String  objectId, @RequestBody CarDto carDto) {
        try {
            return new ResponseEntity<>(carService.update(objectId, carDto), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{objectId}")
    @PreAuthorize("isAuthenticated()")
    public void deleteByObjectId(@PathVariable("objectId") String objectId) {
        carService.deleteByObjectId(objectId);
    }

    @GetMapping("/makes/{make}")
    @PreAuthorize("permitAll()")
    public Page<CarDto> getByMake(@PathVariable String make,
                                      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                      @RequestParam(name = "size", defaultValue = "10", required = false) @Max(100) int size,
                                      @Parameter(schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"}))
                                      @RequestParam(name = "direction", defaultValue = "ASC", required = false) String direction) {
        Sort sort = Sort.by(direction.equalsIgnoreCase("ASC") ? Direction.ASC : Direction.DESC,
                "make", "id");
        return carService.getByMake(make);
    }

    @GetMapping("/makes/{make}/models/{model}")
    @PreAuthorize("permitAll()")
    public Page<CarDto> getByMakeAndModel(@PathVariable("make") String make,
                                              @PathVariable("model") String model,
                                              @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                              @RequestParam(name = "size", defaultValue = "10", required = false) @Max(100) int size,
                                              @Parameter(schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"}))
                                              @RequestParam(name = "direction", defaultValue = "ASC", required = false) String direction) {
        Sort sort = Sort.by(direction.equalsIgnoreCase("ASC") ? Direction.ASC : Direction.DESC,
                "make", "model", "id");
        return carService.getByMakeAndModel(make, model, PageRequest.of(page, size, sort));
    }

    @GetMapping("/makes/{make}/models/{model}/years/{year}")
    @PreAuthorize("permitAll()")
    public Page<CarDto> getByMakeAndModelAndYear(@PathVariable("make") String make,
                                                     @PathVariable("model") String model,
                                                     @PathVariable Year year,
                                                     @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                     @RequestParam(name = "size", defaultValue = "10", required = false) @Max(100) int size,
                                                     @Parameter(schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"}))
                                                     @RequestParam(name = "direction", defaultValue = "ASC", required = false) String direction) {
        Sort sort = Sort.by(direction.equalsIgnoreCase("ASC") ? Direction.ASC : Direction.DESC,
                "make", "model", "year", "id");
        return carService.getByMakeAndModelAndYear(make, model, year, PageRequest.of(page, size, sort));
    }

    @GetMapping("/makes/{make}/models/{model}/min-years/{minYear}/max-years/{maxYear}")
    @PreAuthorize("permitAll()")
    public Page<CarDto> getByMakeAndModelAndMinYearAndMaxYear(@PathVariable String make,
                                                                  @PathVariable String model,
                                                                  @PathVariable Year minYear,
                                                                  @PathVariable Year maxYear,
                                                                  @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                  @RequestParam(name = "size", defaultValue = "10", required = false) @Max(100) int size,
                                                                  @Parameter(schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"}))
                                                                  @RequestParam(name = "direction", defaultValue = "ASC", required = false) String direction) {
        Sort sort = Sort.by(direction.equalsIgnoreCase("ASC") ? Direction.ASC : Direction.DESC,
                "make", "model", "year", "id");
        return carService.getByMakeAndModelAndMinYearAndMaxYear(make, model, minYear, maxYear, PageRequest.of(page, size, sort));
    }
}
