package com.gradle.boot.fintech.services.impl;

import com.gradle.boot.fintech.exceptions.NotCreatedException;
import com.gradle.boot.fintech.exceptions.NotFoundException;
import com.gradle.boot.fintech.models.Person;
import com.gradle.boot.fintech.models.Role;
import com.gradle.boot.fintech.enums.RoleEnum;
import com.gradle.boot.fintech.repositories.PersonRepository;
import com.gradle.boot.fintech.repositories.RoleRepository;
import com.gradle.boot.fintech.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(Person person) {
        if (personRepository.existsByUsername(person.getUsername()))
            throw new NotCreatedException(HttpStatus.BAD_REQUEST, "Username already exists", System.currentTimeMillis());
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        Role role = roleRepository.findByName(RoleEnum.ROLE_USER.toString())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Role not found", System.currentTimeMillis()));
        person.setRoles(Set.of(role));
        personRepository.save(person);
    }
}
