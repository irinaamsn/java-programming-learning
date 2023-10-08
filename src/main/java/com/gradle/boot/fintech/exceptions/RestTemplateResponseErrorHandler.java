package com.gradle.boot.fintech.exceptions;

import com.gradle.boot.fintech.exceptions.error.WeatherApiError;
import com.gradle.boot.fintech.exceptions.weatherapi.IncorrectApiKeyException;
import com.gradle.boot.fintech.exceptions.weatherapi.IncorrectQueryException;
import com.gradle.boot.fintech.exceptions.weatherapi.IncorrectUrlAddressException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import java.io.IOException;
import static com.gradle.boot.fintech.utils.JsonUtil.jsonToPojo;

@Component
public class RestTemplateResponseErrorHandler implements  ResponseErrorHandler  {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        WeatherApiError error=jsonToPojo(httpResponse.getBody(), WeatherApiError.class);
        if (httpResponse.getStatusCode() == HttpStatus.FORBIDDEN) {
            throw new IncorrectApiKeyException(HttpStatus.FORBIDDEN, error.getCode(),
                                                "Sorry...We are trying to solve the problem",
                                                error.getMessage(), System.currentTimeMillis());
        }
        if (httpResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
            if (error.getCode() == 1005) {
                throw new IncorrectUrlAddressException(HttpStatus.BAD_REQUEST, error.getCode(),
                                                        "URL address is invalid",
                                                        error.getMessage(), System.currentTimeMillis());
            }
            if (error.getCode() == 1006 || error.getCode() == 1003) {
                throw new IncorrectQueryException(HttpStatus.BAD_REQUEST, error.getCode(),
                                                    "Incorrect data",
                                                    error.getMessage(), System.currentTimeMillis());
            }
        }

    }
}