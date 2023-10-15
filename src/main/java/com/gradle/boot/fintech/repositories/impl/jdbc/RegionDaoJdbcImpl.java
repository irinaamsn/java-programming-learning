package com.gradle.boot.fintech.repositories.impl.jdbc;

import com.gradle.boot.fintech.models.Region;
import com.gradle.boot.fintech.repositories.RegionRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("regionJdbcRepository")
public class RegionDaoJdbcImpl implements RegionRepository {
    private final JdbcTemplate jdbcTemplate;

    public RegionDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public Optional<Region> findByCode(int code){
        var result= jdbcTemplate.query("SELECT * FROM Region WHERE code=?",
                new Object[]{code},
                new BeanPropertyRowMapper<>(Region.class));
        return result.stream().findFirst();
    }
}
