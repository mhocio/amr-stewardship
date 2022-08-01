package com.top.antibiotic.controllers;

import com.top.antibiotic.dto.AntibiogramResponse;
import com.top.antibiotic.dto.ImportDataInfoResponse;
import com.top.antibiotic.entities.ImportDataInfo;
import com.top.antibiotic.mapper.ImportDataInfoMapper;
import com.top.antibiotic.repository.ImportDataInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ImportDataInfo")
@AllArgsConstructor
@Slf4j
public class ImportDataInfoController {

    private final ImportDataInfoRepository importDataInfoRepository;
    private final ImportDataInfoMapper importDataInfoMapper;

    @GetMapping
    public ResponseEntity<List<ImportDataInfoResponse>> getAllAntibiograms() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(importDataInfoRepository.findAll()
                        .stream().map(importDataInfoMapper::mapImportDataInfoToResponse).collect(Collectors.toList())
                );
    }
}
