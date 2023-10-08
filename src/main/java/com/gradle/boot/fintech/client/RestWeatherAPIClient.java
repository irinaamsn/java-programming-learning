package com.gradle.boot.fintech.client;

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

@Service
public class RestWeatherAPIClient implements EnvironmentAware {
    private final RestTemplate restTemplate;
    private final String baseUrl="http://api.weatherapi.com/v1";
    private String apiKey;

    @Autowired
    public RestWeatherAPIClient(@Qualifier("weatherApiRestTemplate") RestTemplate restTemplate) {
        this.restTemplate=restTemplate;
    }

    @RateLimiter(name="weatherApi")
    public String getCurrentWeatherByRegionName(String regionName) {
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/current.json")
                .queryParam("key", apiKey)
                .queryParam("q", regionName).build();

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {}
        );
        return response.getBody();
    }
    @Override
    public void setEnvironment(Environment environment) {
        this.apiKey = environment.getProperty("API_KEY");
    }
}
