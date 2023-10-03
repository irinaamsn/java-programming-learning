package com.gradle.boot.fintech.exceptions;

public class TemperatureNotFoundException extends RuntimeException{

    @Override
    public String getMessage() {
        return "Temperature not found";
    }
}
