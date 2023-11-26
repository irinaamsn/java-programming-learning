package com.gradle.boot.fintech.kafka;

import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.models.Weather;
import com.gradle.boot.fintech.services.WeatherApiClientService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WeatherConsumer {
    private final WeatherApiClientService weatherApiClientService;
    private static final Logger logger = LogManager.getLogger(WeatherConsumer.class);
    @Value(value = "${count.period}")
    private int sizeBundle;

    @KafkaListener(topics = "weather_topic"/*, groupId = "group1"*/)
    public void listen(ConsumerRecord<String, WeatherDto> record) {
        String cityName = record.key();
        WeatherDto weatherDto = record.value();
        weatherApiClientService.save(cityName, weatherDto);
        Optional<Double> movingAverage = calculateMovingAverage(cityName);
        if (movingAverage.isPresent())
            logger.info("City: {}, Moving Average: {}", cityName, movingAverage);
        else logger.info("Not enough records about the city's weather {}", cityName);
    }

    private Optional<Double> calculateMovingAverage(String cityName) {
        List<Weather> data = weatherApiClientService.getAllWeatherByCityName(cityName);
        if (data.size() >= sizeBundle) {
            Collections.reverse(data);
            List<Weather> weatherDto = data.stream().limit(sizeBundle).toList();
            double sum = weatherDto.stream().mapToDouble(Weather::getTemperature).sum();
            deleteWeathersOlderBundlePeriod(data, cityName);
            return Optional.of(sum / sizeBundle);
        }
        return Optional.empty();
    }

    private void deleteWeathersOlderBundlePeriod(List<Weather> list, String cityName) {
        list = list.stream()
                .skip(Long.parseLong(String.valueOf(sizeBundle)))
                .toList();
        if (!list.isEmpty()) {
            weatherApiClientService.deleteWeatherMoreBundle(list);
            logger.info("Records older than {} periods have been deleted, City: {}", sizeBundle, cityName);
        }
    }
}
