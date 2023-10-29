package com.gradle.boot.fintech.repositories;

import com.gradle.boot.fintech.models.Weather;

import java.time.LocalDate;
import java.util.Optional;

public interface WeatherRepository {
    boolean existsByCityName(String cityName);

    Optional<Double> getTemperatureByCityNameAndDate(String cityName);

    boolean existsByCityNameAndDate(String cityName, LocalDate date);

    void updateByCityName(String cityName, LocalDate date, Double temperature);

    void deleteByCityName(String cityName);

    void addWeather(Weather weather);
}
