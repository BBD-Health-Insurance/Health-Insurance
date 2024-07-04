package com.health_insurance.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.health_insurance.backend.model.StockExchange;
import com.health_insurance.backend.repository.StockExchangeRepository;

@Component
public class TimeUpdater {

    private final TimeSimulation timeSimulation;

    @Autowired
    private StockExchangeRepository stockExchangeRepository;

    public TimeUpdater(TimeSimulation timeSimulation) {
        this.timeSimulation = timeSimulation;
    }

    @Scheduled(fixedRate = 120000)
    public void updateSimulationTime() {
        String currentTime = timeSimulation.getCurrentSimulationDate();
        System.out.println(currentTime);

        String[] splitDate = currentTime.split("\\|");
        String dayStr = splitDate[splitDate.length - 1];
        int day = Integer.parseInt(dayStr);

        if (currentTime.equals("24|01|01")) {
            System.out.println("First day!");
            RegisterStockExchange();
            ListStocks();
        }

        if (day == 30) {
            PayDividends();
        }
    }
    
    public void RegisterStockExchange() {
        try {
            String callbackUrl = "http://health-insurance-api-web.eu-west-1.elasticbeanstalk.com/stock-register";
            String url = "https://api.mese.projects.bbdgrad.com/businesses?callbackUrl=" + callbackUrl;

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.createObjectNode();
            ((ObjectNode) jsonNode).put("name", "HealthInsurance");
            ((ObjectNode) jsonNode).put("bankAccount", "health_insurance");

            HttpEntity<JsonNode> entity = new HttpEntity<>(jsonNode, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            System.out.println(response);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void ListStocks() {
        try {
            List<StockExchange> stockExchange = stockExchangeRepository.findAll();

            if (stockExchange.size() > 0) {
                StockExchange data = stockExchange.get(0);

                String businessId = data.getBusinessID().toString();
                String tradingId = data.getTradingID().toString();

                String callbackUrl = "http://health-insurance-api-web.eu-west-1.elasticbeanstalk.com/stock-sell";
                String url = "https://api.mese.projects.bbdgrad.com/stocks/sell?callbackUrl=" + callbackUrl;

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "application/json");

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.createObjectNode();
                ((ObjectNode) jsonNode).put("sellerId", businessId);
                ((ObjectNode) jsonNode).put("companyId", tradingId);
                ((ObjectNode) jsonNode).put("quantity", 49999);

                HttpEntity<JsonNode> entity = new HttpEntity<>(jsonNode, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

                System.out.println(response);
            } else {
                System.out.println("Unable to retrieve stock data from DB");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void PayDividends() {
        try {
            List<StockExchange> stockExchange = stockExchangeRepository.findAll();
            if (stockExchange.size() > 0) {
                StockExchange data = stockExchange.get(0);

                String businessId = data.getBusinessID().toString();
                String tradingId = data.getTradingID().toString();

                String callbackUrl = "http://health-insurance-api-web.eu-west-1.elasticbeanstalk.com/stock-dividends";
                String url = "https://api.mese.projects.bbdgrad.com/dividends?callbackUrl=" + callbackUrl;

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "application/json");

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.createObjectNode();
                ((ObjectNode) jsonNode).put("businessId", businessId);
                ((ObjectNode) jsonNode).put("amount", 100);

                HttpEntity<JsonNode> entity = new HttpEntity<>(jsonNode, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

                System.out.println(response);
            } else {
                System.out.println("Unable to retrieve stock data from DB");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
