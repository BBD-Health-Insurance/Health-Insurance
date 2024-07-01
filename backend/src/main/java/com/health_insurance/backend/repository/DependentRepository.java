package com.health_insurance.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.health_insurance.backend.model.Dependent;

import java.math.BigInteger;
import java.util.Optional;

public interface DependentRepository extends JpaRepository<Dependent, Long> {
    Optional<Dependent> findByPersonaID(BigInteger personaID);
}
