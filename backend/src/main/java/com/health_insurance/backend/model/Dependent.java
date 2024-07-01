package com.health_insurance.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Dependent")
public class Dependent {

    public Dependent(CoverPlan coverPlan, BigInteger personaID){
        this.coverPlan = coverPlan;
        this.personaID = personaID;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dependentID;

    @ManyToOne
    @JoinColumn(name = "coverPlanID", nullable = false)
    private CoverPlan coverPlan;

    @Column(name = "personaID", nullable = false)
    private BigInteger personaID;
}
