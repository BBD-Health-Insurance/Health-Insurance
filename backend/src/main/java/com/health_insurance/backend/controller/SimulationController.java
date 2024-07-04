package com.health_insurance.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health_insurance.backend.dto.ResponseStatusDto;
import com.health_insurance.backend.repository.*;
import com.health_insurance.backend.service.MaxCoverService;
import com.health_insurance.backend.service.StartTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.*;

@RestController
@RequestMapping("/")
public class SimulationController {
    @Autowired
    private CoverPlanRepository coverPlanRepository;

    @Autowired
    private DependentRepository dependentRepository;

    @Autowired
    private ClaimHistoryRepository claimHistoryRepository;

    @Autowired
    private StartTimeRepository startTimeRepository;

    @Autowired
    private MaxCoverService maxCoverService;

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

            String startTime = (String) request.get("startTime");

            if (startTime == null){
                reasons.add("StartTime not provided");
                return new ResponseEntity<>(
                        new ResponseStatusDto(ResponseStatusDto.responseUnsuccessful, reasons),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }

            StartTimeService startTimeService = new StartTimeService(startTimeRepository);
            startTimeService.saveStartTime(startTime);

            if (!setMaxCover()){
                reasons.add("Failed to set max cover");
            }

            reasons.add("Simulation " + action + "ed");
            return new ResponseEntity<>(new ResponseStatusDto(ResponseStatusDto.responseSuccessful, reasons), HttpStatus.OK);
        } catch (Exception e) {
            List<String> reason = new ArrayList<>();
            reason.add("failed ssl");
            return new ResponseEntity<>(new ResponseStatusDto(ResponseStatusDto.responseUnsuccessful, reason), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean setMaxCover() throws JsonProcessingException {
        String url = "https://api.zeus.projects.bbdgrad.com/health-insurance";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<JsonNode> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() != HttpStatus.OK){
            return false;
        }

        ObjectMapper itemMapper = new ObjectMapper();
        JsonNode itemRoot = itemMapper.readTree(response.getBody());
        BigInteger newMaxCover = BigInteger.valueOf(itemRoot.path("value").asInt());

        return maxCoverService.updateMaxCover(newMaxCover);
    }
}
