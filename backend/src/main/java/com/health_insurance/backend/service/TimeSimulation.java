package com.health_insurance.backend.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

@Service
public class TimeSimulation {
    
    private LocalDateTime simulationStartDate;
    private LocalDateTime realStartDate;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy|MM|dd");

    public TimeSimulation() {
        this.simulationStartDate = LocalDateTime.now();
        this.realStartDate = LocalDateTime.now();
    }

    public String getCurrentSimulationDate() {
        long realSecondsElapsed = ChronoUnit.SECONDS.between(realStartDate, LocalDateTime.now());
        long simulationDaysElapsed = realSecondsElapsed / 120;

        LocalDateTime currentSimulationDate = simulationStartDate.plusDays(simulationDaysElapsed);
        return formatter.format(currentSimulationDate);
    }
}
