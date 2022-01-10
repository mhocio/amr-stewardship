package com.top.antibiotic.controllers;

import com.top.antibiotic.dto.WardDto;
import com.top.antibiotic.servcice.WardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ward")
@AllArgsConstructor
@Slf4j
public class WardController {

    private final WardService wardService;

    @PostMapping
    public ResponseEntity<WardDto> createWard(@RequestBody WardDto wardDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(wardService.save(wardDto));
    }

    @GetMapping
    public ResponseEntity<List<WardDto>> getAllWards() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wardService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WardDto> getWard(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wardService.getWard(id));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<WardDto> getWard(@PathVariable String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wardService.getWard(name));
    }
}