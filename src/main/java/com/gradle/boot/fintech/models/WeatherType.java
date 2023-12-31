package com.gradle.boot.fintech.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "Weather_Type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherType {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    public WeatherType(String name) {
        this.name = name;
    }
}
