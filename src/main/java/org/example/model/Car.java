package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "object_id")
    private String objectId;

    @Column(name = "model")
    private String model;

    @Column(name = "brand")
    private String brand;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "car_category",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();

    @Column(name = "year")
    private Year year;
}
