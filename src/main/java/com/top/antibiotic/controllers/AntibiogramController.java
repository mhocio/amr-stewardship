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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    private final AntibiogramRepository antibiogramRepository;

    private final WardRepository wardRepository;
    private final PatientRepository patientRepository;
    private final MaterialRepository materialRepository;
    private final BacteriaRepository bacteriaRepository;
    private final AntibioticRepository antibioticRepository;
    private final ExaminationRepository examinationRepository;

    private final WardService wardService;
    private final AntibiogramService antibiogramService;
    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<AntibiogramResponse>> getAllAntibiograms() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(antibiogramService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AntibiogramResponse> getById(Long id) {
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

    @PostMapping("/import")
    public void mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException, ParseException {

        List<Antibiogram> tempStudentList = new ArrayList<Antibiogram>();
        XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(2);

        for (Integer i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            log.info(i.toString());
            XSSFRow row = worksheet.getRow(i);
            Instant date = Instant.now();
            DataFormatter formatter = new DataFormatter();

            Antibiogram antibiogram = new Antibiogram();
            antibiogram.setCreatedDate(date);

            String s = formatter.formatCellValue(row.getCell(1));
            List<String> items = new ArrayList<>();
            for (int j = 0; j <= 26 ; j++) {
                items.add(formatter.formatCellValue(row.getCell(j)));
            }

            if (items.get(0).isEmpty()) {
                continue;
            }

            Ward ward;
            try {
                ward = wardRepository.findByName(items.get(0))
                        .orElseThrow(() -> new AntibioticsException("No ward found"));
            } catch (Exception e) {
                ward = wardRepository.save(Ward.builder()
                        .name(items.get(0))
                        .createdDate(date)
                        .build());
            }
            antibiogram.setWard(ward);

            Patient patient;
            try {
                patient = patientRepository.findByPesel(items.get(3))
                        .orElseThrow(() -> new AntibioticsException("No patient found"));
            } catch (Exception e) {
                patient = patientRepository.save(Patient.builder()
                        .firstName(items.get(1))
                        .secondName(items.get(2))
                        .pesel(items.get(3))
                        .createdDate(date)
                        .build());
            }
            antibiogram.setPatient(patient);

            String pattern = "MM/dd/yy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date dateInserted = simpleDateFormat.parse(items.get(4));
            antibiogram.setOrderDate(dateInserted);

            antibiogram.setOrderNumber(Long.parseLong(items.get(5)));

            Material material;
            try {
                material = materialRepository.findByName(items.get(6))
                        .orElseThrow(() -> new AntibioticsException("No material found"));
            } catch (Exception e) {
                material = materialRepository.save(Material.builder()
                        .name(items.get(6))
                        .createdDate(date)
                        .build());
            }
            antibiogram.setMaterial(material);

            Bacteria bacteria;
            try {
                bacteria = bacteriaRepository.findByName(items.get(7))
                        .orElseThrow(() -> new AntibioticsException("No bacteria found"));
            } catch (Exception e) {
                bacteria = bacteriaRepository.save(Bacteria.builder()
                        .name(items.get(7))
                        .subtype(items.get(8))
                        .createdDate(date)
                        .build());
            }
            antibiogram.setBacteria(bacteria);

            Antibiotic antibiotic;
            try {
                antibiotic = antibioticRepository.findByName(items.get(9))
                        .orElseThrow(() -> new AntibioticsException("No antibiotic found"));
            } catch (Exception e) {
                antibiotic = antibioticRepository.save(Antibiotic.builder()
                        .name(items.get(9))
                        .code(items.get(22))
                        .createdDate(date)
                        .build());
            }
            antibiogram.setAntibiotic(antibiotic);

            Examination examination;
            try {
                examination = examinationRepository.findByNumber(Long.parseLong(items.get(5)))
                        .orElseThrow(() -> new AntibioticsException("No Order found"));
            } catch (Exception e) {
                examination = examinationRepository.save(Examination.builder()
                        .number(Long.parseLong(items.get(5)))
                        .ward(ward)
                        .material(material)
                        .patient(patient)
                        .build());
            }
            antibiogram.setExamination(examination);

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
