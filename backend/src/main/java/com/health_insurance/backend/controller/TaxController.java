package com.health_insurance.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.health_insurance.backend.dto.ClaimHistoryDto;
import com.health_insurance.backend.model.ClaimHistory;
import com.health_insurance.backend.service.PayTax;

@RestController
public class TaxController {

    @GetMapping("/tax")
    public void getTax() {
      PayTax payTax = new PayTax();
      payTax.getTaxAmount("1000");
    }
}
