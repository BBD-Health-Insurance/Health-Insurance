package com.health_insurance.backend.repository;

import com.health_insurance.backend.model.StartTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StartTimeRepository extends JpaRepository<StartTime, Long> {
    
}
