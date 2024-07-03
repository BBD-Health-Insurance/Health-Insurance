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
@Table(name = "StockExchange")
public class StockExchange {

    @Id
    private BigInteger businessID;

    @Column(name = "TradingID")
    private BigInteger tradingID;
}
