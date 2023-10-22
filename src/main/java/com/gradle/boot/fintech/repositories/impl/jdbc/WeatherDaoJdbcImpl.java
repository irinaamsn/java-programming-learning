package com.gradle.boot.fintech.repositories.impl.jdbc;

import com.gradle.boot.fintech.models.Weather;
import com.gradle.boot.fintech.repositories.WeatherRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Profile("weatherJdbcRepository")
public class WeatherDaoJdbcImpl implements WeatherRepository {
    private final JdbcTemplate jdbcTemplate;

    public WeatherDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByCityName(String cityName) {
        return jdbcTemplate.query(
                "SELECT * FROM Weather w JOIN City c ON w.city_id=c.id WHERE c.name=?",
                new Object[]{cityName},
                new BeanPropertyRowMapper<>(Weather.class)).size() > 0;
    }

    public Optional<Double> getTemperatureByCityNameAndDate(String cityName) {
        return jdbcTemplate.query(
                "SELECT * FROM Weather w JOIN City c ON w.city_id=c.id WHERE c.name=? AND w.date=CURRENT_DATE",
                new Object[]{cityName},
                new BeanPropertyRowMapper<>(Weather.class)).stream().findFirst().map(Weather::getTemperature);
    }

    public boolean existsByCityNameAndDate(String cityName, LocalDate date) {
        return jdbcTemplate.query(
                "SELECT * FROM Weather w JOIN City c ON w.city_id=c.id WHERE c.name=? AND w.date=?",
                new Object[]{cityName, date},
                new BeanPropertyRowMapper<>(Weather.class)).size() > 0;
    }

    public void addWeather(Weather weather) {
        jdbcTemplate.update(
                "INSERT INTO Weather(temperature, date, time, city_id, type_id) VALUES(?, ?, ?, ?, ?)",
                weather.getTemperature(), weather.getDate(), weather.getTime(),
                weather.getCity().getId(), weather.getWeatherType().getId());
    }

    public void updateByCityName(String cityName, LocalDate date, Double temperature) {
        jdbcTemplate.update("UPDATE Weather w SET w.temperature=? " +
                        "WHERE w.city_id IN (SELECT id FROM City WHERE name =?) AND " +
                        "w.date=?",
                temperature, cityName, date);
    }

    public void deleteByCityName(String cityName) {
        jdbcTemplate.update("DELETE FROM Weather w " +
                "WHERE w.city_id IN (SELECT id FROM City WHERE name =?)", cityName);
    }
}
