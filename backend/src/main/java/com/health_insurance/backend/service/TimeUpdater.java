package com.health_insurance.backend.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimeUpdater {
    
    private final TimeSimulation timeSimulation;

    public TimeUpdater(TimeSimulation timeSimulation) {
        this.timeSimulation = timeSimulation;
    }

    @Scheduled(fixedRate = 120000)
    public void updateSimulationTime() {
        String currentTime = timeSimulation.getCurrentSimulationDate();
        System.out.println(currentTime);
    }
}
