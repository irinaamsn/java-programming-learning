package com.gradle.boot.fintech.services.impl.weatherapi;

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
import com.gradle.boot.fintech.services.WeatherApiClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        if (!weatherTypeRepository.existsByName(weatherDto.getTypeName()))
            weatherTypeRepository.addWeatherType(
                    new WeatherType(weatherDto.getTypeName())
            );

        if (!cityRepository.existsByName(weatherDto.getCityName()))
            cityRepository.addCity(
                    new City(weatherDto.getCityName())
            );

        WeatherType weatherType = weatherTypeRepository.findByName(weatherDto.getTypeName()).get();
        City city = cityRepository.findByName(cityName).get();

        Weather weather = weatherMapper.toWeather(weatherDto);
        weather.setWeatherType(weatherType);
        weather.setCity(city);
        weatherRepository.addWeather(weather);
    }

    @Override
    public void deleteWeatherMoreBundle(List<Weather> list) {
        weatherRepository.deleteListWeatherByCityName(list);
    }

    @Override
    public List<Weather> getAllWeatherByCityName(String cityName) {
        return weatherRepository.getAllWeatherByCityName(cityName);
    }
}
