package com.gradle.boot.fintech.exceptions.weatherapi;

import com.gradle.boot.fintech.exceptions.base.WeatherApiException;
import org.springframework.http.HttpStatus;

public class IncorrectUrlAddressException extends WeatherApiException {

    public IncorrectUrlAddressException(HttpStatus status, int codeError, String errorMessage, String errorDetail, long timestamp) {
        super(status, codeError, errorMessage, errorDetail, timestamp);
    }
}
