package com.gradle.boot.fintech.controllers;

import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.services.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Weather", description = "Contains CRUD methods that modify the list with weather and cities")
@RestController
@RequestMapping("/api/weather/{city}")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @Operation(
            summary = "Getting the temperature by city",
            description = "Allows you to get the temperature by city and the current date"
    )
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "The specified city was not found/The temperature was not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping
    public ResponseEntity<Double> getTemperatureByCity(@PathVariable(name = "city") @Parameter(description = "Name of city") String cityName) {
        return ResponseEntity.ok(weatherService.getTempByRegionId(cityName));
    }

    @Operation(
            summary = "Adding a new city",
            description = "Adds a new city to the list with all the weather information"
    )
    @ApiResponse(responseCode = "201", description = "The city was successfully created")
    @ApiResponse(responseCode = "400", description = "The specified city already exists")
    @ApiResponse(responseCode = "404", description = "The specified city was not found/The type of weather was not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping
    public ResponseEntity<String> addCity(@PathVariable(name = "city") @Parameter(description = "Name of city") String cityName,
                                          @RequestBody WeatherDto weatherDto) {
        weatherService.save(cityName, weatherDto);
        return new ResponseEntity<>("Weather record with a new city added", HttpStatus.CREATED);
    }

    @Operation(
            summary = "Updates the weather by city",
            description = "Updates weather data by city or adds a new record"
    )
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "The specified city was not found/The type of weather was not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PutMapping
    public ResponseEntity<String> updateWeather(@PathVariable(name = "city") @Parameter(description = "Name of city") String cityName,
                                                @RequestBody WeatherDto weatherDto) {
        weatherService.update(cityName,weatherDto);
        return ResponseEntity.ok("The weather in the city has been updated");
    }

    @Operation(
            summary = "Deletes a city",
            description = "Deletes a city with all weather records"
    )
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "The specified city was not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @DeleteMapping
    public ResponseEntity<String> deleteCity(@PathVariable(name = "city") @Parameter(description = "Name of city") String cityName) {
        weatherService.delete(cityName);
        return new ResponseEntity<>("The City has been deleted", HttpStatus.OK);
    }
}
