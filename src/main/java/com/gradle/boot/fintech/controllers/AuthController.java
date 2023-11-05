package com.gradle.boot.fintech.controllers;

import com.gradle.boot.fintech.dto.PersonDto;
import com.gradle.boot.fintech.mappers.PersonMapper;
import com.gradle.boot.fintech.services.impl.PersonServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final PersonServiceImpl registrationService;
    private final PersonMapper personMapper;

    @PostMapping("/register")
    public ResponseEntity<String> addUser(@RequestBody @Valid PersonDto personDto) {
        registrationService.registerUser(personMapper.convertPersonDtoToPerson(personDto));
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }
}
