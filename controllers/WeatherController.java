package com.gradle.boot.fintech.controllers;

import com.gradle.boot.fintech.models.Weather;
import com.gradle.boot.fintech.services.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name="Погода", description="Содержит CRUD методы, которые изменяют список с погодой и регионами")
@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;
    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
    @Operation(
            summary = "Получение температуры по региону",
            description = "Позволяет получить температуру по региону и текущей дате"
    )
    @ApiResponse(responseCode = "200", description = "Запрос был успешно выполнен")
    @ApiResponse(responseCode = "404", description = "Заданный регион не был найден/Температура не была найдена")
    @ApiResponse(responseCode = "500", description = "Внутрення ошибка сервера")
    @GetMapping("/{city}")
    public ResponseEntity<Double> getTemperatureByCity(@PathVariable(name = "city") @Parameter(description = "Идентификатор региона") int regionId) {
        return ResponseEntity.ok(weatherService.getTempByRegionId(regionId));
    }
    @Operation(
            summary = "Добавление нового региона",
            description = "Добавляет новый регион в список со всей информацией о погоде"
    )
    @ApiResponse(responseCode = "201", description = "Регион был успешно создан")
    @ApiResponse(responseCode = "400", description = "Заданный регион уже существует")
    @ApiResponse(responseCode = "500", description = "Внутрення ошибка сервера")
    @PostMapping("/{city}")
    public ResponseEntity<String> addRegion(@PathVariable(name = "city") @Parameter(description = "Идентификатор региона") int regionId,
                                            @RequestBody Weather weather) {
        weatherService.save(regionId,weather);
        return new ResponseEntity<>("Weather record with a new region added", HttpStatus.CREATED);
    }
    @Operation(
            summary = "Обновляет погоду по региону",
            description = "Обновляет данные о погоде по региону или добавляет новую запись"
    )
    @ApiResponse(responseCode = "200", description = "Запрос был успешно выполнен")
    @ApiResponse(responseCode = "404", description = "Заданный регион не был найден")
    @ApiResponse(responseCode = "500", description = "Внутрення ошибка сервера")
    @PutMapping("/{city}")
    public ResponseEntity<String> updateWeather(@PathVariable(name = "city") @Parameter(description = "Идентификатор региона") int regionId,
                                                @RequestBody Weather updatedWeather) {
        weatherService.update(regionId,updatedWeather);
        return ResponseEntity.ok("The weather in the region has been updated");
    }
    @Operation(
            summary = "Удаляет регион",
            description = "Удаляет регион со всеми записями о погоде"
    )
    @ApiResponse(responseCode = "200", description = "Запрос был успешно выполнен")
    @ApiResponse(responseCode = "404", description = "Заданный регион не был найден")
    @ApiResponse(responseCode = "500", description = "Внутрення ошибка сервера")
    @DeleteMapping("/{city}")
    public ResponseEntity<String> deleteRegion(@PathVariable(name = "city") @Parameter(description = "Идентификатор региона") int regionId) {
       weatherService.delete(regionId);
       return new ResponseEntity<>("The Region has been deleted",HttpStatus.OK);
    }
}
