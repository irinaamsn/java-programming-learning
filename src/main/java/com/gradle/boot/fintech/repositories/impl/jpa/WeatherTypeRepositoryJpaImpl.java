package com.gradle.boot.fintech.repositories.impl.jpa;

import com.gradle.boot.fintech.models.WeatherType;
import com.gradle.boot.fintech.repositories.WeatherTypeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("weatherTypeJpaRepository")
public interface WeatherTypeRepositoryJpaImpl extends JpaRepository<WeatherType, Long>, WeatherTypeRepository {
    Optional<WeatherType> findByName(String name);
}
