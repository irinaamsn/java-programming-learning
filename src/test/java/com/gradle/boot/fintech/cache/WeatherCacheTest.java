package com.gradle.boot.fintech.cache;

import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.mappers.WeatherMapper;
import com.gradle.boot.fintech.models.City;
import com.gradle.boot.fintech.models.Weather;
import com.gradle.boot.fintech.repositories.CityRepository;
import com.gradle.boot.fintech.repositories.WeatherRepository;
import com.gradle.boot.fintech.repositories.WeatherTypeRepository;
import com.gradle.boot.fintech.services.WeatherService;
import com.gradle.boot.fintech.services.impl.WeatherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
class WeatherCacheTest {
    @Mock
    private WeatherCache weatherCache;
    @Mock
    private WeatherRepository weatherRepository;
    @Mock
    private WeatherTypeRepository weatherTypeRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private WeatherMapper weatherMapper;
    @Autowired
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherServiceImpl(weatherCache, weatherRepository, weatherTypeRepository, cityRepository, weatherMapper);
    }

    @Test
    void getWeatherFromCache_thenCacheHasData() {
        String testCityName = "testCityName";
        double testTemperature = 1.0;
        Weather testWeather = new Weather(new City(testCityName), testTemperature);

        when(weatherMapper.toWeatherDto(testWeather)).thenReturn(new WeatherDto(testCityName, testTemperature));
        when(weatherCache.getWeather(testCityName)).thenReturn(Optional.of(testWeather));

        double result = weatherService.getTempByCityName(testCityName);

        assertEquals(testTemperature, result);
        verify(weatherCache, times(1)).getWeather(testCityName);
        verify(weatherRepository, never()).getWeatherByCityName(testCityName);
        verify(weatherCache, never()).updateOrAddWeather(testCityName, testWeather);
        verify(weatherRepository, never()).getTemperatureByCityNameAndDate(testCityName);
    }

    @Test
    void getWeatherFromDatabase_thenCacheNotHasData() {
        String testCityName = "testCityName";
        double testTemperature = 1.0;
        Weather testWeather = new Weather(new City(testCityName), testTemperature);

        when(weatherCache.getWeather(testCityName)).thenReturn(Optional.empty());
        when(weatherRepository.existsByCityName(testCityName)).thenReturn(true);
        when(weatherRepository.getWeatherByCityName(testCityName)).thenReturn(Optional.of(testWeather));
        when(weatherRepository.getTemperatureByCityNameAndDate(testCityName)).thenReturn(Optional.of(testTemperature));

        double result = weatherService.getTempByCityName(testCityName);

        assertEquals(testTemperature, result);
        verify(weatherCache, times(1)).getWeather(testCityName);
        verify(weatherRepository, times(1)).getWeatherByCityName(testCityName);
        verify(weatherCache, times(1)).updateOrAddWeather(testCityName, testWeather);
        verify(weatherRepository, times(1)).getTemperatureByCityNameAndDate(testCityName);
    }

    @Test
    void updateWeather_thenDataRemoveFromCache() {
        String testCityName = "testCityName";
        double testTemperature = 1.0;
        WeatherDto testWeather = new WeatherDto(testCityName, testTemperature, LocalDate.now());

        when(weatherRepository.existsByCityName(testCityName)).thenReturn(true);
        when(weatherRepository.existsByCityNameAndDate(testCityName, testWeather.getDate())).thenReturn(true);
        when(weatherMapper.toWeather(testWeather)).thenReturn(new Weather(new City(testCityName), testTemperature));

        weatherService.update(testCityName, testWeather);

        verify(weatherCache, times(1)).updateOrAddWeather(testCityName, weatherMapper.toWeather(testWeather));
    }
}