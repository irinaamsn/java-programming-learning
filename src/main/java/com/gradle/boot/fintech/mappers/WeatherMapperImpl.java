package com.gradle.boot.fintech.mappers;

import com.gradle.boot.fintech.dto.WeatherApiResponseDto;
import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.models.Weather;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class WeatherMapperImpl implements WeatherMapper {
    @Override
    public Weather ToWeather(WeatherDto weatherDto) {
        Weather weather = new Weather();
        weather.setDate(weatherDto.getDate());
        weather.setTime(weatherDto.getTime());
        weather.setTemperature(weatherDto.getTemperature());
        return weather;
    }

    @Override
    public WeatherDto ToWeatherDto(WeatherApiResponseDto responseDto) {
        WeatherDto weatherDto = new WeatherDto();
        weatherDto.setCityName(responseDto.getLocation().getCityName());
        weatherDto.setTypeName(responseDto.getCurrent().getCondition().getTypeName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(responseDto.getLocation().getDateTime(), formatter);
        weatherDto.setDate(dateTime.toLocalDate());
        weatherDto.setTime(dateTime.toLocalTime());

        weatherDto.setTemperature(responseDto.getCurrent().getTemperature());
        return weatherDto;
    }
}
