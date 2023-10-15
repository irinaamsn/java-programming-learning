package com.gradle.boot.fintech.repositories.impl.jdbc;

import com.gradle.boot.fintech.models.Weather;
import com.gradle.boot.fintech.repositories.WeatherRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
@Component("weatherJdbcRepository")
public class WeatherDaoJdbcImpl implements WeatherRepository {
    private final JdbcTemplate jdbcTemplate;
    public WeatherDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByRegionCode(int regionCode){
        return jdbcTemplate.query("SELECT * FROM Weather WHERE region_code=?",
                new Object[]{regionCode},
                new BeanPropertyRowMapper<>(Weather.class)).size()>0;
    }
    public Optional<Double> getTemperatureByRegionCodeAndDate(int regionCode){
        return jdbcTemplate.query("SELECT * FROM Weather w WHERE w.region_code=? AND w.date=CURRENT_DATE",
                new Object[]{regionCode},
                new BeanPropertyRowMapper<>(Weather.class)).stream().findFirst().map(Weather::getTemperature);
    }
    public boolean existsByRegionCodeAndDate(int regionCode, LocalDate date){
        return jdbcTemplate.query("SELECT * FROM Weather WHERE region_code=? AND date=?",
                new Object[]{regionCode,date},
                new BeanPropertyRowMapper<>(Weather.class)).size()>0;
    }
    public void addWeather(Weather weather) {
        jdbcTemplate.update("INSERT INTO Weather(region_code, region_name, type_name, temperature, date, time, region_id, type_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                weather.getRegionCode(), weather.getRegionName(), weather.getTypeName(), weather.getTemperature(), weather.getDate(),weather.getTime(),
                weather.getRegion().getId(), weather.getWeatherType().getId());
    }
    public void updateByRegionCode(int regionCode, LocalDate date, Double temperature,String typeName){//todo bad request
         jdbcTemplate.update("UPDATE Weather SET temperature=?, type_name=? WHERE region_code=? AND date=?",
               temperature, typeName, regionCode,date);
    }
    public void deleteByRegionCode(int regionCode) {
        jdbcTemplate.update("DELETE FROM Weather WHERE region_code=?", regionCode);
    }
}
