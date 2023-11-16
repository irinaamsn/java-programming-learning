package com.gradle.boot.fintech.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeatherDto {

    @Pattern(regexp = "^[а-яА-Я]+(?:[\\s-][а-яА-Я]+)*$", message = "Invalid city name")
    @Schema(description = "Название города")
    private String cityName;


    @Schema(description = "Температура")
    private double temperature;

    @Pattern(regexp = "^[а-яА-Я]+(?:[\\s-][а-яА-Я]+)*$", message = "Invalid weather type")
    @Schema(description = "Тип погоды")
    private String typeName;


    @Schema(description = "Дата")
    private LocalDate date;


    @Schema(description = "Время")
    private LocalTime time;

    public WeatherDto(String cityName, double temperature, String typeName) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.typeName = typeName;
    }
}
