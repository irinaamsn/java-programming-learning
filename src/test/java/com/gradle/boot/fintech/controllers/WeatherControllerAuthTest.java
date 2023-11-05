package com.gradle.boot.fintech.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.models.Person;
import com.gradle.boot.fintech.models.enums.RoleEnum;
import com.gradle.boot.fintech.repositories.PersonRepository;
import com.gradle.boot.fintech.repositories.RoleRepository;
import com.gradle.boot.fintech.services.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Base64;
import java.util.Set;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class WeatherControllerAuthTest {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void authorizedUserWithRoleUserCanAccessGetTemperatureByCityName() throws Exception {
        mockMvc.perform(get("/api/weather/{city}", any(String.class)))
                .andExpect(authenticated().withUsername("user"))
                .andExpect(authenticated().withRoles("USER"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void authorizedUserWithRoleAdminCanAccessGetTemperatureByCityName() throws Exception {
        mockMvc.perform(get("/api/weather/{city}", any(String.class)))
                .andExpect(authenticated().withUsername("admin"))
                .andExpect(authenticated().withRoles("ADMIN"));
    }

    @Test
    void failsNoSecurityAccessGetTemperatureByCityName() throws Exception {
        mockMvc.perform(get("/api/weather/{city}", any(String.class)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void authorizedUserWithRoleAdminCanAccessAddNewCityWithWeather() throws Exception {
        mockMvc.perform(post("/api/weather/{city}", any(String.class))
                        .content(mapper.writeValueAsString(new WeatherDto()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(authenticated().withUsername("admin"))
                .andExpect(authenticated().withRoles("ADMIN"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void failsAuthorizationForUserWithRoleUserGetAccessAddNewCityWithWeather() throws Exception {
        mockMvc.perform(post("/api/weather/{city}", any(String.class))
                        .content(mapper.writeValueAsString(new WeatherDto()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void failsNoSecurityAccessAddNewCityWithWeather() throws Exception {
        mockMvc.perform(post("/api/weather/{city}", any(String.class)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void authorizedUserWithRoleAdminCanAccessUpdateWeatherByCityName() throws Exception {
        mockMvc.perform(put("/api/weather/{city}", any(String.class))
                        .content(mapper.writeValueAsString(new WeatherDto()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(authenticated().withUsername("admin"))
                .andExpect(authenticated().withRoles("ADMIN"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void failsAuthorizationForUserWithRoleUserGetAccessUpdateWeatherByCityName() throws Exception {
        mockMvc.perform(put("/api/weather/{city}", any(String.class))
                        .content(mapper.writeValueAsString(new WeatherDto()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void failsNoSecurityAccessUpdateWeatherByCityName() throws Exception {
        mockMvc.perform(put("/api/weather/{city}", any(String.class)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void authorizedUserWithRoleAdminCanAccessDeleteWeatherByCityName() throws Exception {

        mockMvc.perform(delete("/api/weather/{city}", any(String.class))
                        .content(mapper.writeValueAsString(new WeatherDto()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(authenticated().withUsername("admin"))
                .andExpect(authenticated().withRoles("ADMIN"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void failsAuthorizationForUserWithRoleUserGetAccessDeleteWeatherByCityName() throws Exception {

        mockMvc.perform(delete("/api/weather/{city}", any(String.class))
                        .content(mapper.writeValueAsString(new WeatherDto()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void failsNoSecurityAccessDeleteWeatherByCityName() throws Exception {
        mockMvc.perform(delete("/api/weather/{city}", any(String.class)))
                .andExpect(status().isUnauthorized());
    }
}