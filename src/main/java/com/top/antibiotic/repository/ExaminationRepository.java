package com.top.antibiotic.repository;

import com.top.antibiotic.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, Long> {
    boolean existsByNumber(Long number);
    boolean existsByNumberAndExaminationProvider(Long number, ExaminationProvider examinationProvider);
    Optional<Examination> findByNumber(Long number);
    Optional<Examination> findByNumberAndExaminationProvider(Long number, ExaminationProvider examinationProvider);
    List<Examination> findByPatient(Patient patient);
    List<Examination> findByMaterial(Material material);
    List<Examination> findByWard(Ward ward);
    List<Examination> findByWardAndMaterial(Ward ward, Material material);
    List<Examination> findByWardAndMaterialAndOrderDateBetween(Ward ward, Material material, Date start, Date end);
}
