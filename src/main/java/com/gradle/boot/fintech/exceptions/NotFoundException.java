package com.gradle.boot.fintech.exceptions;

import com.gradle.boot.fintech.exceptions.base.GlobalWeatherException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends GlobalWeatherException {
    public NotFoundException(HttpStatus status, String errorMessage, long timestamp) {
        super(status, errorMessage, timestamp);
    }
}
