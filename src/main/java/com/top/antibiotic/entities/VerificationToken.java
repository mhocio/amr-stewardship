package com.top.antibiotic.entities;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(fetch = LAZY)
    private User user;
    private Instant expiryDate;
}
