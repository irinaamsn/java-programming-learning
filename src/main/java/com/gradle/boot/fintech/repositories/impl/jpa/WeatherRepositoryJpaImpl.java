package com.gradle.boot.fintech.repositories.impl.jpa;

import com.gradle.boot.fintech.models.Weather;
import com.gradle.boot.fintech.repositories.WeatherRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository("weatherJpaRepository")
public interface WeatherRepositoryJpaImpl extends JpaRepository<Weather,Long>, WeatherRepository {
    boolean existsByRegionCode(int regionCode);
    @Query("select w.temperature from Weather w where w.regionCode= :regionCode and w.date= CURRENT_DATE")
    Optional<Double> getTemperatureByRegionCodeAndDate(@Param("regionCode")int regionCode);
    boolean existsByRegionCodeAndDate(int regionCode, LocalDate date);
    @Modifying
    @Query("update Weather set temperature= :temperature, typeName= :typeName where regionCode= :regionCode and date= :date")
    void updateByRegionCode(@Param("regionCode")int regionCode, @Param("date") LocalDate date,
                            @Param("temperature") Double temperature, @Param("typeName") String typeName);
    @Modifying
    @Query("DELETE FROM Weather WHERE regionCode= :regionCode")
    void deleteByRegionCode(@Param("regionCode")int regionCode);
    @Modifying
    default void addWeather(Weather weather) {
        this.save(weather);
    }
}
