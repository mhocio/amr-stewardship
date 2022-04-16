package com.top.antibiotic.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examinationProviderId;

    @Column(unique=true)
    @NotNull(message = "Name is required")
    private String name;
}
