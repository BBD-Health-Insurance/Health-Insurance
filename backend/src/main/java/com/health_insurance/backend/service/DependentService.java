package com.health_insurance.backend.service;

import com.health_insurance.backend.model.Dependent;
import com.health_insurance.backend.repository.DependentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public class DependentService {
    private final DependentRepository dependentRepository;

    public DependentService(DependentRepository dependentRepository) {
        this.dependentRepository = dependentRepository;
    }

    @Transactional
    public void deleteDependentByPersonaID(BigInteger personaID) {
        Optional<Dependent> dependentOptional = dependentRepository.findByPersonaID(personaID);
        dependentOptional.ifPresent(dependentRepository::delete);
    }
}
