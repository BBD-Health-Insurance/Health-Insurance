package com.health_insurance.backend.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MaxCover")
public class MaxCover {

    public MaxCover(String maxCoverAmount){
        this.setMaxCover(new BigDecimal(maxCoverAmount));
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maxCoverID;

    @Column(name = "maxCover", precision = 10, scale = 2)
    private BigDecimal maxCover;
}
