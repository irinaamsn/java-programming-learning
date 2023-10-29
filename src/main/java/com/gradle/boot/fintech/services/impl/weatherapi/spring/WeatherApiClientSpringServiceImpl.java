package com.gradle.boot.fintech.services.impl.weatherapi.spring;

import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.exceptions.NotCreatedException;
import com.gradle.boot.fintech.mappers.WeatherMapper;
import com.gradle.boot.fintech.models.City;
import com.gradle.boot.fintech.models.Weather;
import com.gradle.boot.fintech.models.WeatherType;
import com.gradle.boot.fintech.repositories.CityRepository;
import com.gradle.boot.fintech.repositories.WeatherRepository;
import com.gradle.boot.fintech.repositories.WeatherTypeRepository;
import com.gradle.boot.fintech.services.WeatherApiClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Profile("weatherApiClientSpringService")
public class WeatherApiClientSpringServiceImpl implements WeatherApiClientService {
    private final WeatherRepository weatherRepository;
    private final WeatherTypeRepository weatherTypeRepository;
    private final CityRepository cityRepository;
    private final WeatherMapper weatherMapper;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void save(String cityName, WeatherDto weatherDto) {
        if (weatherRepository.existsByCityName(cityName))
            throw new NotCreatedException(HttpStatus.BAD_REQUEST, "City already exists", System.currentTimeMillis());

        if (!weatherTypeRepository.existsByName(weatherDto.getTypeName()))
            weatherTypeRepository.addWeatherType(
                    new WeatherType(weatherDto.getTypeName())
            );
        WeatherType weatherType = weatherTypeRepository.findByName(weatherDto.getTypeName()).get();

        if (!cityRepository.existsByName(weatherDto.getCityName()))
            cityRepository.addCity(
                    new City(weatherDto.getCityName())
            );
        City city = cityRepository.findByName(cityName).get();

        Weather weather = weatherMapper.ToWeather(weatherDto);
        weather.setWeatherType(weatherType);
        weather.setCity(city);
        weatherRepository.addWeather(weather);
    }
}
