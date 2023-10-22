package com.gradle.boot.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherApiResponseDto {
    @JsonProperty(value = "location")
    private Location location;
    @JsonProperty(value = "current")
    private Current current;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Location {

        @JsonProperty(value = "name")
        private String cityName;

        @JsonProperty("localtime")
        private String dateTime;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Current {
        @JsonProperty("temp_c")
        private Double temperature;
        @JsonProperty("condition")
        private Condition condition;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Condition {
            @JsonProperty("text")
            private String typeName;
        }
    }
}
