package com.gradle.boot.fintech.repositories;

import com.gradle.boot.fintech.models.City;

import java.util.Optional;

public interface CityRepository {
    Optional<City> findByName(String name);

    void addCity(City city);

    boolean existsByName(String name);
}
