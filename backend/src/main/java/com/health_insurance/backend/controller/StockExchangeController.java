package com.health_insurance.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.health_insurance.backend.repository.StockExchangeRepository;
import com.health_insurance.backend.service.MakePayment;
import com.health_insurance.backend.dto.StockExchangeDto;
import com.health_insurance.backend.model.StockExchange;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class StockExchangeController {
    
    @Autowired
    private StockExchangeRepository stockExchangeRepository;

    private final MakePayment makePayment;

    @Autowired
    public StockExchangeController(MakePayment makePayment) {
        this.makePayment = makePayment;
    }

    @GetMapping("/list-stockinfo")
    public ResponseEntity<StockExchangeDto> getStockExchangeInfo() {
        try {
            List<StockExchange> stockExchanges = stockExchangeRepository.findAll();
            StockExchangeDto stockExchangeDto = new StockExchangeDto(stockExchanges);
            return new ResponseEntity<>(stockExchangeDto, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/stock-register")
    public void registerBusiness(@RequestBody Map<String, String> body) {
        try {
            String idStr = body.get("id");
            String tradingIdStr = body.get("tradingId");
    
            BigInteger id = new BigInteger(idStr);
            BigInteger tradingId = new BigInteger(tradingIdStr);
    
            StockExchange stockExchange = new StockExchange();
            stockExchange.setBusinessID(id);
            stockExchange.setTradingID(tradingId);
    
            stockExchangeRepository.save(stockExchange);
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

    @PostMapping("/stock-sell")
    public void sellStocks(@RequestBody Map<String, String> body) {
        try {
            System.out.println(body);
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

    @PostMapping("/stock-dividends")
    public void payDividends(@RequestBody Map<String, String> body) {
        try {
            String referenceId = body.get("referenceId");
            String creditAccount = "stock-exchange";
            ResponseEntity<String> paymentResponse = makePayment.createTransaction(creditAccount, 100, referenceId, referenceId);
            if (paymentResponse.getStatusCode() == HttpStatus.OK) {
                System.out.println("Dividends paid");
            }
            else {
                System.out.println("Dividends could not be paid");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
