package com.health_insurance.backend.service;

import com.health_insurance.backend.model.MaxCover;
import com.health_insurance.backend.repository.MaxCoverRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

@Service
public class MaxCoverService {
    private final MaxCoverRepository maxCoverRepository;

    public MaxCoverService(MaxCoverRepository maxCoverRepository) {
        this.maxCoverRepository = maxCoverRepository;
    }

    public boolean updateMaxCover(BigInteger newMaxCover) {
        Optional<MaxCover> optionalMaxCover = maxCoverRepository.findAll().stream().findFirst();
        if (optionalMaxCover.isEmpty()){
            return false;
        }

        MaxCover maxCover = optionalMaxCover.get();
        maxCover.setMaxCover(new BigDecimal(newMaxCover.toString()));
        maxCoverRepository.save(maxCover);
        return true;
    }
}
