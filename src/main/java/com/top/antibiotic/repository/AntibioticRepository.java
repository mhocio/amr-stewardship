package com.top.antibiotic.repository;

import com.top.antibiotic.entities.Antibiotic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AntibioticRepository extends JpaRepository<Antibiotic, Long> {
    Optional<Antibiotic> findByName(String name);
}