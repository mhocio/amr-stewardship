package com.top.antibiotic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FRATChartsResponse {
    public SusceptibilityChartResponse barChart;
    public SusceptibilityChartResponse graphChart;
}
