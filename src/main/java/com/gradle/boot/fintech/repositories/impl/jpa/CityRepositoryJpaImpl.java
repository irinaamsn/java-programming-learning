package com.gradle.boot.fintech.repositories.impl.jpa;

import com.gradle.boot.fintech.models.City;
import com.gradle.boot.fintech.repositories.CityRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("cityJpaRepository")
public interface CityRepositoryJpaImpl extends JpaRepository<City, Long>, CityRepository {
    Optional<City> findByName(String name);

    boolean existsByName(String name);

    @Modifying
    default void addCity(City city) {
        this.save(city);
    }
}
