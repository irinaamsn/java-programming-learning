package com.gradle.boot.fintech.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradle.boot.fintech.exceptions.JsonMappingException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtil {
    public static <T> T jsonToPojo(InputStream jsonData, Class<T> beanType) {
        try {
           ObjectMapper mapper=new ObjectMapper();
           mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
            return mapper.reader()
                    .forType(beanType)
                    .readValue(jsonData);
        } catch (IOException e) {
            throw new JsonMappingException(HttpStatus.BAD_REQUEST,
                                                    "Error during deserialization of JSON",
                                                    System.currentTimeMillis());
        }
    }
}
