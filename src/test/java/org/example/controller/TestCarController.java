package org.example.controller;

import org.example.model.Car;
import org.example.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(
        exclude = {
                DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        })
class TestCarController {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CarService carService;


    @Test
    void testGetAll() throws Exception {
        when(carService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/cars"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(carService).getAll();
    }

    @Test
    void testGetCarByObjectId() throws Exception {
        Car car = new Car();
        car.setObjectId("AAAAA");
        when(carService.getByObjectId(car.getObjectId())).thenReturn(Optional.of(car));

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/object-id/AAAAA"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(carService).getByObjectId("AAAAA");
    }

    @Test
    void testGetCarsByBrand() throws Exception {
        Car car = new Car();
        car.setBrand("brand");

        when(carService.getByBrand(car.getBrand())).thenReturn(List.of(car));

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/brand/{brand}", "brand"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand").value("brand"));

        verify(carService).getByBrand("brand");
    }

    @Test
    void testGetCarsByBrandAndModel() throws Exception {
        Car car1 = new Car();
        car1.setBrand("brand");
        car1.setModel("model1");

        Car car2 = new Car();
        car2.setBrand("brand");
        car2.setModel("model2");
        when(carService.getByBrandAndModel(eq("brand"), eq("model"), any(PageRequest.class)))
                .thenReturn(Arrays.asList(car1, car2));


        mockMvc.perform(MockMvcRequestBuilders.get("/cars/brand/{brand}/model/{model}", "brand", "model"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand").value("brand"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model").value("model1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].brand").value("brand"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].model").value("model2"));

        verify(carService).getByBrandAndModel(eq("brand"), eq("model"), any(PageRequest.class));
    }

    @Test
    void testGetCarsByBrandAndModelAndYear() throws Exception {
        Car car = new Car();
        car.setBrand("brand");
        car.setModel("model");
        car.setYear(Year.of(2022));

        when(carService.getByBrandAndModelAndYear(car.getBrand(), car.getModel(), car.getYear())).thenReturn(List.of(car));

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/brand/{brand}/model/{model}/year/{year}", "brand", "model", 2022))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand").value("brand"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model").value("model"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(2022));

        verify(carService).getByBrandAndModelAndYear("brand", "model", Year.of(2022));
    }

    @Test
    void testGetCarsByBrandAndModelAndMinYearAndMaxYear() throws Exception {
        Car car = new Car();
        car.setBrand("brand");
        car.setModel("model");
        car.setYear(Year.of(2022));

        when(carService.getByBrandAndModelAndMinYearAndMaxYear(car.getBrand(), car.getModel(), Year.of(2020), Year.of(2025))).thenReturn(List.of(car));

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/brand/{brand}/model/{model}/min-year/{minYear}/max-year/{maxYear}", "brand", "model", 2020, 2025))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand").value("brand"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model").value("model"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(2022));

        verify(carService).getByBrandAndModelAndMinYearAndMaxYear("brand", "model", Year.of(2020), Year.of(2025));
    }

    @Test
    void add_shouldNotAccess_whenUserUnauthenticated() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/cars?brand=brand&model=model&year=2020&categories=cat1,cat2,cat3"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}