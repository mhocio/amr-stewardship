package com.top.antibiotic.controllers;

import com.top.antibiotic.dto.FratRequest;
import com.top.antibiotic.dto.FratTableResponse;
import com.top.antibiotic.dto.SusceptibilityChartRequest;
import com.top.antibiotic.dto.SusceptibilityChartResponse;
import com.top.antibiotic.repository.*;
import com.top.antibiotic.servcice.ChartService;
import com.top.antibiotic.servcice.FratTableService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

@RestController
@RequestMapping("/api/chart")
@AllArgsConstructor
@Slf4j
public class ChartController {
    private final ChartService chartService;

    @GetMapping(path = "/{bacteria}/{startDate}/{endDate}")
    public ResponseEntity<SusceptibilityChartResponse> getChartData(
           @PathVariable String bacteria, @PathVariable Integer startDate, @PathVariable Integer endDate) throws ParseException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chartService.getSusceptibilityChartData(
                        SusceptibilityChartRequest.builder()
                                .bacteria(bacteria)
                                .startYear(startDate)
                                .endYear(endDate)
                                .build()));
    }
}
