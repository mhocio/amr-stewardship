package com.top.antibiotic.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Antibiogram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long antibiogramId;
    private Instant createdDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "wardId", referencedColumnName = "wardId")
    private Ward ward;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "patientId", referencedColumnName = "patientId")
    private Patient patient;

    private String orderDate;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "examinationId", referencedColumnName = "examinationId")
    //            ,insertable = false, updatable = false)
    private Examination examination;
    private Long orderNumber;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "materialId", referencedColumnName = "materialId")
    private Material material;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "bacteriaId", referencedColumnName = "bacteriaId")
    private Bacteria bacteria;  // "izolacja"

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "antibioticId", referencedColumnName = "antibioticId")
    private Antibiotic antibiotic;

    private String susceptibility;
    private String mic;
    private boolean alert;
    private boolean patogen;
    private String growth;
    private boolean firstIsolate;
    private boolean hospitalInfection;

    private Long orderId;
    private Long testId;
    private Long isolationId;
    private String isolationCode;

    private Long isolationNum;
    private String result;
    private String dailyNumber;
    private String mode;
    private String pryw;
}