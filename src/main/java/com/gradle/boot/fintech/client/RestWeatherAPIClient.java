package com.gradle.boot.fintech.client;

import com.gradle.boot.fintech.dto.WeatherApiResponseDto;
import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.mappers.WeatherMapper;
import com.gradle.boot.fintech.services.WeatherApiClientService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class RestWeatherAPIClient implements EnvironmentAware {
    private final RestTemplate restTemplate;
    private final static String baseUrl = "http://api.weatherapi.com/v1";
    private final WeatherMapper weatherMapper;
    private final WeatherApiClientService weatherApiClientService;
    private String apiKey;

    @Autowired
    public RestWeatherAPIClient(@Qualifier("weatherApiRestTemplate") RestTemplate restTemplate,
                                WeatherMapper weatherMapper,
                                WeatherApiClientService weatherApiClientService) {
        this.restTemplate = restTemplate;
        this.weatherMapper = weatherMapper;
        this.weatherApiClientService = weatherApiClientService;
    }

    @RateLimiter(name = "weatherApi")
    public Optional<WeatherDto> getCurrentWeatherByRegionName(String regionName) {
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/current.json")
                .queryParam("key", apiKey)
                .queryParam("q", regionName)
                .queryParam("lang", "RU").build();

        ResponseEntity<WeatherApiResponseDto> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        WeatherDto weatherDto = weatherMapper.ToWeatherDto(response.getBody());
        weatherApiClientService.save(weatherDto.getCityName(), weatherDto);
        return Optional.ofNullable(weatherDto);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.apiKey = environment.getProperty("API_KEY");
    }
}
