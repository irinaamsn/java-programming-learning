package com.gradle.boot.fintech.repositories;

import com.gradle.boot.fintech.models.WeatherType;

import java.util.Optional;

public interface WeatherTypeRepository {
    Optional<WeatherType> findByName(String name);
}
