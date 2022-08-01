package com.top.antibiotic.controllers;

import com.top.antibiotic.dto.AntibiogramResponse;
import com.top.antibiotic.dto.AntibioticDto;
import com.top.antibiotic.entities.*;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.mapper.AntibiogramMapper;
import com.top.antibiotic.repository.*;
import com.top.antibiotic.servcice.AntibiogramService;
import com.top.antibiotic.servcice.PatientService;
import com.top.antibiotic.servcice.WardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/antibiogram")
@AllArgsConstructor
@Slf4j
public class AntibiogramController {

    private final PatientRepository patientRepository;

    private final AntibiogramService antibiogramService;

    @GetMapping
    public ResponseEntity<List<AntibiogramResponse>> getAllAntibiograms() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(antibiogramService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AntibiogramResponse> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(antibiogramService.getAntibiogramsById(id));
    }

    @GetMapping("/by-pesel/{pesel}")
    public ResponseEntity<List<AntibiogramResponse>> getAllByPesel(@PathVariable String pesel) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(antibiogramService.getAllByPatient(patientRepository.
                        findByPesel(pesel).orElseThrow(
                                () -> new AntibioticsException("No user with pesel: " + pesel))
                        )
                );
    }

    @PostMapping("/import/asseco")
    public ResponseEntity mapReapExcelDataToDB(@RequestParam("file") MultipartFile readExcelDataFile,
                                               @RequestParam(required = false) Optional<Integer> sheetNumber) throws IOException, ParseException {
        antibiogramService.saveFromFile(new ByteArrayResource(readExcelDataFile.getBytes()), sheetNumber);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/import/cgm")
    public ResponseEntity mapReadExcelDataToDB2(@RequestParam("file") MultipartFile readExcelDataFile,
                                                @RequestParam(required = false) Optional<Integer> sheetNumber) throws IOException, ParseException {
        antibiogramService.saveFromFileCGM(new ByteArrayResource(readExcelDataFile.getBytes()), sheetNumber);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/data")
    @Transactional
    public ResponseEntity deleteAllData() {
        antibiogramService.deleteAllData();

        return ResponseEntity.ok().build();
    }
}
