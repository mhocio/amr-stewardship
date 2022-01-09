package com.top.antibiotic.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @NotBlank(message = "FirstName is required")
    private String firstName;

    @NotBlank(message = "SecondName is required")
    private String secondName;

    @Column(unique=true)
    @NotNull(message = "Pesel is required")
    private String pesel;

    private Instant createdDate;
}
