package com.health_insurance.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.health_insurance.backend.repository.CoverPlanRepository;
import com.health_insurance.backend.repository.DependentRepository;
import com.health_insurance.backend.repository.StatusRepository;
import com.health_insurance.backend.model.CoverPlan;
import com.health_insurance.backend.model.Dependent;
import com.health_insurance.backend.model.Status;
import com.health_insurance.backend.dto.ResponseStatusDto;
import com.health_insurance.backend.dto.CoverPlanDto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class CoverPlanController {
    
    @Autowired
    private CoverPlanRepository coverPlanRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private DependentRepository dependentRepository;

    @GetMapping("/list-coverplan")
    public ResponseEntity<CoverPlanDto> getAllCoverPlans() {
        List<CoverPlan> coverPlan = coverPlanRepository.findAll();
        CoverPlanDto coverPlanDto = new CoverPlanDto(coverPlan);
        return new ResponseEntity<>(coverPlanDto, HttpStatus.OK);
    }

    @PostMapping("/create-plan")
    public ResponseEntity<List<ResponseStatusDto>> addCoverPlan(@RequestBody List<Map<String, Object>> request) {
        List<ResponseStatusDto> responseList = new ArrayList<>();

        try {
            Status activeStatus = statusRepository.findByName("Active");
            if (activeStatus == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            for (Map<String, Object> personaData: request) {
                try {
                    String personaIDStr = (String) personaData.get("personaID");
                    BigInteger personaID = new BigInteger(personaIDStr);
    
                    CoverPlan coverPlan = new CoverPlan();
                    coverPlan.setPersonaID(personaID);
                    coverPlan.setStatus(activeStatus);
    
                    CoverPlan savedCoverPlan = coverPlanRepository.save(coverPlan);
    
                    List<String> dependents = (List<String>) personaData.get("dependents");
                    if (dependents != null) {
                        for (String dependentIDStr: dependents) {
                            BigInteger dependentID = new BigInteger(dependentIDStr);
        
                            Dependent dependent = new Dependent();
                            dependent.setPersonaID(dependentID);
                            dependent.setCoverPlan(savedCoverPlan);
        
                            dependentRepository.save(dependent);
                        }
                    }

                    responseList.add(new ResponseStatusDto("successful"));
                } catch (Exception e) {
                    e.printStackTrace();

                    responseList.add(new ResponseStatusDto("unsuccessful"));
                }
                
            }

            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}