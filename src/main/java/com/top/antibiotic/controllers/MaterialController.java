package com.top.antibiotic.controllers;

import com.top.antibiotic.dto.MaterialDto;
import com.top.antibiotic.dto.WardDto;
import com.top.antibiotic.servcice.MaterialService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material")
@AllArgsConstructor
@Slf4j
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    public ResponseEntity<MaterialDto> createMaterial(@RequestBody MaterialDto materialDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(materialService.save(materialDto));
    }

    @GetMapping
    public ResponseEntity<List<MaterialDto>> getAllMaterials() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(materialService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialDto> getMaterial(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(materialService.getMaterial(id));
    }
}
