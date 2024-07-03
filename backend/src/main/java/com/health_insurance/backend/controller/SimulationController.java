package com.health_insurance.backend.controller;

import com.health_insurance.backend.dto.ResponseStatusDto;
import com.health_insurance.backend.repository.ClaimHistoryRepository;
import com.health_insurance.backend.repository.CoverPlanRepository;
import com.health_insurance.backend.repository.DependentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/")
public class SimulationController {
    @Autowired
    private CoverPlanRepository coverPlanRepository;

    @Autowired
    private DependentRepository dependentRepository;

    @Autowired
    private ClaimHistoryRepository claimHistoryRepository;

    @PostMapping("/control-simulation")
    public ResponseEntity<ResponseStatusDto> updatePersonaInformation(@RequestBody Map<String, Object> request) {
        try {
            List<String> reasons = new ArrayList<>();

            String action = (String) request.get("action");

            if (action == null){
                reasons.add("Action was not provided");
                return new ResponseEntity<>(
                        new ResponseStatusDto(ResponseStatusDto.responseUnsuccessful, reasons),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }

            claimHistoryRepository.deleteAll();
            dependentRepository.deleteAll();
            coverPlanRepository.deleteAll();

            if (action.toLowerCase(Locale.ROOT).equals("start")){
                // Todo, store startTime
                String startTime = (String) request.get("startTime");

                if (startTime == null){
                    reasons.add("StartTime not provided");
                    return new ResponseEntity<>(
                            new ResponseStatusDto(ResponseStatusDto.responseUnsuccessful, reasons),
                            HttpStatus.INTERNAL_SERVER_ERROR
                    );
                }
            }

            reasons.add("Simulation " + action + "ed");
            return new ResponseEntity<>(new ResponseStatusDto(ResponseStatusDto.responseSuccessful, reasons), HttpStatus.OK);
        } catch (Exception e) {
            List<String> reason = new ArrayList<>();
            reason.add("failed ssl");
            return new ResponseEntity<>(new ResponseStatusDto(ResponseStatusDto.responseUnsuccessful, reason), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
