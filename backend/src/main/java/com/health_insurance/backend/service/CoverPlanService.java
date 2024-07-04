package com.health_insurance.backend.service;

import com.health_insurance.backend.model.CoverPlan;
import com.health_insurance.backend.repository.CoverPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public class CoverPlanService {
    @Autowired
    private CoverPlanRepository coverPlanRepository;

    public boolean updateDebitOrderID(String debitOrderID, BigInteger personaID) {
        Optional<CoverPlan> optionalCoverPlan = coverPlanRepository.findByPersonaID(personaID);

        if (optionalCoverPlan.isPresent()){
            CoverPlan coverPlan = optionalCoverPlan.get();
            coverPlan.setDebitOrderID(debitOrderID);
            coverPlanRepository.save(coverPlan);

            return true;
        }
        else {
            return false;
        }
    }
}
