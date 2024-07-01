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
@Table(name = "CoverPlan", uniqueConstraints = @UniqueConstraint(name = "uq_persona_id", columnNames = "personaID"))
public class CoverPlan {

    public CoverPlan(BigInteger personaID, Status status){
        this.personaID = personaID;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coverPlanID;

    @Column(name = "personaID", nullable = false)
    private BigInteger personaID;

    @ManyToOne
    @JoinColumn(name = "statusID", nullable = false)
    private Status status;
}
