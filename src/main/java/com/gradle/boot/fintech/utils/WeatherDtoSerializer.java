package com.gradle.boot.fintech.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gradle.boot.fintech.dto.WeatherDto;
import org.apache.kafka.common.serialization.Serializer;

public class WeatherDtoSerializer implements Serializer<WeatherDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, WeatherDto data) {
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
