package com.gradle.fintech;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Weather {
    private int regionId;
    private String regionName;
    private double valueTemp;
    private LocalDateTime dateTime;
    @Override
    public String toString() {
        return "Weather{" +
                "id=" + regionId +
                ", region='" + regionName + '\'' +
                ", temperature=" + valueTemp +
                ", dateTime=" + dateTime +
                '}';
    }
}
