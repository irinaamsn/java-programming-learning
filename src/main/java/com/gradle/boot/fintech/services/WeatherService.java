package com.gradle.boot.fintech.services;

import com.gradle.boot.fintech.models.Weather;

public interface WeatherService {
    void save(int regionCode, Weather weather);
    void update(int regionCode, Weather weather);
    Double getTempByRegionId(int regionCode);
    void delete(int regionCode) ;
}
