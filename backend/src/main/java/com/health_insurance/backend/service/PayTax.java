package com.health_insurance.backend.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class PayTax {

  private String taxIdUrl = "https://api.mers.projects.bbdgrad.com/api/taxpayer/business/getTaxId";
  private String taxAmountUrl = "http://api.mers.projects.bbdgrad.com/api/taxcalculator/calculate";
  private String payTaxUrl = "http://api.mers.projects.bbdgrad.com/api/taxpayment/createTaxInvoice";
  private String accountBalanceUrl = "http://api.commercialbank.projects.bbdgrad.com/account/balance";
  private String taxRateUrl = "http://api.zeus.projects.bbdgrad.com/tax-rate";

  public void payTax() {

    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.set("Content-Type", "application/json");

      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonNode = mapper.createObjectNode();
      ((ObjectNode)jsonNode).put("taxId", getTaxId());
      ((ObjectNode)jsonNode).put("taxType", "Income");
      ((ObjectNode)jsonNode).put("amount", getTaxAmount(getAccountBalance()));

      HttpEntity<JsonNode> entity = new HttpEntity<>(jsonNode, headers);

      ResponseEntity<String> response = restTemplate.exchange(payTaxUrl, HttpMethod.POST, entity, String.class);

      System.out.println(response);
    } catch (Exception e) {
      System.out.println(e);
    }
  };

  public void getTaxId() {

    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.set("Content-Type", "application/json");

      HttpEntity<String> entity = new HttpEntity<>(headers);

      String urlWithParams = taxIdUrl + "/health-insurance";

      ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.GET, entity, String.class);

      System.out.println(response);
    } catch (Exception e) {
      System.out.println(e);
    }
}

  public void getAccountBalance() {

    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.set("Content-Type", "application/json");
      headers.set("x-origin", "health_insurance");

      HttpEntity<String> entity = new HttpEntity<>(headers);

      ResponseEntity<String> response = restTemplate.exchange(accountBalanceUrl, HttpMethod.GET, entity, String.class);

      System.out.println(response);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void getTaxAmount(int profitAmount) {

    String amount = Integer.toString(profitAmount);

    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.set("Content-Type", "application/json");

      HttpEntity<String> entity = new HttpEntity<>(headers);

      String urlWithParams = taxAmountUrl + "?amount=" + amount + "&taxType=INCOME";

      ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.GET, entity, String.class);

      System.out.println(response);
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
