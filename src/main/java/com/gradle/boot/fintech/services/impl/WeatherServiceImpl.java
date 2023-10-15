package com.gradle.boot.fintech.services.impl;

import com.gradle.boot.fintech.dao.WeatherDao;
import com.gradle.boot.fintech.exceptions.NotFoundException;
import com.gradle.boot.fintech.exceptions.NotCreatedException;
import com.gradle.boot.fintech.models.Weather;
import com.gradle.boot.fintech.services.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final WeatherDao weatherDAO;
    @Override
    public void save(int regionId, Weather weather) {
        if (weatherDAO.isContainsByKey(regionId))
            throw new NotCreatedException(HttpStatus.BAD_REQUEST,"Region already exists", System.currentTimeMillis());
        weatherDAO.save(regionId,weather);
    }
    @Override
    public void update(int regionId, Weather weather) {
        if (!weatherDAO.isContainsByKey(regionId)) throw new NotFoundException(HttpStatus.NOT_FOUND,"Region not found", System.currentTimeMillis());
        List<Weather> updatedWeathers = weatherDAO.getWeathersByRegionIdAndDate(regionId,weather.getDateTime().toLocalDate());
        if (updatedWeathers.size()>0)
            updatedWeathers.forEach(x->weatherDAO.update(x,weather));
        else weatherDAO.save(regionId,weather);
    }
    @Override
    public Double getTempByRegionId(int regionId){
        if (weatherDAO.isContainsByKey(regionId)) {
            Optional<Double> temperature= weatherDAO.getTempByRegionIdToday(regionId);
            if (temperature.isPresent()) return temperature.get();
            throw new NotFoundException(HttpStatus.NOT_FOUND,"Temperature not found", System.currentTimeMillis());
        }
        throw new NotFoundException(HttpStatus.NOT_FOUND,"Region not found", System.currentTimeMillis());
    }
    @Override
    public void delete(int regionId) {
        if(weatherDAO.isContainsByKey(regionId))
            weatherDAO.deleteByRegionId(regionId);
        else throw new NotFoundException(HttpStatus.NOT_FOUND,"Region not found",System.currentTimeMillis());
    }
}
