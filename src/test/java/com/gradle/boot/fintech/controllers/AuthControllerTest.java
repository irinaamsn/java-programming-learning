package com.gradle.boot.fintech.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradle.boot.fintech.dto.PersonDto;
import com.gradle.boot.fintech.dto.WeatherDto;
import com.gradle.boot.fintech.exceptions.NotCreatedException;
import com.gradle.boot.fintech.exceptions.NotFoundException;
import com.gradle.boot.fintech.services.PersonService;
import com.gradle.boot.fintech.services.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private PersonService personService;

    @Test
    void testAddNewUser() throws Exception {
        String username = "user";
        String password = "@User1234";
        String responseMessage = "User created";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .content(mapper.writeValueAsString(new PersonDto(username, password)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(responseMessage));
    }

    @Test
    void handleNotCreatedUser_testAddNewUser() throws Exception {
        String username = "user";
        String password = "@User1234";
        String expectedErrorMessage = "Username already exists";

        doThrow(new NotCreatedException(HttpStatus.BAD_REQUEST, "Username already exists", System.currentTimeMillis()))
                .when(personService).registerUser(any(PersonDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .content(mapper.writeValueAsString(new PersonDto(username, password)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }
    @Test
    void handleNotFoundRole_testAddNewUser() throws Exception {
        String username = "user";
        String password = "@User1234";
        String expectedErrorMessage = "Role not found";

        doThrow(new NotFoundException(HttpStatus.NOT_FOUND, "Role not found", System.currentTimeMillis()))
                .when(personService).registerUser(any(PersonDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .content(mapper.writeValueAsString(new PersonDto(username, password)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }
}