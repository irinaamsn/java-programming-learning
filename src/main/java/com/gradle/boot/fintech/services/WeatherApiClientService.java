package com.gradle.boot.fintech.services;

import com.gradle.boot.fintech.dto.WeatherDto;

public interface WeatherApiClientService {
    void save(String cityName, WeatherDto weatherDto);
}
