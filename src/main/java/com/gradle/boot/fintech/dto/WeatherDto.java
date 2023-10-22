package com.gradle.boot.fintech.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Код региона")
    private int regionCode;

    @Schema(description = "Название региона", example = "Московская область")
    private String regionName;

    @Schema(description = "Температура")
    private double temperature;

    @Schema(description = "Тип погоды")
    private String typeName;

    @Schema(description = "Дата")
    private LocalDate date;

    @Schema(description = "Время")
    private LocalTime time;

}
