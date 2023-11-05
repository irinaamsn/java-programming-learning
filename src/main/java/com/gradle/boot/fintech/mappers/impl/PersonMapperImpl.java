package com.gradle.boot.fintech.mappers.impl;

import com.gradle.boot.fintech.dto.PersonDto;
import com.gradle.boot.fintech.exceptions.MapperCovertException;
import com.gradle.boot.fintech.mappers.PersonMapper;
import com.gradle.boot.fintech.models.Person;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class PersonMapperImpl implements PersonMapper {
    @Override
    public Person convertPersonDtoToPerson(PersonDto personDto) {
        if (personDto == null)
            throw new MapperCovertException(HttpStatus.BAD_REQUEST, "Invalid data person", System.currentTimeMillis());
        Person person = new Person();
        person.setUsername(personDto.getUsername());
        person.setPassword(personDto.getPassword());
        return person;
    }
}
