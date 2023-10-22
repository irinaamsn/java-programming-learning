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

    @Transient
    private String cityName;

    @Column(name = "temperature", nullable = false)
    private Double temperature;

    @Transient
    private String typeName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private WeatherType weatherType;

    public Weather(String regionName, Double temperature, String typeName, LocalDate date, LocalTime time) {
        this.cityName = regionName;
        this.temperature = temperature;
        this.typeName = typeName;
        this.date = date;
        this.time = time;
    }
}
