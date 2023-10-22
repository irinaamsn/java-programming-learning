package com.gradle.boot.fintech.mappers;

import com.gradle.boot.fintech.dto.WeatherApiResponseDto;
import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.models.Weather;


public interface WeatherMapper {
    Weather ToWeather(WeatherDto weatherDto);

    Weather ToWeather(WeatherApiResponseDto weatherApiResponse);

    WeatherDto ToWeatherDto(WeatherApiResponseDto body);
}
