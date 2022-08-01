package com.top.antibiotic.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialId;

    @Column(unique=true)
    @NotBlank(message = "Material name is required")
    private String name;

    private Instant createdDate;
}
