package com.top.antibiotic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SusceptibilityFRATChartRequest {
    @NotBlank
    private Integer startYear;
    @NotBlank
    private Integer endYear;
    @NotBlank
    private String ward;
    @NotBlank
    private String material;
}
