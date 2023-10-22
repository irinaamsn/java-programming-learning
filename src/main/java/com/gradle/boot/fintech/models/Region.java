package com.gradle.boot.fintech.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Region")
public class Region {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="code",unique = true,nullable = false)
    private int code;

    @Column(name="name",unique = true,nullable = false)
    private String name;

    public Region(int code, String name) {
        this.code = code;
        this.name = name;
    }
}

