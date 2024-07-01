package com.health_insurance.backend.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimeUpdater {
    
    private final TimeSimulation timeSimulation;

    public TimeUpdater(TimeSimulation timeSimulation) {
        this.timeSimulation = timeSimulation;
    }

    @Scheduled(fixedRate = 1000)
    public void updateSimulationTime() {
        // used to update time
    }
}
