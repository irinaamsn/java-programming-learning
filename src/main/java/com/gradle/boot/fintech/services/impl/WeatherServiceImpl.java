package com.gradle.boot.fintech.services.impl;

import com.gradle.boot.fintech.exceptions.NotCreatedException;
import com.gradle.boot.fintech.exceptions.NotFoundException;
import com.gradle.boot.fintech.models.Region;
import com.gradle.boot.fintech.models.Weather;
import com.gradle.boot.fintech.models.WeatherType;
import com.gradle.boot.fintech.repositories.RegionRepository;
import com.gradle.boot.fintech.repositories.WeatherRepository;
import com.gradle.boot.fintech.repositories.WeatherTypeRepository;
import com.gradle.boot.fintech.services.WeatherService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class WeatherServiceImpl implements WeatherService {
    private final WeatherRepository weatherRepository;
    private final WeatherTypeRepository weatherTypeRepository;
    private final RegionRepository regionRepository;
    public WeatherServiceImpl(@Qualifier("weatherJpaRepository")WeatherRepository weatherRepository,
                              @Qualifier("weatherTypeJpaRepository")WeatherTypeRepository weatherTypeRepository,
                              @Qualifier("regionJpaRepository")RegionRepository regionRepository) {
        this.weatherRepository = weatherRepository;
        this.weatherTypeRepository = weatherTypeRepository;
        this.regionRepository = regionRepository;
    }

    @Override
    @Transactional
    public void save(int regionCode, Weather weather) {
        if (weatherRepository.existsByRegionCode(regionCode))
            throw new NotCreatedException(HttpStatus.BAD_REQUEST,"Region already exists", System.currentTimeMillis());

        Optional<WeatherType> weatherType=weatherTypeRepository.findByName(weather.getTypeName());
        if (weatherType.isPresent()) weather.setWeatherType(weatherType.get());
        else throw new NotFoundException(HttpStatus.NOT_FOUND, "The type of weather was not found",System.currentTimeMillis());

        Optional<Region> region=regionRepository.findByCode(regionCode);
        if (region.isPresent()) weather.setRegion(region.get());
        else throw new NotFoundException(HttpStatus.NOT_FOUND,"Region not found",System.currentTimeMillis());

        weatherRepository.addWeather(weather);
    }
    @Override
    @Transactional
    public void update(int regionCode, Weather weather) {
        if (!weatherRepository.existsByRegionCode(regionCode))
            throw new NotFoundException(HttpStatus.NOT_FOUND,"Region not found", System.currentTimeMillis());

        if (weatherRepository.existsByRegionCodeAndDate(regionCode,weather.getDate()))
            weatherRepository.updateByRegionCode(regionCode,weather.getDate(),weather.getTemperature(),weather.getTypeName());
        else {
            Optional<WeatherType> weatherType=weatherTypeRepository.findByName(weather.getTypeName());
            if (weatherType.isPresent()) weather.setWeatherType(weatherType.get());
            else throw new NotFoundException(HttpStatus.NOT_FOUND, "The type of weather was not found",System.currentTimeMillis());

            weather.setRegion(regionRepository.findByCode(regionCode).get());
            weatherRepository.addWeather(weather);
        }
    }
    @Override
    public Double getTempByRegionId(int regionCode){
        if (weatherRepository.existsByRegionCode(regionCode)) {
            Optional<Double> temperature= weatherRepository.getTemperatureByRegionCodeAndDate(regionCode);
            if (temperature.isPresent())
                return temperature.get();
            throw new NotFoundException(HttpStatus.NOT_FOUND,"Temperature not found", System.currentTimeMillis());
        }
        throw new NotFoundException(HttpStatus.NOT_FOUND,"Region not found", System.currentTimeMillis());
    }
    @Override
    @Transactional
    public void delete(int regionCode) {
        if(weatherRepository.existsByRegionCode(regionCode))
            weatherRepository.deleteByRegionCode(regionCode);
        else throw new NotFoundException(HttpStatus.NOT_FOUND,"Region not found",System.currentTimeMillis());
    }
}
