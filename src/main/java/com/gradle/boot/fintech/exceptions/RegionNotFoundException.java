package com.gradle.boot.fintech.exceptions;

public class RegionNotFoundException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Region not found";
    }
}
