package com.gradle.boot.fintech.repositories.impl.jdbc;

import com.gradle.boot.fintech.models.WeatherType;
import com.gradle.boot.fintech.repositories.WeatherTypeRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("weatherTypeJdbcRepository")
public class WeatherTypeDaoJdbcImpl implements WeatherTypeRepository {
    private final JdbcTemplate jdbcTemplate;

    public WeatherTypeDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<WeatherType> findByName(String name) {
        var result = jdbcTemplate.query("SELECT * FROM Weather_Type WHERE name=?",
                new Object[]{name},
                new BeanPropertyRowMapper<>(WeatherType.class));
        return result.stream().findFirst();
    }
}
