package com.top.antibiotic.entities;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExcelRowErrorInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ExcelRowErrorInfoId;

    private String receivedValue;
    private String expectedType;
    private Integer cellNumber;
    @Column(columnDefinition = "TEXT")  //  TEXT – 64KB (65,535 characters)
    private String message;
    private Long rowNumber;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ImportDataInfoId", referencedColumnName = "ImportDataInfoId")
    private ImportDataInfo importDataInfo;
}
