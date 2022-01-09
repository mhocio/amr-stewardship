package com.top.antibiotic.controllers;

import com.top.antibiotic.dto.MaterialDto;
import com.top.antibiotic.dto.PatientDto;
import com.top.antibiotic.servcice.PatientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@AllArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@RequestBody PatientDto patientDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patientService.save(patientDto));
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(patientService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatient(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(patientService.getPatient(id));
    }
}
