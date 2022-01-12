package com.top.antibiotic.controllers;

import com.top.antibiotic.dto.ExaminationDto;
import com.top.antibiotic.servcice.ExaminationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/examination")
@AllArgsConstructor
@Slf4j
public class ExaminationController {

    ExaminationService examinationService;

    @RequestMapping(path = "/by-ward-and-material/{wardName}/{materialName}", method = RequestMethod.GET)
    public ResponseEntity<List<ExaminationDto>> getByWardAndMaterial(
            @PathVariable String wardName, @PathVariable String materialName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(examinationService.getByByWardAndMaterial(
                        wardName, materialName));
    }
}
