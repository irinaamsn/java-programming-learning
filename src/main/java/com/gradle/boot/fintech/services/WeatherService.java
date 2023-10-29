package com.gradle.boot.fintech.services;

import com.gradle.boot.fintech.dto.WeatherDto;

public interface WeatherService {
    void save(String cityName, WeatherDto weatherDto);
    void update(String cityName, WeatherDto weatherDto);
    Double getTempByRegionId(String cityName);
    void delete(String cityName);
}
