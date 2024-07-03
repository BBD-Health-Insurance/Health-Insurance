package com.health_insurance.backend.service;

import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import com.health_insurance.backend.dto.MakePaymentDto;

@Service
public class MakePayment {
    
    private String authToken;
    private String url = "http://api.commercialbank.projects.bbdgrad.com/transactions/create";

    public ResponseEntity<String> createTransaction(double amount, String debitRef, String creditRef) {
        RestTemplate restTemplate = new RestTemplate();

        MakePaymentDto.Transaction transaction = new MakePaymentDto.Transaction();
        transaction.setDebitAccountName("HealthCare");
        transaction.setCreditAccountName("HealthInsurance");
        transaction.setAmount(amount);
        transaction.setDebitRef(debitRef);
        transaction.setCreditRef(creditRef);

        MakePaymentDto requestBody = new MakePaymentDto();
        requestBody.setTransactions(List.of(transaction));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/json");
        // headers.setBearerAuth(authToken);

        HttpEntity<MakePaymentDto> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            return responseEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }
}
