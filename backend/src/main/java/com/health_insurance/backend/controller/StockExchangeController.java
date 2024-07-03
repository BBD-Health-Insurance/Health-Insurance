package com.health_insurance.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.health_insurance.backend.repository.StockExchangeRepository;
import com.health_insurance.backend.dto.StockExchangeDto;
import com.health_insurance.backend.model.StockExchange;

import java.util.List;

@RestController
@RequestMapping("/")
public class StockExchangeController {
    
    @Autowired
    private StockExchangeRepository stockExchangeRepository;

    @GetMapping("/list-stockinfo")
    public ResponseEntity<StockExchangeDto> getStockExchangeInfo() {
        List<StockExchange> stockExchanges = stockExchangeRepository.findAll();
        StockExchangeDto stockExchangeDto = new StockExchangeDto(stockExchanges);
        return new ResponseEntity<>(stockExchangeDto, HttpStatus.OK);
    }

    @PostMapping("/register-callback")
    public void registerBusiness() {
        System.out.println("Register business");
    }

    @PostMapping("/sell-stocks")
    public void sellStocks() {
        System.out.println("Sell stocks");
    }

    @PostMapping("/pay-dividends")
    public void payDividends() {
        System.out.println("Pay Dividends");
    }


}
