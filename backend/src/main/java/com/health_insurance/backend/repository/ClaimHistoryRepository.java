package com.health_insurance.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.health_insurance.backend.model.ClaimHistory;

public interface ClaimHistoryRepository extends JpaRepository<ClaimHistory, String> {
    
}
