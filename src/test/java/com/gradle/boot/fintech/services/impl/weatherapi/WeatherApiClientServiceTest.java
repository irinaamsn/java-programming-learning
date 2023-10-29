package com.gradle.boot.fintech.services.impl.weatherapi;

import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.exceptions.NotCreatedException;
import com.gradle.boot.fintech.mappers.WeatherMapper;
import com.gradle.boot.fintech.mappers.WeatherMapperImpl;
import com.gradle.boot.fintech.models.City;
import com.gradle.boot.fintech.models.WeatherType;
import com.gradle.boot.fintech.repositories.CityRepository;
import com.gradle.boot.fintech.repositories.WeatherRepository;
import com.gradle.boot.fintech.repositories.WeatherTypeRepository;
import com.gradle.boot.fintech.services.WeatherApiClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class WeatherApiClientServiceTest {
    @Mock
    private WeatherRepository weatherRepository;
    @Mock
    private WeatherTypeRepository weatherTypeRepository;
    @Mock
    private CityRepository cityRepository;
    @Spy
    private WeatherMapper weatherMapper = new WeatherMapperImpl();
    @Autowired
    private WeatherApiClientService weatherService;

    @BeforeEach
    void setUp() {
       weatherService = new WeatherApiClientSpringServiceImpl(weatherRepository, weatherTypeRepository, cityRepository, weatherMapper);
    }

    @Test
    void testAddNewWeather() {
        String cityName = "cityName";
        String weatherType = "weatherType";
        WeatherDto weatherDto = new WeatherDto(cityName, 1.1, weatherType,
                LocalDate.now(), LocalTime.now());

        when(weatherRepository.existsByCityName(cityName)).thenReturn(false);
        when(weatherTypeRepository.existsByName(weatherType)).thenReturn(true);
        when(weatherTypeRepository.findByName(weatherType)).thenReturn(Optional.of(new WeatherType(weatherType)));
        when(cityRepository.existsByName(cityName)).thenReturn(true);
        when(cityRepository.findByName(cityName)).thenReturn(Optional.of(new City(cityName)));

        weatherService.save(cityName, weatherDto);

        verify(weatherRepository, times(1)).existsByCityName(anyString());
        verify(weatherTypeRepository,never()).addWeatherType(any(WeatherType.class));
        verify(weatherTypeRepository, times(1)).existsByName(anyString());
        verify(cityRepository, times(1)).existsByName(anyString());
        verify(cityRepository,never()).addCity(any(City.class));
        verify(weatherRepository, times(1)).addWeather(any());
    }

    @Test
    void addNewWeatherType_testAddNewWeather() {
        String cityName = "cityName";
        String weatherType = "newWeatherType";
        WeatherDto weatherDto = new WeatherDto(cityName, 1.1, weatherType,
                LocalDate.now(), LocalTime.now());

        when(weatherRepository.existsByCityName(cityName)).thenReturn(false);
        when(weatherTypeRepository.existsByName(weatherType)).thenReturn(false);
        when(weatherTypeRepository.findByName(weatherType)).thenReturn(Optional.of(new WeatherType(weatherType)));
        when(cityRepository.existsByName(cityName)).thenReturn(true);
        when(cityRepository.findByName(cityName)).thenReturn(Optional.of(new City(cityName)));

        assertDoesNotThrow(() -> weatherService.save(cityName, weatherDto));

        verify(weatherRepository, times(1)).existsByCityName(anyString());
        verify(weatherTypeRepository, times(1)).addWeatherType(any(WeatherType.class));
        verify(weatherTypeRepository, times(1)).existsByName(anyString());
        verify(cityRepository, times(1)).existsByName(anyString());
        verify(cityRepository, never()).addCity(any(City.class));
        verify(weatherRepository, times(1)).addWeather(any());
    }

    @Test
    void handleException_testAddNewWeatherAlreadyExists() {
        String cityName = "cityName";
        String weatherType = "weatherType";
        WeatherDto weatherDto = new WeatherDto(cityName, 1.1, weatherType,
                LocalDate.now(), LocalTime.now());

        when(weatherRepository.existsByCityName(cityName)).thenReturn(true);

        assertThrows(NotCreatedException.class, () -> weatherService.save(cityName, weatherDto));

        verify(weatherRepository, times(1)).existsByCityName(anyString());
        verify(weatherTypeRepository,never()).addWeatherType(any(WeatherType.class));
        verify(weatherTypeRepository, never()).existsByName(anyString());
        verify(cityRepository,never()).existsByName(anyString());
        verify(cityRepository,never()).addCity(any(City.class));
        verify(weatherRepository, never()).addWeather(any());
    }
}