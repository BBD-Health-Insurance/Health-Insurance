package com.health_insurance.backend.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

@Service
public class TimeSimulation {
    
    private static final LocalDateTime SIMULATION_START_DATE = LocalDateTime.of(2024, 1, 1, 0, 0);
    private static final LocalDateTime REAL_START_DATE = LocalDateTime.of(2024, 7, 3, 15, 0);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy|MM|dd");

    public TimeSimulation() {
        
    }

    public String getCurrentSimulationDate() {
        long realSecondsElapsed = ChronoUnit.SECONDS.between(REAL_START_DATE, LocalDateTime.now());
        long simulationDaysElapsed = realSecondsElapsed / 120;

        LocalDateTime currentSimulationDate = calculateSimulationDate(simulationDaysElapsed);
        return formatter.format(currentSimulationDate);
    }

    private LocalDateTime calculateSimulationDate(long simulationDaysElapsed) {
        long totalDays = simulationDaysElapsed;
        
        int yearsElapsed = (int) (totalDays / 360);
        totalDays = totalDays % 360;
        
        int monthsElapsed = (int) (totalDays / 30);
        totalDays = totalDays % 30;

        int daysElapsed = (int) totalDays + 1; // Adding 1 because days are 1-based

        int newYear = SIMULATION_START_DATE.getYear() + yearsElapsed;
        int newMonth = SIMULATION_START_DATE.getMonthValue() + monthsElapsed;
        int newDay = daysElapsed;

        if (newMonth > 12) {
            newYear += newMonth / 12;
            newMonth = newMonth % 12;
        }

        return LocalDateTime.of(newYear, newMonth, newDay, 0, 0);
    }
}
