package org.example.service;

import org.example.model.Car;
import org.example.model.Category;
import org.example.repository.CarRepository;
import org.example.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.*;


@Service
public class CarService {
    private CarRepository carRepository;
    private CategoryRepository categoryRepository;
    private Logger logger = LoggerFactory.getLogger(CarService.class);

    @Transactional
    public Car save(Car car) {
        carRepository.save(car);
        logger.info("Saved car with objectId: {}", car.getObjectId());
        return car;
    }

    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public Optional<Car> getById(Long id) {
        return carRepository.findById(id);
    }

    public Optional<Car> getByObjectId(String objectId) {
        return carRepository.findByObjectId(objectId);
    }

    public List<Car> getByBrand(String brand) {
        return carRepository.findByBrand(brand);
    }

    @Transactional
    public Car addCar(String brand, String model, Year year, String[] categoriesNames) {
        Car car = new Car();
        car.setBrand(brand);
        car.setModel(model);
        car.setYear(year);

        List<Category> categories = saveCategories(categoriesNames);
        car.setCategories(categories);

        return carRepository.save(car);
    }

    @Transactional
    public Car update(Car car, Long carId) {
        car.setId(carId);
        carRepository.save(car);
        logger.info("Updated car with id: {}", car.getId());
        return car;
    }

    @Transactional
    public void deleteById(Long id) {
        carRepository.deleteById(id);
        logger.info("Deleted by id: {}", id);
    }

    @Transactional
    public void deleteByObjectId(String objectId) {
        carRepository.deleteByObjectId(objectId);
        logger.info("Deleted by objectId: {}", objectId);
    }

    public List<Car> getByBrandAndModel(String brand, String model, PageRequest pageRequest) {
        Sort sort = pageRequest.getSort();
        Page<Car> page = carRepository.findByBrandAndModel(brand, model, pageRequest, sort);
        return page.getContent();
    }

    public List<Car> getByBrandAndModelAndYear(String brand, String model, Year year) {
        List<Car> cars = getByBrand(brand);
        cars.removeIf(obj -> obj.getModel().equals(model) && obj.getYear().equals(year));
        return cars;
    }

    public List<Car> getByBrandAndModelAndMinYearAndMaxYear(String brand, String model, Year minYear, Year maxYear) {
        List<Car> cars = getByBrand(brand);
        cars.removeIf(obj -> !obj.getModel().equals(model));
        cars.removeIf(obj -> (obj.getYear().isBefore(minYear) || obj.getYear().isAfter(maxYear)));
        return cars;
    }

    private List<Category> saveCategories(String[] categoriesNames) {
        List<Category> categories = new ArrayList<>();

        for (String categoryName : categoriesNames) {
            Optional<Category> existingCategory = categoryRepository.findByName(categoryName);

            if (existingCategory.isPresent()) {
                categories.add(existingCategory.get());
            } else {
                Category newCategory = new Category();
                newCategory.setName(categoryName);
                categories.add(categoryRepository.save(newCategory));
            }
        }

        return categories;
    }
}
