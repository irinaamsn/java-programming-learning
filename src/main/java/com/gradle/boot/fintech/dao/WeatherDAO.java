package com.gradle.boot.fintech.dao;

import com.gradle.boot.fintech.models.Weather;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WeatherDAO {
    private final Map<Integer, List<Weather>> mapWeatherOfRegions;
    public WeatherDAO() {
        mapWeatherOfRegions =generateListWeather().stream().collect(Collectors.groupingBy(Weather::getRegionId));
    }
    public boolean isContainsByKey(int regionId){
        return mapWeatherOfRegions.containsKey(regionId);
    }
    public Optional<Double> getTempByRegionIdToday(int regionId){
        return mapWeatherOfRegions.get(regionId).stream()
                .filter(weather -> weather.getDateTime().toLocalDate().isEqual(LocalDate.now()))
                .map(Weather::getValueTemp)
                .findAny();
    }
    public List<Weather> getWeathersByRegionIdAndDate(int regionId, LocalDate date){
        return mapWeatherOfRegions.getOrDefault(regionId,List.of()).stream()
                .filter(x-> x.getDateTime().toLocalDate().isEqual(date))
                .toList();
    }
    public void save(int regionId, Weather weather) {
        if (isContainsByKey(regionId))
            mapWeatherOfRegions.get(regionId).add(weather);
        else mapWeatherOfRegions.put(regionId,List.of(weather));
    }
    public void update(Weather updatedWeather, Weather newWeather){
        updatedWeather.setValueTemp(newWeather.getValueTemp());
    }
    public void deleteByRegionId(int regionId) {
        mapWeatherOfRegions.remove(regionId);
    }
    public List<Weather> generateListWeather(){
        List<Weather> weathers=new ArrayList<>();
        weathers.add(new Weather(50, "Московская область", 23.1, LocalDateTime.parse("2023-10-01T12:10:00")));
        weathers.add(new Weather(69, "Тверская область", 19.2, LocalDateTime.parse("2023-09-27T13:00:00")));
        weathers.add(new Weather(33, "Владимирская область", 26.1, LocalDateTime.parse("2023-08-01T12:40:00")));
        weathers.add(new Weather(50, "Московская область", 25.0, LocalDateTime.parse("2023-09-23T19:00:00")));
        weathers.add(new Weather(70, "Томская область", 4.1, LocalDateTime.parse("2023-09-21T12:00:00")));
        weathers.add(new Weather(50, "Московская область", 20.1, LocalDateTime.parse("2023-09-21T14:00:00")));
        weathers.add(new Weather(33, "Владимирская область", 19.8, LocalDateTime.parse("2023-09-21T14:00:00")));
        weathers.add(new Weather(41, "Камчатский край", 6.2, LocalDateTime.parse("2023-09-24T17:30:00")));
        weathers.add(new Weather(50, "Московская область", 25.9, LocalDateTime.parse("2023-08-09T11:00:00")));
        weathers.add(new Weather(50, "Московская область", 24.2, LocalDateTime.parse("2023-09-01T15:00:00")));
        return weathers;
    }
}
