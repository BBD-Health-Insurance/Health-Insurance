package com.health_insurance.backend.controller;

import com.health_insurance.backend.dto.PersonaInformationDto;
import com.health_insurance.backend.dto.ResponseStatusDto;
import com.health_insurance.backend.model.CoverPlan;
import com.health_insurance.backend.model.Dependent;
import com.health_insurance.backend.repository.CoverPlanRepository;
import com.health_insurance.backend.repository.DependentRepository;
import com.health_insurance.backend.service.DependentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.health_insurance.backend.repository.StatusRepository;
import com.health_insurance.backend.model.Status;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class PersonaInformationController {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private CoverPlanRepository coverPlanRepository;

    @Autowired
    private DependentRepository dependentRepository;

    @Autowired
    private DependentService dependentService;

    @PostMapping("/persona-information")
    public ResponseEntity<PersonaInformationDto> updatePersonaInformation(@RequestBody Map<String, Object> request) {
        PersonaInformationDto response = new PersonaInformationDto();
        List<ResponseStatusDto> deaths = new ArrayList<>();
        List<ResponseStatusDto> newAdults = new ArrayList<>();
        List<ResponseStatusDto> dependents = new ArrayList<>();

        try {
            Status activeStatus = statusRepository.findByName("Active");
            Status deadStatus = statusRepository.findByName("Dead");
            Status inactiveStatus = statusRepository.findByName("Inactive");
            Status NoneStatus = statusRepository.findByName("None");

            if (activeStatus == null || deadStatus == null || inactiveStatus == null || NoneStatus == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Adding child to parent's plan
            List<Map<String, Object>> childrenList = (List<Map<String, Object>>) request.get("children");
            for (Map<String, Object> child : childrenList) {
                List<String> reasons = new ArrayList<>();

                BigInteger firstParentPersonaID = new BigInteger(String.valueOf(child.get("parentA")));
                BigInteger secondParentPersonaID = new BigInteger(String.valueOf(child.get("parentB")));
                BigInteger childPersonaID = new BigInteger(String.valueOf(child.get("child")));

                Optional<CoverPlan> firstParentCoverPlan = coverPlanRepository.findByPersonaID(firstParentPersonaID);
                Optional<CoverPlan> secondParentCoverPlan = coverPlanRepository.findByPersonaID(secondParentPersonaID);

                // Todo, confirm this logic later
                if (firstParentCoverPlan.isEmpty() && secondParentCoverPlan.isEmpty()){
                    reasons.add("Persona: " + firstParentPersonaID.toString() + " doesn't have insurance");
                    reasons.add("Persona: " + secondParentPersonaID.toString() + " doesn't have insurance");

                    dependents.add(new ResponseStatusDto(ResponseStatusDto.responseUnsuccessful, reasons));
                }
                else if (firstParentCoverPlan.isPresent() && secondParentCoverPlan.isPresent()) {
                    dependentRepository.save(new Dependent(firstParentCoverPlan.get(), childPersonaID));
                    dependentRepository.save(new Dependent(secondParentCoverPlan.get(), childPersonaID));

                    dependents.add(new ResponseStatusDto(ResponseStatusDto.responseSuccessful));
                }
                else if (firstParentCoverPlan.isPresent()) {
                    dependentRepository.save(new Dependent(firstParentCoverPlan.get(), childPersonaID));
                    reasons.add("Persona: " + secondParentPersonaID.toString() + " doesn't have insurance");

                    dependents.add(new ResponseStatusDto(ResponseStatusDto.responseSuccessful, reasons));
                }
                else {
                    dependentRepository.save(new Dependent(secondParentCoverPlan.get(), childPersonaID));
                    reasons.add("Persona: " + firstParentPersonaID.toString() + " doesn't have insurance");

                    dependents.add(new ResponseStatusDto(ResponseStatusDto.responseSuccessful, reasons));
                }
            }

            // Removing kids and upgrading their plans
            List<Integer> adults = (List<Integer>) request.get("adults");
            for (Integer adultPersonaID : adults){
                BigInteger personaID = new BigInteger(String.valueOf(adultPersonaID));

                // Remove their old plans
                dependentService.deleteDependentByPersonaID(personaID);

                // Sign then up to a new plan
                coverPlanRepository.save(new CoverPlan(personaID, activeStatus));

                newAdults.add(new ResponseStatusDto(ResponseStatusDto.responseSuccessful));
            }

            // Updating claims to dead, if they die.
            List<Map<String, Object>> deathsList = (List<Map<String, Object>>) request.get("deaths");
            for (Map<String, Object> death : deathsList) {
                List<String> reasons = new ArrayList<>();
                BigInteger personaID = new BigInteger(String.valueOf(death.get("deceased")));

                Optional<CoverPlan> coverPlan = coverPlanRepository.findByPersonaID(personaID);

                if (coverPlan.isPresent()){
                    CoverPlan plan = coverPlan.get();
                    plan.setStatus(deadStatus);
                    coverPlanRepository.save(plan);

                    deaths.add(new ResponseStatusDto(ResponseStatusDto.responseSuccessful));
                }
                else {
                    reasons.add("Persona: " + personaID.toString() + " doesn't have insurance");
                    deaths.add(new ResponseStatusDto(ResponseStatusDto.responseUnsuccessful, reasons));
                }
            }

            response.setDependents(dependents);
            response.setDeaths(deaths);
            response.setNewAdults(newAdults);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
