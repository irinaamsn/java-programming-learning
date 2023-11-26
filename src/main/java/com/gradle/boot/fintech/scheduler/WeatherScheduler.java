package com.gradle.boot.fintech.scheduler;

import com.gradle.boot.fintech.client.RestWeatherAPIClient;
import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.exceptions.NotFoundException;
import com.gradle.boot.fintech.kafka.WeatherProducer;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;

@Component
@RequiredArgsConstructor
public class WeatherScheduler {
    private final RestWeatherAPIClient weatherAPIClient;
    private final WeatherProducer weatherProducer;
    private final TaskScheduler taskScheduler;

    @Value("${list.weather.cities}")
    private final String[] CITIES;
    private int currentCityIndex = 0;
    private static final Logger logger = LogManager.getLogger(WeatherScheduler.class);

    @Scheduled(cron = "${cron.expression}")
    public void scheduleWeatherRequests() {
        logger.info("Scheduled method started ");
        ScheduledFuture<?>[] scheduledFuture = new ScheduledFuture<?>[1];
        scheduledFuture[0] = taskScheduler.scheduleAtFixedRate((() -> {
            if (currentCityIndex < CITIES.length) {
                String currentCity = CITIES[currentCityIndex];
                logger.info("{} called", currentCity);
                WeatherDto weatherDto = weatherAPIClient.getCurrentWeatherByCityName(currentCity)
                        .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Weather not found", System.currentTimeMillis()));
                weatherProducer.sendWeatherData(currentCity, weatherDto);
                currentCityIndex++;
            } else {
                currentCityIndex = 0;
                scheduledFuture[0].cancel(false);
                logger.info("Scheduled method stopped");
            }
        }), 1000);
    }
}
