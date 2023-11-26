package com.gradle.boot.fintech.services;

import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.models.Weather;

import java.util.List;

public interface WeatherApiClientService {
    void save(String cityName, WeatherDto weatherDto);
    void deleteWeatherMoreBundle(List<Weather> list);
    List<Weather> getAllWeatherByCityName(String cityName);
}
