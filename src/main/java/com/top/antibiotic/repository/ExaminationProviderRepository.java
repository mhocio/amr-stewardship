package com.top.antibiotic.repository;

import com.top.antibiotic.entities.ExaminationProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExaminationProviderRepository extends JpaRepository<ExaminationProvider, Long> {
    ExaminationProvider findByName(String name);
}
