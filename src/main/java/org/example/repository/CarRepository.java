package org.example.repository;

import org.example.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByMake(String make);

    Optional<Car> findByObjectId(String objectId);

    void deleteByObjectId(String objectId);

    Page<Car> findByMakeAndModel(String make, String model, Pageable pageable);

    @Query("SELECT c FROM Car c WHERE c.make = :make AND c.model = :model AND c.year = :year")
    Page<Car> findByMakeAndModelAndYear(@Param("make") String make, @Param("model") String model, @Param("year") Year year, Pageable pageable);

    @Query("SELECT c FROM Car c " +
            "WHERE c.make = :make " +
            "AND c.model = :model " +
            "AND c.year BETWEEN :minYear AND :maxYear")
    Page<Car> findByMakeAndModelAndYearBetween(@Param("make") String make,
                                               @Param("model") String model,
                                               @Param("minYear") Year minYear,
                                               @Param("maxYear") Year maxYear,
                                               Pageable pageable);
}
