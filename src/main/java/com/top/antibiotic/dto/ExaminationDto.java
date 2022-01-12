package com.top.antibiotic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExaminationDto {
    private Long examinationId;
    private Long number;

    private String wardName;
    private Long wardId;

    private String materialName;
    private Long materialIdl;

    private String patientPesel;
    private Long patientId;
}
