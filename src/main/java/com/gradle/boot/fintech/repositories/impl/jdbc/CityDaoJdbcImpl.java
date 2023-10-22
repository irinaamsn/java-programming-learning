package com.gradle.boot.fintech.repositories.impl.jdbc;

import com.gradle.boot.fintech.models.City;
import com.gradle.boot.fintech.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Profile("cityJdbcRepository")
@RequiredArgsConstructor
public class CityDaoJdbcImpl implements CityRepository {
    private final JdbcTemplate jdbcTemplate;

    public Optional<City> findByName(String name) {
        var result = jdbcTemplate.query("SELECT * FROM City WHERE name=?",
                new Object[]{name},
                new BeanPropertyRowMapper<>(City.class));
        return result.stream().findFirst();
    }

    public void addCity(City city) {
        jdbcTemplate.update("INSERT INTO City(name) VALUES(?)", city.getName());
    }

    public boolean existsByName(String name) {
        return jdbcTemplate.query("SELECT * FROM City WHERE name=?",
                new Object[]{name},
                new BeanPropertyRowMapper<>(City.class)).size() > 0;
    }
}
