package com.top.antibiotic.entities;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportDataInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ImportDataInfoId;

    private String status;

    private Instant createdDate;
    private String fileName;
    private String formatType;
    private Long numberOfLines;
    private Long numberOfFailedLines;
    private Integer sheetNumber;

    @OneToMany(fetch = LAZY, mappedBy = "importDataInfo")
    private List<ExcelRowErrorInfo> errors;
}
