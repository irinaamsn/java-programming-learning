package com.gradle.boot.fintech.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.exceptions.NotCreatedException;
import com.gradle.boot.fintech.exceptions.NotFoundException;
import com.gradle.boot.fintech.services.WeatherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Testcontainers
@WebMvcTest(WeatherController.class)
class WeatherControllerIT {
    @Container
    public static GenericContainer<?> h2Container = new GenericContainer<>("oscarfonts/h2")
            .withExposedPorts(1521, 81)
            .withEnv("H2_OPTIONS", "-ifNotExists")
            .waitingFor(Wait.defaultWaitStrategy());

    @Autowired
    private MockMvc mockMvc;
    private static final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private WeatherService weatherService;

    @BeforeAll
    static void setup() {
        System.setProperty("spring.datasource.driverClassName", "org.h2.Driver");
        System.setProperty("spring.datasource.url", "jdbc:h2:tcp://" +
                h2Container.getContainerIpAddress() + ":" + h2Container.getMappedPort(1521) + "/test");
        System.setProperty("spring.datasource.username", "sa");
        System.setProperty("spring.datasource.password", "");
    }

    @Test
    void testGetTemperatureByCityName() throws Exception {
        String cityName = "cityName";
        double expectedTemperature = 1.1;

        when(weatherService.getTempByCityName(cityName))
                .thenReturn(expectedTemperature);

        mockMvc.perform(get("/api/weather/{city}", cityName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedTemperature));
    }

    @Test
    void handleNotFoundCity_testGetTemperatureByCityName() throws Exception {
        String cityName = "cityName";
        String expectedErrorMessage = "City not found";
        int expectedHttpStatus = HttpStatus.NOT_FOUND.value();

        when(weatherService.getTempByCityName(cityName))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "City not found", System.currentTimeMillis()));

        mockMvc.perform(get("/api/weather/{city}", cityName))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(expectedHttpStatus))
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }

    @Test
    void handleNotFoundTemperature_testGetTemperatureByCityName() throws Exception {
        String cityName = "cityName";
        String expectedErrorMessage = "Temperature not found";
        int expectedHttpStatus = HttpStatus.NOT_FOUND.value();

        when(weatherService.getTempByCityName(cityName))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "Temperature not found", System.currentTimeMillis()));

        mockMvc.perform(get("/api/weather/{city}", cityName))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(expectedHttpStatus))
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }

    @Test
    void testAddCityToWeather() throws Exception {
        String cityName = "cityName";
        String typeName = "typeName";
        String expectedResponseMessage = "Weather record with a new city added";

        doNothing().when(weatherService).save(eq(cityName), any(WeatherDto.class));

        mockMvc.perform(post("/api/weather/{city}", cityName)
                        .content(mapper.writeValueAsString(new WeatherDto(cityName, 1.1, typeName)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedResponseMessage))
                .andReturn();

        verify(weatherService).save(eq(cityName), any(WeatherDto.class));
    }

    @Test
    void handleNotCreatedCity_testAddCityToWeather() throws Exception {
        String cityName = "cityName";
        String expectedErrorMessage = "City already exists";
        int expectedHttpStatus = HttpStatus.BAD_REQUEST.value();

        when(weatherService.getTempByCityName(cityName))
                .thenThrow(new NotCreatedException(HttpStatus.BAD_REQUEST, "City already exists", System.currentTimeMillis()));

        mockMvc.perform(get("/api/weather/{city}", cityName))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(expectedHttpStatus))
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }

    @Test
    void handleNotFoundCity_testAddCityToWeather() throws Exception {
        String cityName = "cityName";
        String expectedErrorMessage = "City not found";
        int expectedHttpStatus = HttpStatus.NOT_FOUND.value();

        when(weatherService.getTempByCityName(cityName))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "City not found", System.currentTimeMillis()));

        mockMvc.perform(get("/api/weather/{city}", cityName))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(expectedHttpStatus))
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }

    @Test
    void handleNotFoundType_testAddCityToWeather() throws Exception {
        String cityName = "cityName";
        String expectedErrorMessage = "The type of weather was not found";
        int expectedHttpStatus = HttpStatus.NOT_FOUND.value();

        when(weatherService.getTempByCityName(cityName))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "The type of weather was not found", System.currentTimeMillis()));

        mockMvc.perform(get("/api/weather/{city}", cityName))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(expectedHttpStatus))
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }

}