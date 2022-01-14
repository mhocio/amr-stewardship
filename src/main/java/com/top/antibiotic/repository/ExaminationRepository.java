package com.top.antibiotic.repository;

import com.top.antibiotic.entities.Material;
import com.top.antibiotic.entities.Examination;
import com.top.antibiotic.entities.Patient;
import com.top.antibiotic.entities.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, Long> {
    boolean existsByNumber(Long number);
    Optional<Examination> findByNumber(Long number);
    List<Examination> findByPatient(Patient patient);
    List<Examination> findByMaterial(Material material);
    List<Examination> findByWard(Ward ward);
    List<Examination> findByWardAndMaterial(Ward ward, Material material);
}
