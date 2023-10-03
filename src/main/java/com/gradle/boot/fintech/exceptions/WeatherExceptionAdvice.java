package com.gradle.boot.fintech.exceptions;

import com.gradle.boot.fintech.errors.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WeatherExceptionAdvice {
    @ExceptionHandler(RegionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(RegionNotFoundException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis()));
    }
    @ExceptionHandler(TemperatureNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(TemperatureNotFoundException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis()));
    }
    @ExceptionHandler(RegionNotCreatedException.class)
    public ResponseEntity<ErrorResponse> handleNotCreatedException(RegionNotCreatedException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis()));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleAllException(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", System.currentTimeMillis()));
    }
}
