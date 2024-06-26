package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CarDto;
import org.example.service.CarService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class TestCarController {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CarService carService;

    public static Page<CarDto> getCarDtoPages() {
        return new PageImpl<>(List.of(
                new CarDto("objectId1", "make1", 2022, "model1", Collections.singletonList("category1")),
                new CarDto("objectId2", "make2", 2023, "model2", Collections.singletonList("category2"))
        ));
    }

    public static Page<CarDto> getCarDtoPage() {
        return new PageImpl<>(List.of(new CarDto("objectId1", "make1", 2022, "model1", Collections.singletonList("category1"))));
    }

    public static Optional<CarDto> getSampleCarDtoOptionalWithSingleElement() {
        return Optional.of(new CarDto("objectId1", "make1", 2022, "model1", Collections.singletonList("category1")));
    }

    public static CarDto getSampleCarDtoWithSingleElement() {
        return new CarDto("objectId1", "make1", 2022, "model1", Collections.singletonList("category1"));
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_write"})
    void testCreate() throws Exception {
        CarDto carDto = getSampleCarDtoWithSingleElement();
        Long firstId = 1L;

        when(carService.create(carDto)).thenReturn(firstId);

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(carDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"))
                .andDo(MockMvcResultHandlers.print());
        verify(carService).create(carDto);
    }

    @Test
    void testGetAll() throws Exception {
        Page<CarDto> carDtoPage = TestCarController.getCarDtoPages();
        when(carService.getAll(any(Pageable.class))).thenReturn(carDtoPage);

        mockMvc.perform(get("/cars"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.content[0].objectId").value("objectId1"))
                .andExpect(jsonPath("$.content[0].make").value("make1"))
                .andExpect(jsonPath("$.content[0].year").value(2022))
                .andExpect(jsonPath("$.content[0].model").value("model1"))
                .andExpect(jsonPath("$.content[0].categories").isArray())
                .andExpect(jsonPath("$.content[0].categories", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.content[0].categories[0]").value("category1"))
                .andExpect(jsonPath("$.content[1].objectId").value("objectId2"))
                .andExpect(jsonPath("$.content[1].make").value("make2"))
                .andExpect(jsonPath("$.content[1].year").value(2023))
                .andExpect(jsonPath("$.content[1].model").value("model2"))
                .andExpect(jsonPath("$.content[1].categories").isArray())
                .andExpect(jsonPath("$.content[1].categories", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.content[1].categories[0]").value("category2"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testGetCarDtoByObjectId() throws Exception {
        String objectId = "objectId1";
        Optional<CarDto> carDto = TestCarController.getSampleCarDtoOptionalWithSingleElement();

        when(carService.getByObjectId(objectId)).thenReturn(carDto);

        mockMvc.perform(get("/cars/" + objectId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.objectId").value("objectId1"))
                .andExpect(jsonPath("$.make").value("make1"))
                .andExpect(jsonPath("$.year").value(2022))
                .andExpect(jsonPath("$.model").value("model1"))
                .andExpect(jsonPath("$.categories[0]").value("category1"))
                .andDo(MockMvcResultHandlers.print());
        verify(carService).getByObjectId("objectId1");
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_write"})
    public void testUpdate() throws Exception {
        CarDto updatedCarDto = new CarDto("updatedObjectId", "updatedMake", 2022, "updatedModel", Collections.singletonList("updatedCategory"));

        when(carService.update(any(String.class), eq(updatedCarDto))).thenReturn(updatedCarDto);

        mockMvc.perform(put("/cars/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedCarDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.objectId").value("updatedObjectId"))
                .andExpect(jsonPath("$.make").value("updatedMake"))
                .andExpect(jsonPath("$.year").value(2022))
                .andExpect(jsonPath("$.model").value("updatedModel"))
                .andExpect(jsonPath("$.categories[0]").value("updatedCategory"))
                .andDo(MockMvcResultHandlers.print());
        verify(carService).update(eq("123"), eq(updatedCarDto));
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_write"})
    public void testDeleteByObjectId() throws Exception {
        when(carService.deleteByObjectId("objectId1")).thenReturn("objectId1");

        mockMvc.perform(delete("/cars/{objectId}", "objectId1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(carService).deleteByObjectId(any(String.class));
    }

    @Test
    void testGetByMake() throws Exception {
        Page<CarDto> carDtoPage = getCarDtoPage();
        String nameMake = "make1";

        when(carService.getByMake(eq(nameMake), any(Pageable.class))).thenReturn(carDtoPage);

        mockMvc.perform(get("/cars/makes/{make}", nameMake))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].objectId").value("objectId1"))
                .andExpect(jsonPath("$.content[0].make").value("make1"))
                .andExpect(jsonPath("$.content[0].year").value(2022))
                .andExpect(jsonPath("$.content[0].model").value("model1"))
                .andExpect(jsonPath("$.content[0].categories").isArray())
                .andExpect(jsonPath("$.content[0].categories", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.content[0].categories[0]").value("category1"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testGetByMakeAndModel() throws Exception {
        Page<CarDto> carDtoPage = getCarDtoPage();
        String makeName = "make1";
        String modelName = "model1";
        when(carService.getByMakeAndModel(eq("make1"), eq("model1"), any(PageRequest.class))).thenReturn(carDtoPage);


        mockMvc.perform(get("/cars/makes/{make}/models/{model}", makeName, modelName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].make").value(makeName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].model").value(modelName));

        verify(carService).getByMakeAndModel(eq("make1"), eq("model1"), any(PageRequest.class));
    }

    @Test
    void testGetCarsByMakeAndModelAndYear() throws Exception {
        Page<CarDto> carDtoPage = getCarDtoPage();

        when(carService.getByMakeAndModelAndYear(
                eq("make1"),
                eq("model1"),
                eq(Year.of(2022)),
                any(PageRequest.class))).thenReturn(carDtoPage);

        mockMvc.perform(get("/cars/makes/{make}/models/{model}/years/{year}", "make1", "model1", 2022))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].make").value("make1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].model").value("model1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].year").value(2022));

        verify(carService).getByMakeAndModelAndYear(eq("make1"), eq("model1"), eq(Year.of(2022)), any(PageRequest.class));
    }

    @Test
    void testGetCarsByMakeAndModelAndMinYearAndMaxYear() throws Exception {
        Page<CarDto> carDtoPage = getCarDtoPage();
        String makeName = "make1";
        String modelName = "model1";

        when(carService.getByMakeAndModelAndMinYearAndMaxYear(
                eq(makeName),
                eq(modelName),
                eq(Year.of(2020)),
                eq(Year.of(2023)),
                any(PageRequest.class))).thenReturn(carDtoPage);


        mockMvc.perform(get("/cars/makes/{make}/models/{model}/min-years/{minYear}/max-years/{maxYear}", makeName, modelName, 2020, 2023))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].make").value("make1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].model").value("model1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].year").value(2022));

        verify(carService).getByMakeAndModelAndMinYearAndMaxYear(
                eq(makeName),
                eq(modelName),
                eq(Year.of(2020)),
                eq(Year.of(2023)),
                any(PageRequest.class));
    }
}