package com.gradle.boot.fintech.repositories.impl.jpa;

import com.gradle.boot.fintech.models.Weather;
import com.gradle.boot.fintech.repositories.WeatherRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@Profile("weatherJpaRepository")
public interface WeatherRepositoryJpaImpl extends JpaRepository<Weather, Long>, WeatherRepository {
    boolean existsByCityName(String cityName);

    @Query("select w.temperature from Weather w where w.cityName= :cityName and w.date= CURRENT_DATE")
    Optional<Double> getTemperatureByCityNameAndDate(@Param("cityName") String cityName);

    boolean existsByCityNameAndDate(String cityName, LocalDate date);

    @Modifying
    @Query("update Weather set temperature= :temperature, typeName= :typeName where cityName= :cityName and date= :date")
    void updateByCityName(@Param("cityName") String cityName, @Param("date") LocalDate date,
                          @Param("temperature") Double temperature, @Param("typeName") String typeName);

    @Modifying
    @Query("delete from Weather w where w.cityName= :cityName")
    void deleteByCityName(@Param("cityName") String cityName);

    @Modifying
    default void addWeather(Weather weather) {
        this.save(weather);
    }
}
