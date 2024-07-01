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
@Table(name = "ClaimHistory")
public class ClaimHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long claimHistoryID;

    @ManyToOne
    @JoinColumn(name = "coverPlanID", nullable = false)
    private CoverPlan coverPlan;

    @Column(name = "claimAmount", nullable = false, precision = 10, scale = 2)
    private BigDecimal claimAmount;

    @Column(name = "amountPaid", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @Column(name = "claimPersonaID", nullable = false)
    private BigInteger claimPersonaID;

    @Column(name = "timeStamp")
    private Date timeStamp;
}
