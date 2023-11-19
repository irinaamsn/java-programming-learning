package com.gradle.boot.fintech.services.impl;

import com.gradle.boot.fintech.cache.WeatherCache;
import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.exceptions.NotCreatedException;
import com.gradle.boot.fintech.exceptions.NotFoundException;
import com.gradle.boot.fintech.mappers.WeatherMapper;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherServiceImpl implements WeatherService {
    private final WeatherCache weatherCache;
    private final WeatherRepository weatherRepository;
    private final WeatherTypeRepository weatherTypeRepository;
    private final CityRepository cityRepository;
    private final WeatherMapper weatherMapper;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void save(String cityName, WeatherDto weatherDto) {
        if (weatherRepository.existsByCityName(cityName))
            throw new NotCreatedException(HttpStatus.BAD_REQUEST, "City already exists", System.currentTimeMillis());

        WeatherType weatherType = weatherTypeRepository.findByName(weatherDto.getTypeName())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "The type of weather was not found", System.currentTimeMillis()));
        Weather weather = weatherMapper.toWeather(weatherDto);
        weather.setWeatherType(weatherType);

        City city = cityRepository.findByName(cityName)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "City not found", System.currentTimeMillis()));
        weather.setCity(city);

        weatherRepository.addWeather(weather);
    }

    @Override
    public void update(String cityName, WeatherDto weatherDto) {
        if (!weatherRepository.existsByCityName(cityName))
            throw new NotFoundException(HttpStatus.NOT_FOUND, "City not found", System.currentTimeMillis());

        if (weatherRepository.existsByCityNameAndDate(cityName, weatherDto.getDate()))
            weatherRepository.updateByCityName(cityName, weatherDto.getDate(), weatherDto.getTemperature());
        else {
            WeatherType weatherType = weatherTypeRepository.findByName(weatherDto.getTypeName())
                    .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "The type of weather was not found", System.currentTimeMillis()));
            Weather weather = weatherMapper.toWeather(weatherDto);
            weather.setWeatherType(weatherType);
            weather.setCity(cityRepository.findByName(cityName).get());
            weatherRepository.addWeather(weather);
        }
        weatherCache.updateOrAddWeather(cityName, weatherMapper.toWeather(weatherDto));
    }

    @Override
    public Double getTempByCityName(String cityName) {
        Optional<Weather> weather = weatherCache.getWeather(cityName);
        if (weather.isPresent()) {
            return weatherMapper.toWeatherDto(weather.get()).getTemperature();
        } else {
            if (weatherRepository.existsByCityName(cityName)) {
                weather = weatherRepository.getWeatherByCityName(cityName);
                weather.ifPresent(w ->
                        weatherCache.updateOrAddWeather(cityName, w)
                );
                return weatherRepository.getTemperatureByCityNameAndDate(cityName)
                        .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Temperature not found", System.currentTimeMillis()));
            }
            throw new NotFoundException(HttpStatus.NOT_FOUND, "City not found", System.currentTimeMillis());
        }
    }

    @Override
    public void delete(String cityName) {
        if (weatherRepository.existsByCityName(cityName))
            weatherRepository.deleteByCityName(cityName);
        else throw new NotFoundException(HttpStatus.NOT_FOUND, "City not found", System.currentTimeMillis());
    }
}
