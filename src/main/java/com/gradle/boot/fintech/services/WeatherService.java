package com.gradle.boot.fintech.services;

import com.gradle.boot.fintech.dao.WeatherDAO;
import com.gradle.boot.fintech.exceptions.RegionNotCreatedException;
import com.gradle.boot.fintech.exceptions.RegionNotFoundException;
import com.gradle.boot.fintech.exceptions.TemperatureNotFoundException;
import com.gradle.boot.fintech.models.Weather;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WeatherService {
    private final WeatherDAO weatherDAO;
    public void save(int regionId, Weather weather) {
        if (weatherDAO.isContainsByKey(regionId))
            throw new RegionNotCreatedException("Region already exists");
        weatherDAO.save(regionId,weather);
    }
    public void update(int regionId, Weather weather) {
        if (!weatherDAO.isContainsByKey(regionId)) throw new RegionNotFoundException();
        List<Weather> updatedWeathers = weatherDAO.getWeathersByRegionIdAndDate(regionId,weather.getDateTime().toLocalDate());
        if (updatedWeathers.size()>0)
            updatedWeathers.forEach(x->weatherDAO.update(x,weather));
        else weatherDAO.save(regionId,weather);
    }
    public Double getTempByRegionId(int regionId){
        if (weatherDAO.isContainsByKey(regionId)) {
            Optional<Double> temperature= weatherDAO.getTempByRegionIdToday(regionId);
            if (temperature.isPresent()) return temperature.get();
            throw new TemperatureNotFoundException();
        }
        throw new RegionNotFoundException();
    }
    public void delete(int regionId) {
         if(weatherDAO.isContainsByKey(regionId))
            weatherDAO.deleteByRegionId(regionId);
         else throw new RegionNotFoundException();
    }
}
