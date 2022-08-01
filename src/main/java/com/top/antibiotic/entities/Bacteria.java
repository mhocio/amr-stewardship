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
public class Bacteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bacteriaId;

    @Column(unique=true)
    @NotBlank(message = "Bacteria name is required")
    private String name;

    private String subtype;

    private Instant createdDate;
}
