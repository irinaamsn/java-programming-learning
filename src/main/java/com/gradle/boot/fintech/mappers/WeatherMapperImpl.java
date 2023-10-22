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
        weather.setCityName(weatherDto.getCityName());
        weather.setTypeName(weatherDto.getTypeName());
        weather.setDate(weatherDto.getDate());
        weather.setTime(weatherDto.getTime());
        weather.setTemperature(weatherDto.getTemperature());
        return weather;
    }

    @Override
    public Weather ToWeather(WeatherApiResponseDto weatherApiResponse) {
        Weather weather = new Weather();
        weather.setCityName(weatherApiResponse.getLocation().getCityName());
        weather.setTypeName(weatherApiResponse.getCurrent().getCondition().getTypeName());
        weather.setTemperature(weatherApiResponse.getCurrent().getTemperature());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(weatherApiResponse.getLocation().getDateTime(), formatter);//todo

        weather.setDate(dateTime.toLocalDate());
        weather.setTime(dateTime.toLocalTime());
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
