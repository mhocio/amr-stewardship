package com.top.antibiotic.controllers;

import com.top.antibiotic.dto.BacteriaDto;
import com.top.antibiotic.dto.WardDto;
import com.top.antibiotic.servcice.BacteriaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bacteria")
@AllArgsConstructor
@Slf4j
public class BacteriaController {

    private final BacteriaService bacteriaService;

    @PostMapping
    public ResponseEntity<BacteriaDto> createBacteria(@RequestBody BacteriaDto bacteriaDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bacteriaService.save(bacteriaDto));
    }

    @GetMapping
    public ResponseEntity<List<BacteriaDto>> getAllBacterias() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bacteriaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BacteriaDto> getBacteria(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bacteriaService.getBacteria(id));
    }
}
