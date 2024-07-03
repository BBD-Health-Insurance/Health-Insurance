package com.health_insurance.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MakePaymentDto {
    
    private List<Transaction> transactions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Transaction {
        private String debitAccountName;
        private String creditAccountName;
        private double amount;
        private String debitRef;
        private String creditRef;
    }
}
