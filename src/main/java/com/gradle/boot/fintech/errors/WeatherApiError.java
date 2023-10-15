package com.gradle.boot.fintech.errors;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName(value = "error")
public class WeatherApiError {
    private int code;
    private String message;

}