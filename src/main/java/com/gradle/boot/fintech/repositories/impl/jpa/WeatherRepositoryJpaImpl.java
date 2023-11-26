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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("weatherJpaRepository")
public interface WeatherRepositoryJpaImpl extends JpaRepository<Weather, UUID>, WeatherRepository {
    @Query("select w from Weather w where w.city.name = :cityName")
    List<Weather> getAllWeatherByCityName(String cityName);

    @Query("select w from Weather w where w.city.name= :cityName and w.date= CURRENT_DATE")
    Optional<Weather> getWeatherByCityName(@Param("cityName") String cityName);

    @Query("select case when COUNT(w) > 0 then true else false end from Weather w where w.city.name = :cityName")
    boolean existsByCityName(@Param("cityName") String cityName);

    @Query("select w.temperature from Weather w where w.city.name= :cityName and w.date= CURRENT_DATE")
    Optional<Double> getTemperatureByCityNameAndDate(@Param("cityName") String cityName);

    @Query("select case when COUNT(w) > 0 then true else false end from Weather w where w.city.name = :cityName AND w.date = :date")
    boolean existsByCityNameAndDate(@Param("cityName") String cityName, @Param("date") LocalDate date);

    @Modifying
    @Query("update Weather w set w.temperature= :temperature where w.city.name= :cityName and w.date= :date")
    void updateByCityName(@Param("cityName") String cityName, @Param("date") LocalDate date,
                          @Param("temperature") Double temperature);

    @Modifying
    @Query("delete from Weather w where w.city.name = :cityName")
    void deleteByCityName(@Param("cityName") String cityName);

    @Modifying
    default void deleteListWeatherByCityName(List<Weather> lists) {
        for (Weather weather : lists) {
            this.delete(weather);
        }
    }

    @Modifying
    default void addWeather(Weather weather) {
        this.save(weather);
    }
}
