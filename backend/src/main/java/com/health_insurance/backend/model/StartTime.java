package com.health_insurance.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
@Table(name = "StartTime")
public class StartTime {
    
    @Id
    private Date startTime;
}
