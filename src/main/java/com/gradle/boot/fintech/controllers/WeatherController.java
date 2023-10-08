package com.gradle.boot.fintech.controllers;

import com.gradle.boot.fintech.models.Weather;
import com.gradle.boot.fintech.services.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="Weather", description="Contains CRUD methods that modify the list with weather and regions")
@RestController
@RequestMapping("/api/weather/{city}")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;
    @Operation(
            summary = "Getting the temperature by region",
            description = "Allows you to get the temperature by region and the current date"
    )
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "The specified region was not found/The temperature was not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping
    public ResponseEntity<Double> getTemperatureByCity(@PathVariable(name = "city") @Parameter(description = "Region ID") int regionId) {
        return ResponseEntity.ok(weatherService.getTempByRegionId(regionId));
    }
    @Operation(
            summary = "Adding a new region",
            description = "Adds a new region to the list with all the weather information"
    )
    @ApiResponse(responseCode = "201", description = "Регион был успешно создан")
    @ApiResponse(responseCode = "400", description = "The specified region already exists")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping
    public ResponseEntity<String> addRegion(@PathVariable(name = "city") @Parameter(description = "Region ID") int regionId,
                                            @RequestBody Weather weather) {
        weatherService.save(regionId,weather);
        return new ResponseEntity<>("Weather record with a new region added", HttpStatus.CREATED);
    }
    @Operation(
            summary = "Updates the weather by region",
            description = "Updates weather data by region or adds a new record"
    )
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "The specified region was not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PutMapping
    public ResponseEntity<String> updateWeather(@PathVariable(name = "city") @Parameter(description = "Region ID") int regionId,
                                                @RequestBody Weather updatedWeather) {
        weatherService.update(regionId,updatedWeather);
        return ResponseEntity.ok("The weather in the region has been updated");
    }
    @Operation(
            summary = "Deletes a region",
            description = "Deletes a region with all weather records"
    )
    @ApiResponse(responseCode = "200", description = "The request was successfully executed")
    @ApiResponse(responseCode = "404", description = "The specified region was not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @DeleteMapping
    public ResponseEntity<String> deleteRegion(@PathVariable(name = "city") @Parameter(description = "Region ID") int regionId) {
        weatherService.delete(regionId);
        return new ResponseEntity<>("The Region has been deleted",HttpStatus.OK);
    }
}
