package com.health_insurance.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.health_insurance.backend.model.CoverPlan;

import java.math.BigInteger;
import java.util.Optional;

public interface CoverPlanRepository extends JpaRepository<CoverPlan, Long> {
    Optional<CoverPlan> findByPersonaID(BigInteger personaID);
}
