package com.gradle.boot.fintech.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Weather {
    @Schema(description = "Идентификатор региона")
    private int regionId;
    @Schema(description = "Название региона", example = "Московская область")
    private String regionName;
    @Schema(description = "Температура")
    private double valueTemp;
    @Schema(description = "Дата и время")
    private LocalDateTime dateTime;
    public Weather(int regionId,String regionName, double valueTemp, LocalDateTime dateTime) {
        this.regionId=regionId;
        this.regionName = regionName;
        this.valueTemp = valueTemp;
        this.dateTime = dateTime;
    }

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
