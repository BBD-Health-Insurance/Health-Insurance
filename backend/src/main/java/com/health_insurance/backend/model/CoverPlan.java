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
@Table(
        name = "CoverPlan",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_persona_id", columnNames = "personaID"),
                @UniqueConstraint(name = "uq_debit_order_id", columnNames = "debitOrderID")
        }
)
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

    @Column(name = "debitOrderID")
    private String debitOrderID;

    @ManyToOne
    @JoinColumn(name = "statusID", nullable = false)
    private Status status;
}
