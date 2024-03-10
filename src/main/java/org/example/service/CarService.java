package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.CarDto;
import org.example.mapper.CarMapper;
import org.example.model.Car;
import org.example.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final Logger logger = LoggerFactory.getLogger(CarService.class);

    public CarService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    public Long create(CarDto carDto) {
        try {
            Car car = carMapper.toEntity(carDto);
            carRepository.save(car);
            logger.info("Saved car with objectId: {}", carDto.getObjectId());
            return car.getId();
        } catch (Exception ex) {
            logger.error("Error saving car with objectId: {}", carDto.getObjectId(), ex);
            throw new EntityNotFoundException("Error updating car", ex);
        }
    }

    public Page<CarDto> getAll(Pageable pageable) {
        return carRepository.findAll(pageable).map(carMapper::toDto);
    }

    public Optional<CarDto> getByObjectId(String objectId) {
        return carRepository.findByObjectId(objectId).map(carMapper::toDto);
    }

    public CarDto update(String objectId, CarDto carDto) {
        try {
            Car car = carRepository.findByObjectId(objectId)
                    .orElseThrow(() -> new EntityNotFoundException("Car not found with id: " + objectId));
            updateCarFields(car, carDto);
            carRepository.save(car);
            logger.info("Updated car with objectId: {}", carDto.getObjectId());
            return carDto;
        } catch (Exception ex) {
            logger.error("Error updating car with objectId: {}", carDto.getObjectId(), ex);
            throw new EntityNotFoundException("Error updating car", ex);
        }
    }

    public void deleteByObjectId(String objectId) {
        try {
            carRepository.deleteByObjectId(objectId);
            logger.info("Deleted by objectId: {}", objectId);
        } catch (RuntimeException ex) {
            logger.error("Error car with objectId: " + objectId + " not deleted", ex);
            throw new EntityNotFoundException("Error car with objectId: " + objectId + " not deleted", ex);
        }
    }

    public Page<CarDto> getByMake(String make) {
        List<CarDto> allCarDtoByMake = carRepository.findByMake(make).stream().map(carMapper::toDto).toList();

        if (allCarDtoByMake.isEmpty()) {
            throw new EntityNotFoundException("Error don't found with make: " + make);
        }

        return new PageImpl<>(allCarDtoByMake);
    }


    public Page<CarDto> getByMakeAndModel(String make, String model, PageRequest pageRequest) {
        return carRepository.findByMakeAndModel(make, model, pageRequest).map(carMapper::toDto);
    }

    public Page<CarDto> getByMakeAndModelAndYear(String make, String model, Year year, Pageable pageable) {
        try {
            Page<CarDto> carDtoPage = carRepository.findByMakeAndModelAndYear(make, model, year, pageable).map(carMapper::toDto);
            logger.info("Find by make: {}, model: {}, year: {}", make, model, year);
            return carDtoPage;
        } catch (RuntimeException ex) {
            logger.error("Error not find car with - make: {}, model: {}, year: {}: ", make, model, year, ex);
            throw new EntityNotFoundException("Error not find car with - make, model, year: " + make + model + year, ex);
        }
    }

    public Page<CarDto> getByMakeAndModelAndMinYearAndMaxYear(String make, String model, Year minYear, Year maxYear, Pageable pageable) {
        try {
            Page<CarDto> carDtoPage = carRepository.findByMakeAndModelAndYearBetween(make, model, minYear, maxYear, pageable).map(carMapper::toDto);
            logger.info("Find by make: {}, model: {}, min year: {}, max year: {}", make, model, minYear, maxYear);
            return carDtoPage;
        } catch (RuntimeException ex) {
            logger.error("Error not find car with - make: {}, model: {}, min year: {}, max year: {}", make, model, minYear, maxYear, ex);
            throw new EntityNotFoundException("Error not find car with - make, model, min year, max year: ", ex);
        }
    }

    private void updateCarFields(Car car, CarDto carDto) {
        car.setObjectId(carDto.getObjectId());
        car.setMake(carDto.getMake());
        car.setModel(carDto.getModel());
        car.setYear(Year.of(carDto.getYear()));
    }
}
