package com.top.antibiotic.repository;

import com.top.antibiotic.entities.Antibiogram;
import com.top.antibiotic.entities.Bacteria;
import com.top.antibiotic.entities.Patient;
import com.top.antibiotic.entities.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AntibiogramRepository extends JpaRepository<Antibiogram, Long> {
    List<Antibiogram> findByPatient(Patient patient);
    List<Antibiogram> findByWard(Ward ward);
    List<Antibiogram> findByBacteria(Bacteria bacteria);
    List<Antibiogram> findByBacteriaAndOrderDateBetween(Bacteria bacteria, Date start, Date end);
}