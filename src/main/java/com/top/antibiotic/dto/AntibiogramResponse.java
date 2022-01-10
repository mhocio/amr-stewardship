package com.top.antibiotic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AntibiogramResponse {
    private Long antibiogramId;
    private String bacteria;  // Bacteria.name
    private String antibiotic;  // Antibiotic.name
    private String material;  // Material.name
    private String ward;  // Ward.name
    private String pesel; // Patient.pesel
    private String orderDate;
    private String susceptibility;
}
