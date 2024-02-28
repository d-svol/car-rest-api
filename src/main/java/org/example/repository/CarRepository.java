package org.example.repository;

import org.example.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByBrand(String brand);

    Optional<Car> findByObjectId(String objectId);

    void deleteByObjectId(String objectId);

    Page<Car> findByBrandAndModel(String brand, String model, Pageable pageable, Sort sort);
}
