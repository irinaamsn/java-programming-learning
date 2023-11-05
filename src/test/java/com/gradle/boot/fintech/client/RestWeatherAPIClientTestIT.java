package com.gradle.boot.fintech.client;

import com.gradle.boot.fintech.dto.WeatherApiResponseDto;
import com.gradle.boot.fintech.dto.WeatherDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
class RestWeatherAPIClientTestIT {
    @Container
    private static final GenericContainer<?> wireMockContainer = new GenericContainer<>("rodolpheche/wiremock")
            .withExposedPorts(8080);

    @Autowired
    private RestWeatherAPIClient restWeatherAPIClient;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        String testCityName = "Moscow";
        configureFor(wireMockContainer.getContainerIpAddress(), wireMockContainer.getMappedPort(8080));
        stubFor(get(urlEqualTo("/current.json?key=" + System.getenv("API_KEY") + "&q=" + testCityName + "&lang=RU"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "  \"location\": {\n" +
                                "    \"name\": \"Moscow\",\n" +
                                "    \"localtime\": \"2023-01-01 10:00\"\n" +
                                "  },\n" +
                                "  \"current\": {\n" +
                                "    \"temp_c\": 0.0,\n" +
                                "    \"condition\": {\n" +
                                "      \"text\": \"testTypeName\"\n" +
                                "    }\n" +
                                "  }\n" +
                                "}")
                ));
    }

    @Test
    void getCurrentWeatherByRegionName() throws Exception {
        String testCityName = "Moscow";
        String wireMockUrl = "http://" + wireMockContainer.getContainerIpAddress() + ":" + wireMockContainer.getMappedPort(8080);
        String apiUrl = wireMockUrl + "/current.json?key=" + System.getenv("API_KEY") + "&q=" + testCityName + "&lang=RU";

        ResponseEntity<WeatherApiResponseDto> response =
                restTemplate.exchange(apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        Optional<WeatherDto> result = restWeatherAPIClient.getCurrentWeatherByCityName(testCityName);

        assertTrue(result.isPresent());
        assertEquals(response.getBody().getLocation().getCityName(), result.get().getCityName());
    }
}