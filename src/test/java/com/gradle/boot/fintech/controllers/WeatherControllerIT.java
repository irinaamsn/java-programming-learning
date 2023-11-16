package com.gradle.boot.fintech.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradle.boot.fintech.config.SecurityConfigTest;
import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.exceptions.NotCreatedException;
import com.gradle.boot.fintech.exceptions.NotFoundException;
import com.gradle.boot.fintech.services.WeatherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Base64;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Testcontainers
@WebMvcTest(WeatherController.class)
@AutoConfigureMockMvc
class WeatherControllerIT {

    @Container
    static GenericContainer<?> h2Container = new GenericContainer<>("oscarfonts/h2")
            .withExposedPorts(1521, 81)
            .withEnv("H2_OPTIONS", "-ifNotExists")
            .waitingFor(Wait.defaultWaitStrategy());
    @Autowired
    private MockMvc mockMvc;
    private static final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private WeatherService weatherService;

    @DynamicPropertySource
    static void registerH2Properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driverClassName", () -> "org.h2.Driver");
        registry.add("spring.datasource.url",
                () -> "jdbc:h2:tcp://" + h2Container.getContainerIpAddress() + ":" + h2Container.getMappedPort(1521) + "/test");
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "");
    }

    @Test
    public void testGetTemperatureByCity() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/{city}", "Moscow"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
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
        String cityName = "Город";
        String typeName = "тип";
        String expectedResponseMessage = "Weather record with a new city added";

        mockMvc.perform(post("/api/weather/{city}", cityName)
                        .content(mapper.writeValueAsString(new WeatherDto(cityName, 1.1, typeName)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedResponseMessage));

        verify(weatherService).save(eq(cityName), any(WeatherDto.class));
    }

    @Test
    void handleNotCreatedCity_testAddCityToWeather() throws Exception {
        String cityName = "Город";
        String typeName = "Тип";
        String expectedErrorMessage = "City already exists";
        int expectedHttpStatus = HttpStatus.BAD_REQUEST.value();

        doThrow(new NotCreatedException(HttpStatus.BAD_REQUEST, "City already exists", System.currentTimeMillis()))
                .when(weatherService).save(eq(cityName), any(WeatherDto.class));

        mockMvc.perform(post("/api/weather/{city}", cityName)
                        .content(mapper.writeValueAsString(new WeatherDto(cityName, 1.1, typeName)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(expectedHttpStatus))
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }

    @Test
    void handleNotFoundCity_testAddCityToWeather() throws Exception {
        String cityName = "Город";
        String typeName = "Тип";
        String expectedErrorMessage = "City not found";
        int expectedHttpStatus = HttpStatus.NOT_FOUND.value();

        doThrow(new NotFoundException(HttpStatus.NOT_FOUND, "City not found", System.currentTimeMillis()))
                .when(weatherService).save(eq(cityName), any(WeatherDto.class));

        mockMvc.perform(post("/api/weather/{city}", cityName)
                        .content(mapper.writeValueAsString(new WeatherDto(cityName, 1.1, typeName)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(expectedHttpStatus))
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }

    @Test
    void handleNotFoundType_testAddCityToWeather() throws Exception {
        String cityName = "Город";
        String typeName = "Тип";
        String expectedErrorMessage = "The type of weather was not found";
        int expectedHttpStatus = HttpStatus.NOT_FOUND.value();

        doThrow(new NotFoundException(HttpStatus.NOT_FOUND, "The type of weather was not found", System.currentTimeMillis()))
                .when(weatherService).save(eq(cityName), any(WeatherDto.class));

        mockMvc.perform(post("/api/weather/{city}", cityName)
                        .content(mapper.writeValueAsString(new WeatherDto(cityName, 1.1, typeName)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(expectedHttpStatus))
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }
}