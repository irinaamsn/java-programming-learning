package com.gradle.boot.fintech.repositories.impl.jpa;

import com.gradle.boot.fintech.models.WeatherType;
import com.gradle.boot.fintech.repositories.WeatherTypeRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("weatherTypeJpaRepository")
public interface WeatherTypeRepositoryJpaImpl extends JpaRepository<WeatherType, Long>, WeatherTypeRepository {

    Optional<WeatherType> findByName(String name);

    boolean existsByName(String name);

    @Modifying
    default void addWeatherType(WeatherType weatherType) {
        this.save(weatherType);
    }
}
