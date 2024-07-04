package com.health_insurance.backend.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class PayTax {

  private String registerTaxUrl = "https://api.mers.projects.bbdgrad.com/api/taxpayer/business/register";
  private String taxAmountUrl = "https://api.mers.projects.bbdgrad.com/api/taxcalculator/calculate";
  private String payTaxUrl = "https://api.mers.projects.bbdgrad.com/api/taxpayment/createTaxInvoice";
  private String accountBalanceUrl = "https://api.commercialbank.projects.bbdgrad.com:443/account/balance";

  public String payTax() {

    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.set("Content-Type", "application/json");
      headers.set("X-origin", "health_insurance");

      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonNode = mapper.createObjectNode();
      ((ObjectNode)jsonNode).put("taxId", registerTax());
      ((ObjectNode)jsonNode).put("taxType", "Income");
      ((ObjectNode)jsonNode).put("amount", getTaxAmount(getAccountBalance()));

      HttpEntity<JsonNode> entity = new HttpEntity<>(jsonNode, headers);

      ResponseEntity<String> response = restTemplate.exchange(payTaxUrl, HttpMethod.POST, entity, String.class);

      if (response.getStatusCode() == HttpStatus.OK) {
        String responseBody = response.getBody();
        if (responseBody != null) {
            JsonNode responseJson = mapper.readTree(responseBody);
            String paymentId = responseJson.path("paymentId").asText();
            return "Tax payment is made with paymentId " + paymentId;
        }
    }

      System.out.println(response);
    } catch (Exception e) {
      System.out.println(e);
    }
    return null;
  };

  public String registerTax() {

    try {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-origin", "health_insurance");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.createObjectNode();
        ((ObjectNode) jsonNode).put("businessName", "health_insurance");

        HttpEntity<JsonNode> entity = new HttpEntity<>(jsonNode, headers);

        ResponseEntity<String> response = restTemplate.exchange(registerTaxUrl, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            if (responseBody != null) {
                JsonNode responseJson = mapper.readTree(responseBody);
                String taxId = responseJson.path("taxId").asText();
                return taxId;
            }
        }

        System.out.println(response);
    } catch (Exception e) {
        System.out.println(e);
    }
    // If any exception occurs or the response is not OK, return a default value
    return null;
  }

  public String getAccountBalance() {

    try {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-origin", "health_insurance");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(accountBalanceUrl, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            if (responseBody != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseJson = mapper.readTree(responseBody);
                JsonNode dataNode = responseJson.path("data");
                if (dataNode != null && dataNode.has("accountBalance")) {
                    String accountBalance = dataNode.path("accountBalance").asText();
                    return accountBalance;
                }
            }
        }

        System.out.println(response);
    } catch (Exception e) {
        System.out.println(e);
    }
    return null;
  }

  public String getTaxAmount(String profitAmount) {

    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.set("Content-Type", "application/json");
      headers.set("X-Origin", "health_insurance");

      HttpEntity<String> entity = new HttpEntity<>(headers);

      String urlWithParams = taxAmountUrl + "?amount=" + profitAmount + "&taxType=VAT";

      ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.GET, entity, String.class);

      if (response.getStatusCode() == HttpStatus.OK) {
        String responseBody = response.getBody();
        if (responseBody != null) {
          ObjectMapper mapper = new ObjectMapper();
          JsonNode responseJson = mapper.readTree(responseBody);
          String taxAmount = responseJson.path("taxAmount").asText();
          return taxAmount;
        }
      }

      System.out.println(response);
    } catch (Exception e) {
      System.out.println(e);
    }
    return null;
  };
}
