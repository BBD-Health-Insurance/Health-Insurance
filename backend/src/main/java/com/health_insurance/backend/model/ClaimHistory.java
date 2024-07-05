package com.health_insurance.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
@Table(name = "ClaimHistory")
public class ClaimHistory {
    public ClaimHistory(CoverPlan coverPlan, BigDecimal claimAmount, BigDecimal amountPaid, BigInteger personaID){
        this.claimHistoryID = UUID.randomUUID().toString();
        this.coverPlan = coverPlan;
        this.claimAmount = claimAmount;
        this.amountPaid = amountPaid;
        this.claimPersonaID = personaID;
        this.timeStamp = new Date();
    }

    @Id
    @Column(name = "claimHistoryID", nullable = false, length = 36)
    private String claimHistoryID;

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
