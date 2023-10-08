package com.gradle.boot.fintech.exceptions;

import com.gradle.boot.fintech.exceptions.base.GlobalWeatherException;
import com.gradle.boot.fintech.exceptions.base.WeatherApiException;
import com.gradle.boot.fintech.errors.ErrorDtoResponse;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.concurrent.TimeoutException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(WeatherApiException.class)
    public ResponseEntity<ErrorDtoResponse> handleWeatherApiException(WeatherApiException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDtoResponse(e.getStatus().value(), e.getErrorMessage()));
    }
    @ExceptionHandler(GlobalWeatherException.class)
    public ResponseEntity<ErrorDtoResponse> handleGlobalWeatherException(GlobalWeatherException e){
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorDtoResponse(e.getStatus().value(),e.getErrorMessage()));
    }
    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ErrorDtoResponse> handleRequestNotPermittedException(RequestNotPermitted e){
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ErrorDtoResponse(HttpStatus.TOO_MANY_REQUESTS.value(),
                        "Sorry...too many requests"));
    }
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorDtoResponse> handleRequestNotPermittedException(TimeoutException e){
        return ResponseEntity
                .status(HttpStatus.GATEWAY_TIMEOUT)
                .body(new ErrorDtoResponse(HttpStatus.GATEWAY_TIMEOUT.value(),
                        "Wait while we fulfill your request"));
    }
    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<ErrorDtoResponse> handleJsonMappingException(JsonMappingException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDtoResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
    }
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorDtoResponse> handleUnknownException(Throwable e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDtoResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Unknown error"));
    }
}
