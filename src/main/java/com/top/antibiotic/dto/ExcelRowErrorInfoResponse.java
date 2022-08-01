package com.top.antibiotic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelRowErrorInfoResponse {
    private String receivedValue;
    private String expectedType;
    private Integer cellNumber;
    private String message;
    private Long rowNumber;
}
