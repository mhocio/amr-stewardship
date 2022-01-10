package com.top.antibiotic.repository;

import com.top.antibiotic.entities.Antibiogram;
import com.top.antibiotic.entities.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AntibiogramRepository extends JpaRepository<Antibiogram, Long> {
    Optional<Antibiogram> findByWard(Ward ward);
}