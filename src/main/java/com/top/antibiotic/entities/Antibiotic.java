package com.top.antibiotic.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Antibiotic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long antibioticId;

    @Column(unique=true)
    @NotBlank(message = "Antibiotic name is required")
    private String name;

    @Column(unique=true)
    private String code;

    private Instant createdDate;
}
