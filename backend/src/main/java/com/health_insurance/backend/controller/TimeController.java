package com.health_insurance.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.health_insurance.backend.service.TimeSimulation;

@RestController
@RequestMapping("/")
public class TimeController {
    
    private final TimeSimulation timeSimulation;

    public TimeController(TimeSimulation timeSimulation) {
        this.timeSimulation = timeSimulation;
    }

    @GetMapping("/get-time")
    public String getCurrentTime() {
        return timeSimulation.getCurrentSimulationDate();
    }
}
