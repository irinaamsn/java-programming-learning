package com.gradle.boot.fintech.services.impl;

import com.gradle.boot.fintech.exceptions.NotCreatedException;
import com.gradle.boot.fintech.exceptions.NotFoundException;
import com.gradle.boot.fintech.models.City;
import com.gradle.boot.fintech.models.Weather;
import com.gradle.boot.fintech.models.WeatherType;
import com.gradle.boot.fintech.repositories.CityRepository;
import com.gradle.boot.fintech.repositories.WeatherRepository;
import com.gradle.boot.fintech.repositories.WeatherTypeRepository;
import com.gradle.boot.fintech.services.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final WeatherRepository weatherRepository;
    private final WeatherTypeRepository weatherTypeRepository;
    private final CityRepository cityRepository;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void save(String cityName, Weather weather) {
        if (weatherRepository.existsByCityName(cityName))
            throw new NotCreatedException(HttpStatus.BAD_REQUEST, "City already exists", System.currentTimeMillis());
        WeatherType weatherType = weatherTypeRepository.findByName(weather.getTypeName())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "The type of weather was not found", System.currentTimeMillis()));
        weather.setWeatherType(weatherType);

        City city = cityRepository.findByName(cityName)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "City not found", System.currentTimeMillis()));
        weather.setCity(city);

        weatherRepository.addWeather(weather);
    }

    @Override
    public void update(String cityName, Weather weather) {
        if (!weatherRepository.existsByCityName(cityName))
            throw new NotFoundException(HttpStatus.NOT_FOUND, "City not found", System.currentTimeMillis());

        if (weatherRepository.existsByCityNameAndDate(cityName, weather.getDate()))
            weatherRepository.updateByCityName(cityName, weather.getDate(), weather.getTemperature(), weather.getTypeName());
        else {
            WeatherType weatherType = weatherTypeRepository.findByName(weather.getTypeName())
                    .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "The type of weather was not found", System.currentTimeMillis()));
            weather.setWeatherType(weatherType);
            weather.setCity(cityRepository.findByName(cityName).get());
            weatherRepository.addWeather(weather);
        }
    }

    @Override
    public Double getTempByRegionId(String cityName) {
        if (weatherRepository.existsByCityName(cityName)) {
            return weatherRepository.getTemperatureByCityNameAndDate(cityName)
                    .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Temperature not found", System.currentTimeMillis()));
        }
        throw new NotFoundException(HttpStatus.NOT_FOUND, "City not found", System.currentTimeMillis());
    }

    @Override
    @Transactional
    public void delete(String cityName) {
        if (weatherRepository.existsByCityName(cityName))
            weatherRepository.deleteByCityName(cityName);
        else throw new NotFoundException(HttpStatus.NOT_FOUND, "City not found", System.currentTimeMillis());
    }
}
