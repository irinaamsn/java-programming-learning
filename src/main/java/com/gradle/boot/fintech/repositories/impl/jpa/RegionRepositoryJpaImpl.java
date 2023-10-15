package com.gradle.boot.fintech.repositories.impl.jpa;

import com.gradle.boot.fintech.models.Region;
import com.gradle.boot.fintech.repositories.RegionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository("regionJpaRepository")
public interface RegionRepositoryJpaImpl extends JpaRepository<Region,Long>, RegionRepository {
    Optional<Region> findByCode(int code);
}
