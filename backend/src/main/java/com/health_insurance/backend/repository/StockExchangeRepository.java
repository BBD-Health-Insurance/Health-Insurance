package com.health_insurance.backend.repository;


import com.health_insurance.backend.model.StockExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface StockExchangeRepository extends JpaRepository<StockExchange, BigInteger> {

}