package com.gradle.boot.fintech.repositories.impl.jdbc;

import com.gradle.boot.fintech.models.City;
import com.gradle.boot.fintech.models.WeatherType;
import com.gradle.boot.fintech.repositories.WeatherTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Profile("weatherTypeJdbcRepository")
@RequiredArgsConstructor
public class WeatherTypeDaoJdbcImpl implements WeatherTypeRepository {
    private final JdbcTemplate jdbcTemplate;

    public Optional<WeatherType> findByName(String name) {
        var result = jdbcTemplate.query("SELECT * FROM Weather_Type WHERE name=?",
                new Object[]{name},
                new BeanPropertyRowMapper<>(WeatherType.class));
        return result.stream().findFirst();
    }

    public void addWeatherType(WeatherType weatherType) {
        jdbcTemplate.update("INSERT INTO Weather_Type(name) VALUES(?)", weatherType.getName());
    }

    public boolean existsByName(String name) {
        return jdbcTemplate.query("SELECT * FROM Weather_Type WHERE name=?",
                new Object[]{name},
                new BeanPropertyRowMapper<>(WeatherType.class)).size() > 0;
    }
}
