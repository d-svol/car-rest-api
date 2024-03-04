package org.example.mapper;

import org.example.dto.CarDto;
import org.example.model.Car;
import org.example.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(source = "year", target = "year", qualifiedByName = "mapYearToInt")
    @Mapping(source = "categories", target = "categories", qualifiedByName = "mapCategoriesToCategoryNames")
    CarDto toDto(Car car);

    List<CarDto> toDto(List<Car> cars);

    @Mapping(source = "year", target = "year", qualifiedByName = "mapIntToYear")
    @Mapping(source = "categories", target = "categories", qualifiedByName = "mapCategoryNamesToCategories")
    Car toEntity(CarDto carDto);

    List<Car> toEntity(List<CarDto> carDTOs);

    @Named("mapYearToInt")
    default int mapYearToInt(Year year) {
        return year.getValue();
    }

    @Named("mapIntToYear")
    default Year mapIntToYear(int year) {
        return Year.of(year);
    }

    @Named("mapCategoriesToCategoryNames")
    default List<String> mapCategoriesToCategoryNames(List<Category> categories) {
        return categories.stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    @Named("mapCategoryNamesToCategories")
    default List<Category> mapCategoryNamesToCategories(List<String> categoryNames) {
        return categoryNames.stream()
                .map(name -> {
                    Category category = new Category();
                    category.setName(name);
                    return category;
                })
                .collect(Collectors.toList());
    }

}
