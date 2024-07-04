package com.health_insurance.backend.service;

import java.util.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.hibernate.loader.internal.LoadAccessContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeSimulation {
    
    private static final LocalDateTime SIMULATION_START_DATE = LocalDateTime.of(2024, 1, 1, 0, 0);
    private static LocalDateTime REAL_START_DATE;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy|MM|dd");

    private final StartTimeService startTimeService;

    @Autowired
    public TimeSimulation(StartTimeService startTimeService) {
        this.startTimeService = startTimeService;
        Date startTime = startTimeService.getSimulationStartTime();
        Instant instant = startTime.toInstant();
        REAL_START_DATE = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public String getCurrentSimulationDate() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(REAL_START_DATE)) {
            return "0|0|0";
        }

        long realSecondsElapsed = ChronoUnit.SECONDS.between(REAL_START_DATE, now);
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

        int daysElapsed = (int) totalDays + 1;

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
