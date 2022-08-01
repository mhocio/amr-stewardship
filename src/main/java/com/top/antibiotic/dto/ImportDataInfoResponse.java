package com.top.antibiotic.dto;

import com.top.antibiotic.entities.ExcelRowErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportDataInfoResponse {
    private String status;
    private String createdDate;
    private String fileName;
    private String formatType;
    private Long numberOfLines;
    private Long numberOfFailedLines;
    private Integer sheetNumber;
    private List<ExcelRowErrorInfoResponse> errors;
}
