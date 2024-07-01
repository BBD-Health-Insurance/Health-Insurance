package com.health_insurance.backend.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Persona")
public class Persona {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger personaID;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "salary", nullable = false)
    private BigDecimal salary;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timeOfBirth", nullable = false, updatable = false, columnDefinition = "default CURRENT_TIMESTAMP")
    private Date timeOfBirth;
}
