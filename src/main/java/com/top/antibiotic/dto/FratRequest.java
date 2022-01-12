package com.top.antibiotic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FratRequest {
    private Date startDate;
    private Date endDate;

    @NotBlank
    private String ward;
    @NotBlank
    private String material;
}
