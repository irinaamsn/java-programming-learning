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

    @Column(name = "temperature", nullable = false)
    private Double temperature;

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

    public Weather(Double temperature, LocalDate date, LocalTime time) {
        this.temperature = temperature;
        this.date = date;
        this.time = time;
    }
}
