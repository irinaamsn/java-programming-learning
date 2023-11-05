package com.gradle.boot.fintech.repositories;

import com.gradle.boot.fintech.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    Optional<Person> findByUsername(String username);
    boolean existsByUsername(String username);
}
