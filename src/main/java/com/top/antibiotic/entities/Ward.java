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
public class Ward {
    @Id
    @Column(unique=true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wardId;

    @Column(unique=true)
    @NotBlank(message = "Ward name is required")
    private String name;

    private Instant createdDate;
}

