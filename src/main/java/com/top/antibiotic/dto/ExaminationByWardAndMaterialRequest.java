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
public class ExaminationByWardAndMaterialRequest {
    @NotBlank
    private String wardName;
    @NotBlank
    private String materialName;
}
