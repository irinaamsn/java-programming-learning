package com.gradle.boot.fintech.services;

import com.gradle.boot.fintech.models.Weather;

public interface WeatherService {
    void save(int regionId, Weather weather);
    void update(int regionId, Weather weather);
    Double getTempByRegionId(int regionId);
    void delete(int regionId) ;
}
