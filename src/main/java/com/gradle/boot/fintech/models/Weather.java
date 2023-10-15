package com.gradle.boot.fintech.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Weather {
    @Schema(description = "Region ID")
    private int regionId;
    @Schema(description = "Name of the region", example = "Moscow oblast")
    private String regionName;
    @Schema(description = "Temperature")
    private double valueTemp;
    @Schema(description = "Date and time")
    private LocalDateTime dateTime;

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + regionId +
                ",region='" + regionName + '\'' +
                ",temperature=" + valueTemp +
                ",dateTime=" + dateTime +
                '}';
    }
}
