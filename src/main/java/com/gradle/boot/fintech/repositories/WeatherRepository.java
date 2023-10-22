package com.gradle.boot.fintech.repositories;

import com.gradle.boot.fintech.models.Weather;

import java.time.LocalDate;
import java.util.Optional;

public interface WeatherRepository {
    boolean existsByRegionCode(int regionCode);

    Optional<Double> getTemperatureByRegionCodeAndDate(int regionCode);

    boolean existsByRegionCodeAndDate(int regionCode, LocalDate date);

    void updateByRegionCode(int regionCode, LocalDate date, Double temperature, String typeName);

    void deleteByRegionCode(int regionCode);

    void addWeather(Weather weather);
}
