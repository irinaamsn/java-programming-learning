package com.gradle.boot.fintech.mappers;

import com.gradle.boot.fintech.dto.PersonDto;
import com.gradle.boot.fintech.models.Person;

public interface PersonMapper {
    Person convertPersonDtoToPerson(PersonDto personDto);
}
