package com.gradle.boot.fintech.cache;

import com.gradle.boot.fintech.models.Weather;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WeatherCache extends LruCache<Weather> {

    public Optional<Weather> getWeather(String cityName) {
        if (containsKey(cityName))
            return Optional.ofNullable(get(cityName));
        return Optional.empty();
    }

    public void updateOrAddWeather(String cityName, Weather weather) {
        put(cityName, weather);
    }
}
