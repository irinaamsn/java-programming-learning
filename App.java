package com.gradle.fintech;

import com.gradle.fintech.models.Weather;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {

        List<Weather> weathers = generateListWeather();
        //1. Рассчитать среднее значение температуры в регионах
        double avgTemperature=weathers.stream().collect( Collectors.averagingDouble(Weather::getValueTemp));
        assert avgTemperature==19.4;

        //2. Создать функцию для поиска регионов, больше какой-то определенной температуры
        double thresholdTemperature = 25.0;
        List<String> regionsWithTempAboveThreshold=weathers.stream()
                .filter(x->x.getValueTemp()>thresholdTemperature)
                .map(Weather::getRegionName)
                .toList();

        //3. Преобразовать список в Map, у которой ключ - уникальный идентификатор, значение - список со значениями температур
        Map<Integer,List<Double>> mapTemperaturesById=weathers.stream()
                .collect(Collectors.groupingBy(Weather::getRegionId,
                        Collectors.mapping(Weather::getValueTemp,Collectors.toList())));

        //4. Преобразовать список в Map, где ключ - температура, значение - коллекция объектов Weather, которым соответствует температура, указанная в ключе
        Map<Double,List<Weather>> mapWeathersByTemp=weathers.stream()
                .collect(Collectors.groupingBy(Weather::getValueTemp));
        System.out.println(avgTemperature);
    }
    public static List<Weather> generateListWeather(){
        List<Weather> weathers=new ArrayList<>();
        weathers.add(new Weather(50, "Московская область", 23.1, LocalDateTime.parse("2023-09-29T12:10:00")));
        weathers.add(new Weather(69, "Тверская область", 19.8, LocalDateTime.parse("2023-09-27T13:00:00")));
        weathers.add(new Weather(33, "Владимирская область", 26.1, LocalDateTime.parse("2023-08-01T12:40:00")));
        weathers.add(new Weather(50, "Московская область", 25.0, LocalDateTime.parse("2023-09-23T19:00:00")));
        weathers.add(new Weather(70, "Томская область", 4.8, LocalDateTime.parse("2023-09-21T12:00:00")));
        weathers.add(new Weather(50, "Московская область", 20.1, LocalDateTime.parse("2023-09-21T14:00:00")));
        weathers.add(new Weather(33, "Владимирская область", 19.8, LocalDateTime.parse("2023-09-21T14:00:00")));
        weathers.add(new Weather(41, "Камчатский край", 5.2, LocalDateTime.parse("2023-09-24T17:30:00")));
        weathers.add(new Weather(50, "Московская область", 25.9, LocalDateTime.parse("2023-08-09T11:00:00")));
        weathers.add(new Weather(50, "Московская область", 24.2, LocalDateTime.parse("2023-09-01T15:00:00")));
        return weathers;
    }
}
