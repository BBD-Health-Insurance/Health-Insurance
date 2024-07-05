package com.health_insurance.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.health_insurance.backend.repository.ClaimHistoryRepository;
import com.health_insurance.backend.repository.CoverPlanRepository;
import com.health_insurance.backend.repository.DependentRepository;
import com.health_insurance.backend.repository.MaxCoverRepository;
import com.health_insurance.backend.service.MakePayment;
import com.health_insurance.backend.dto.AddClaimHistoryDto;
import com.health_insurance.backend.dto.ClaimHistoryDto;

import com.health_insurance.backend.model.ClaimHistory;
import com.health_insurance.backend.model.CoverPlan;
import com.health_insurance.backend.model.Dependent;
import com.health_insurance.backend.model.MaxCover;
import com.health_insurance.backend.model.Status;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class ClaimHistoryController {

    @Autowired
    private ClaimHistoryRepository claimHistoryRepository;

    @Autowired
    private CoverPlanRepository coverPlanRepository;

    @Autowired
    private DependentRepository dependentRepository;

    @Autowired
    private MaxCoverRepository maxCoverRepository;

    private final MakePayment makePayment;

    @Autowired
    public ClaimHistoryController(MakePayment makePayment) {
        this.makePayment = makePayment;
    }

    @GetMapping("/list-claimhistory")
    public ResponseEntity<ClaimHistoryDto> getAllClaimHistories() {
        List<ClaimHistory> claimHistories = claimHistoryRepository.findAll();
        ClaimHistoryDto claimHistoryDto = new ClaimHistoryDto(claimHistories);
        return new ResponseEntity<>(claimHistoryDto, HttpStatus.OK);
    }

    @PostMapping("/pay-claim")
    public ResponseEntity<List<AddClaimHistoryDto>> payClaim(@RequestBody List<Map<String, Object>> request) {
        List<AddClaimHistoryDto> responseList = new ArrayList<>();

        try {
            Optional<MaxCover> maxCoverOptional = maxCoverRepository.findAll().stream().findFirst();
            if (!maxCoverOptional.isPresent()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            BigDecimal maxCoverAmount = maxCoverOptional.get().getMaxCover();

            for (Map<String, Object> claimData: request) {

                try {
                    String personaIDStr = (String) claimData.get("personaID");
                    BigInteger personaID = new BigInteger(personaIDStr);

                    Optional<CoverPlan> coverPlanOptional = coverPlanRepository.findByPersonaID(personaID);
                    if (!coverPlanOptional.isPresent()) {
                        Optional<Dependent> dependentOptional = dependentRepository.findByPersonaID(personaID);
                        if (!dependentOptional.isPresent()) {
                            responseList.add(new AddClaimHistoryDto("unsuccessful"));
                            continue;
                        }
                        Dependent dependent = dependentOptional.get();
                        coverPlanOptional = Optional.of(dependent.getCoverPlan());
                    }

                    CoverPlan coverPlan = coverPlanOptional.get();
                    Status coverPlanStatus = coverPlan.getStatus();
                    if (coverPlanStatus == null || !"Active".equals(coverPlanStatus.getName())) {
                        responseList.add(new AddClaimHistoryDto("unsuccessful"));
                        continue;
                    }

                    BigDecimal claimAmount = new BigDecimal(claimData.get("claimAmount").toString());

                    BigDecimal amountPaid;
                    if (claimAmount.compareTo(maxCoverAmount) <= 0) {
                        amountPaid = claimAmount;
                    } else {
                        amountPaid = maxCoverAmount;
                    }

                    String creditAccount = "health_care";

                    ResponseEntity<String> paymentResponse = makePayment.createTransaction(creditAccount, amountPaid.doubleValue(), personaIDStr, personaIDStr);

                    if (paymentResponse.getStatusCode() == HttpStatus.OK) {
                        try {
                            ClaimHistory claimHistory = new ClaimHistory(coverPlan, claimAmount, amountPaid, personaID);
                            claimHistoryRepository.save(claimHistory);

                            responseList.add(new AddClaimHistoryDto("successful"));
                        }
                        catch (Exception e){
                            responseList.add(new AddClaimHistoryDto("failed saving history"));
                        }
                    } else {
                        responseList.add(new AddClaimHistoryDto("unsuccessful - cannot make payment"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    responseList.add(new AddClaimHistoryDto("unsuccessful"));
                }
            }

            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
