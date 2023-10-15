package com.gradle.boot.fintech.repositories;

import com.gradle.boot.fintech.models.Region;
import com.gradle.boot.fintech.models.Weather;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.Optional;

public interface RegionRepository {
    Optional<Region> findByCode(int code);
}
