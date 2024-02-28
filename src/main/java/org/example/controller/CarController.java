package org.example.controller;

import org.example.model.Car;
import org.example.service.CarService;
import org.example.service.CategoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static org.springframework.data.domain.Sort.Direction;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService, CategoryService categoryService) {
        this.carService = carService;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    List<Car> getAll() {
        return carService.getAll();
    }


    @GetMapping("object-id/{objectId}")
    @PreAuthorize("permitAll()")
    Optional<Car> getCarByObjectId(@PathVariable String objectId) {
        return carService.getByObjectId(objectId);
    }

    @GetMapping("/brand/{brand}")
    @PreAuthorize("permitAll()")
    List<Car> getCarsByBrand(@PathVariable String brand) {
        return carService.getByBrand(brand);
    }

    @GetMapping("/brand/{brand}/model/{model}")
    @PreAuthorize("permitAll()")
    List<Car> getCarsByBrandAndModel(@PathVariable String brand,
                                     @PathVariable String model,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "asc") String sortOrder) {
        Sort sort = Sort.by(sortOrder.equals("desc") ? Direction.DESC : Direction.ASC, "year", "brand", "model");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return carService.getByBrandAndModel(brand, model, pageRequest);
    }

    @GetMapping("/brand/{brand}/model/{model}/year/{year}")
    @PreAuthorize("permitAll()")
    List<Car> getCarsByBrandAndModelAndYear(@PathVariable String brand,
                                            @PathVariable String model,
                                            @PathVariable Year year) {
        return carService.getByBrandAndModelAndYear(brand, model, year);
    }

    @GetMapping("/brand/{brand}/model/{model}/min-year/{minYear}/max-year/{maxYear}")
    @PreAuthorize("permitAll()")
    List<Car> getCarsByBrandAndModelAndMinYearAndMaxYear(@PathVariable String brand,
                                                         @PathVariable String model,
                                                         @PathVariable Year minYear,
                                                         @PathVariable Year maxYear) {
        return carService.getByBrandAndModelAndMinYearAndMaxYear(brand, model, minYear, maxYear);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    Car add(@RequestParam String brand,
            @RequestParam String model,
            @RequestParam Year year,
            @RequestParam(name = "categories") String[] categoriesNames) {
        return carService.addCar(brand, model, year, categoriesNames);
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    void delete(@RequestParam("object-id") String objectId) {
        carService.deleteByObjectId(objectId);
    }

}
