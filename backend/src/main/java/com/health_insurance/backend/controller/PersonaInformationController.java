package com.health_insurance.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.health_insurance.backend.dto.PersonaInformationDto;
import com.health_insurance.backend.dto.ResponseStatusDto;
import com.health_insurance.backend.model.CoverPlan;
import com.health_insurance.backend.model.Dependent;
import com.health_insurance.backend.model.MaxCover;
import com.health_insurance.backend.repository.CoverPlanRepository;
import com.health_insurance.backend.repository.DependentRepository;
import com.health_insurance.backend.repository.MaxCoverRepository;
import com.health_insurance.backend.service.CoverPlanService;
import com.health_insurance.backend.service.DependentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.health_insurance.backend.repository.StatusRepository;
import com.health_insurance.backend.model.Status;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.*;

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

    @Autowired
    private MaxCoverRepository maxCoverRepository;

    @Autowired
    private CoverPlanService coverPlanService;

    @PostMapping("/persona-information")
    public ResponseEntity<PersonaInformationDto> updatePersonaInformation(@RequestBody Map<String, Object> request) {
        String commercialBankURL = "https://api.commercialbank.projects.bbdgrad.com";
        String createDebitOrder = "/debitOrders/create";
        String cancelDebitOrder = "/debitOrders/";
        String healthInsuranceName = "health_insurance";

        PersonaInformationDto response = new PersonaInformationDto();
        List<ResponseStatusDto> deaths = new ArrayList<>();
        List<ResponseStatusDto> newAdults = new ArrayList<>();
        List<ResponseStatusDto> dependents = new ArrayList<>();

        try {
            Status activeStatus = statusRepository.findByName("Active");
            Status deadStatus = statusRepository.findByName("Dead");
            Status inactiveStatus = statusRepository.findByName("Inactive");
            Status NoneStatus = statusRepository.findByName("None");
            Optional<MaxCover> maxCover = maxCoverRepository.findAll().stream().findFirst();

            if (activeStatus == null || deadStatus == null ||
                    inactiveStatus == null || NoneStatus == null || maxCover.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Adding child to parent's plan
            List<Map<String, Object>> childrenList = (List<Map<String, Object>>) request.get("children");
            if (childrenList != null) {
                for (Map<String, Object> child : childrenList) {
                    List<String> reasons = new ArrayList<>();
    
                    BigInteger parentPersonaID = new BigInteger(String.valueOf(child.get("parent")));
                    BigInteger childPersonaID = new BigInteger(String.valueOf(child.get("child")));
    
                    Optional<CoverPlan> parentCoverPlan = coverPlanRepository.findByPersonaID(parentPersonaID);
    
                    if (parentCoverPlan.isEmpty()) {
                        reasons.add("Persona: " + parentPersonaID.toString() + " doesn't have insurance");
                        dependents.add(new ResponseStatusDto(ResponseStatusDto.responseUnsuccessful, reasons));
                    }
                    else  {
                        dependentRepository.save(new Dependent(parentCoverPlan.get(), childPersonaID));    
                        dependents.add(new ResponseStatusDto(ResponseStatusDto.responseSuccessful));
                    }
                }
            }

            // Removing kids and upgrading their plans
            List<Integer> adults = (List<Integer>) request.get("adults");
            if (adults != null) {
                // Defining the JSON object that will hold our debit orders
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode root = mapper.createObjectNode();
                ArrayNode debitOrdersArray = mapper.createArrayNode();

                for (Integer adultPersonaID : adults){
                    BigInteger personaID = new BigInteger(String.valueOf(adultPersonaID));
    
                    // Remove their old plans
                    dependentService.deleteDependentByPersonaID(personaID);
    
                    // Sign then up to a new plan
                    coverPlanRepository.save(new CoverPlan(personaID, activeStatus));

                    // Add a row to the debit order array
                    ObjectNode debitOrder = mapper.createObjectNode();
                    debitOrder.put("debitAccountName", personaID.toString());
                    debitOrder.put("creditAccountName", healthInsuranceName);
                    debitOrder.put("debitRef", "Health Insurance Premium");
                    debitOrder.put("creditRef", "Premium for " + personaID);
                    debitOrder.put("amount", maxCover.get().getMaxCover());
                    debitOrdersArray.add(debitOrder);

                    newAdults.add(new ResponseStatusDto(ResponseStatusDto.responseSuccessful));
                }
                root.set("debitOrders", debitOrdersArray);

                //Setup Debit orders
                String url = commercialBankURL + createDebitOrder;
                ResponseEntity<String> commercialResponse = makeCallToCommercialBank(url, root, HttpMethod.POST);
                HttpStatusCode statusCode = commercialResponse.getStatusCode();

                List<String> reasons = new ArrayList<>();
                if (statusCode == HttpStatus.OK){
                    // Reading the response body and setting the debitOrderIDs
                    ObjectMapper itemMapper = new ObjectMapper();
                    JsonNode itemRoot = itemMapper.readTree(commercialResponse.getBody());
                    JsonNode itemsNode = itemRoot.path("data").path("items");

                    if (itemsNode.isArray()) {
                        int counter = 0;
                        Iterator<JsonNode> itemsIterator = itemsNode.elements();
                        while (itemsIterator.hasNext()) {
                            JsonNode itemNode = itemsIterator.next();

                            String id = itemNode.path("id").asText();
                            BigInteger personaID = new BigInteger(String.valueOf(adults.get(counter)));
                            if (!coverPlanService.updateDebitOrderID(id, personaID)){
                                newAdults.add(new ResponseStatusDto(ResponseStatusDto.responseInternalServerError, reasons));
                            }

                            counter += 1;
                        }
                    } else {
                        reasons.add("unknown structure");
                        newAdults.add(new ResponseStatusDto(ResponseStatusDto.responseUnsuccessful, reasons));
                    }
                }
                else {
                    reasons.add("Failed to setup debit orders");
                    newAdults.add(new ResponseStatusDto(ResponseStatusDto.responseUnsuccessful, reasons));
                }
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

                    // Updating the debit order
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode debitOrder = mapper.createObjectNode();
                    debitOrder.put("debitAccountName", personaID.toString());
                    debitOrder.put("creditAccountName", healthInsuranceName);
                    debitOrder.put("debitRef", "Health Insurance Premium");
                    debitOrder.put("creditRef", "Premium for " + personaID);
                    debitOrder.put("amount", 0);

                    String url = commercialBankURL + cancelDebitOrder + personaID;
                    ResponseEntity<String> commercialResponse = makeCallToCommercialBank(url, debitOrder, HttpMethod.PUT);
                    HttpStatusCode statusCode = commercialResponse.getStatusCode();

                    if (statusCode != HttpStatus.OK){
                        reasons.add("Failed to cancel debit order for " + personaID);
                        deaths.add(new ResponseStatusDto(ResponseStatusDto.responseUnsuccessful, reasons));
                    }
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

    private ResponseEntity<String> makeCallToCommercialBank(String url, ObjectNode body, HttpMethod method){
        //Setup Debit orders
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
         headers.set("X-Origin", "health_insurance");

        HttpEntity<JsonNode> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, method, entity, String.class);
    }
}
