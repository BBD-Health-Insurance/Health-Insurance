package com.health_insurance.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStatusDto {

    public static String responseUnsuccessful = "unsuccessful";
    public static String responseSuccessful = "successful";

    private String status;
    private List<String> reason;

    public ResponseStatusDto(String status) {
        this.status = status;
        this.reason = new ArrayList<>();
    }
}
