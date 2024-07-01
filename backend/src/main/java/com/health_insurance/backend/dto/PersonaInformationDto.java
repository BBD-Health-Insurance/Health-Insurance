package com.health_insurance.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonaInformationDto {

    private List<ResponseStatusDto> dependents;
    private List<ResponseStatusDto> newAdults;
    private List<ResponseStatusDto> deaths;
}
