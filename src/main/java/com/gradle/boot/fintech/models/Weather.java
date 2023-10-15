package com.gradle.boot.fintech.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Weather")
public class Weather {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "regionCode", nullable = false)
    private int regionCode;

    @Column(name = "regionName", nullable = false)
    private String regionName;

    @Column(name = "temperature", nullable = false)
    private Double temperature;

    @Column(name = "typeName", nullable = false)
    private String typeName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private WeatherType weatherType;

    public Weather(int regionCode, String regionName, Double temperature, String typeName, LocalDate date, LocalTime time) {
        this.regionCode = regionCode;
        this.regionName = regionName;
        this.temperature = temperature;
        this.typeName = typeName;
        this.date = date;
        this.time = time;
    }
}
