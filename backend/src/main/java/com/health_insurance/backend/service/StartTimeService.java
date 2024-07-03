package com.health_insurance.backend.service;

import com.health_insurance.backend.model.StartTime;
import com.health_insurance.backend.repository.StartTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Service
public class StartTimeService {

    private final StartTimeRepository startTimeRepository;

    public StartTimeService(StartTimeRepository startTimeRepository){
        this.startTimeRepository = startTimeRepository;
    }

    public Date getSimulationStartTime() {
        Optional<StartTime> startTime = startTimeRepository.findAll().stream().findFirst();
        if (startTime.isPresent()) {
            return startTime.get().getStartTime();
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public void saveStartTime(String strStartTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(strStartTime, formatter);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        startTimeRepository.save(new StartTime(Date.from(instant)));
    }
}
