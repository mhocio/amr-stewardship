package com.top.antibiotic.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Examination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examinationId;

    @Column(unique=true)
    private Long number;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "wardId", referencedColumnName = "wardId")
    private Ward ward;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "materialId", referencedColumnName = "materialId")
    private Material material;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "patientId", referencedColumnName = "patientId")
    private Patient patient;

    private Date orderDate;

    @OneToMany(fetch = LAZY, mappedBy = "examination")
    private List<Antibiogram> antibiograms;
}
