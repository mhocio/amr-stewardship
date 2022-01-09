package com.top.antibiotic.controllers;

import com.top.antibiotic.dto.AntibioticDto;
import com.top.antibiotic.servcice.AntibioticService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antibiotic")
@AllArgsConstructor
@Slf4j
public class AntibioticController {

    private final AntibioticService antibioticService;

    @PostMapping
    public ResponseEntity<AntibioticDto> createAntibiotic(@RequestBody AntibioticDto antibioticDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(antibioticService.save(antibioticDto));
    }

    @GetMapping
    public ResponseEntity<List<AntibioticDto>> getAllAntibiotics() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(antibioticService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AntibioticDto> getAntibiotic(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(antibioticService.getAntibiotic(id));
    }
}
