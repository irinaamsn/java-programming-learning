package com.gradle.boot.fintech.kafka;

import com.gradle.boot.fintech.dto.WeatherDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeatherProducer {
    private final KafkaTemplate<String, WeatherDto> kafkaTemplate;

    @Value("${spring.kafka.topic}")
    private String kafkaTopic;

    public void sendWeatherData(String city, WeatherDto weatherDto) {
        kafkaTemplate.send(kafkaTopic, city, weatherDto);
    }
}
