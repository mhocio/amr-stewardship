package com.top.antibiotic.repository;

import com.top.antibiotic.entities.Bacteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BacteriaRepository extends JpaRepository<Bacteria, Long> {
    Optional<Bacteria> findByName(String name);
}