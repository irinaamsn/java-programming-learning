package com.gradle.boot.fintech.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gradle.boot.fintech.dto.WeatherDto;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class WeatherDtoDeserializer implements Deserializer<WeatherDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public WeatherDto deserialize(String topic, byte[] data) {
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.readValue(data, WeatherDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Deserialization error WeatherDto", e);
        }
    }
}
