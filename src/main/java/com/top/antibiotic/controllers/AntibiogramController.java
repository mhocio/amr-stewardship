package com.top.antibiotic.controllers;

import com.top.antibiotic.dto.AntibiogramResponse;
import com.top.antibiotic.dto.AntibioticDto;
import com.top.antibiotic.entities.*;
import com.top.antibiotic.mapper.AntibiogramMapper;
import com.top.antibiotic.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/antibiogram")
@AllArgsConstructor
@Slf4j
public class AntibiogramController {

    private final AntibiogramRepository antibiogramRepository;
    private final AntibiogramMapper antibiogramMapper;

    private final WardRepository wardRepository;
    private final PatientRepository patientRepository;
    private final MaterialRepository materialRepository;
    private final BacteriaRepository bacteriaRepository;
    private final AntibioticRepository antibioticRepository;

    @GetMapping
    public ResponseEntity<List<AntibiogramResponse>> getAllAntibiograms() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(antibiogramRepository.findAll()
                        .stream()
                        .map(antibiogramMapper::mapAntibiogramToResponse)
                        .collect(toList()));
    }

    @PostMapping("/import")
    public void mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {

        List<Antibiogram> tempStudentList = new ArrayList<Antibiogram>();
        XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(2);

        for (Integer i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            log.info(i.toString());
            XSSFRow row = worksheet.getRow(i);
            Instant date = java.time.Instant.now();
            DataFormatter formatter = new DataFormatter();

            Antibiogram antibiogram = new Antibiogram();
            antibiogram.setCreatedDate(date);

            String s = formatter.formatCellValue(row.getCell(1));
            List<String> items = new ArrayList<>();
            for (int j = 0; j <= 26 ; j++) {
                items.add(formatter.formatCellValue(row.getCell(j)));
            }

            Ward ward = Ward.builder()
                    .name(items.get(0))
                    .createdDate(date)
                    .build();
            Ward savedWard = wardRepository.save(ward);
            antibiogram.setWard(savedWard);

            Patient patient = Patient.builder()
                    .firstName(items.get(1))
                    .secondName(items.get(2))
                    .pesel(items.get(3))
                    .createdDate(date)
                    .build();
            Patient savedPatient = patientRepository.save(patient);
            antibiogram.setPatient(savedPatient);

            antibiogram.setOrderDate(items.get(4));
            antibiogram.setOrderNumber(Long.parseLong(items.get(5)));

            Material material = Material.builder()
                    .name(items.get(6))
                    .createdDate(date)
                    .build();
            Material savedMaterial = materialRepository.save(material);
            antibiogram.setMaterial(savedMaterial);

            Bacteria bacteria = Bacteria.builder()
                    .name(items.get(7))
                    .subtype(items.get(8))
                    .createdDate(date)
                    .build();
            Bacteria savedBacteria = bacteriaRepository.save(bacteria);
            antibiogram.setBacteria(savedBacteria);

            Antibiotic antibiotic = Antibiotic.builder()
                    .name(items.get(9))
                    .code(items.get(22))
                    .createdDate(date)
                    .build();
            Antibiotic savedAntibiotic = antibioticRepository.save(antibiotic);
            antibiogram.setAntibiotic(savedAntibiotic);

            antibiogram.setSusceptibility(items.get(10));
            antibiogram.setMic(items.get(11));
            antibiogram.setAlert(Boolean.parseBoolean(items.get(12)));
            antibiogram.setPatogen(Boolean.parseBoolean(items.get(13)));
            antibiogram.setGrowth(items.get(14));
            antibiogram.setFirstIsolate(Boolean.parseBoolean(items.get(15)));
            antibiogram.setHospitalInfection(Boolean.parseBoolean(items.get(16)));
            antibiogram.setOrderId(Long.parseLong(items.get(17)));
            antibiogram.setTestId(Long.parseLong(items.get(18)));
            antibiogram.setIsolationId(Long.parseLong(items.get(19)));
            antibiogram.setIsolationCode(items.get(20));
            antibiogram.setIsolationNum(Long.parseLong(items.get(21)));
            antibiogram.setResult(items.get(23));
            antibiogram.setDailyNumber(items.get(24));
            antibiogram.setMode(items.get(25));
            antibiogram.setPryw(items.get(26));

            Antibiogram savedAntibiogram = antibiogramRepository.save(antibiogram);

            //Antibiogram tempStudent = new Antibiogram();
            //tempStudent.setId((int) row.getCell(0).getNumericCellValue());
            //tempStudent.setContent(row.getCell(1).getStringCellValue());
        }
    }
}
