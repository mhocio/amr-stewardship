package com.top.antibiotic.servcice;

import com.monitorjbl.xlsx.StreamingReader;
import com.top.antibiotic.data.AntibiogramRow;
import com.top.antibiotic.dto.AntibiogramResponse;
import com.top.antibiotic.entities.*;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.mapper.AntibiogramMapper;
import com.top.antibiotic.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AntibiogramService {
    @PersistenceContext
    private EntityManager em;
    // TODO
    private final WardRepository wardRepository;
    private final PatientRepository patientRepository;
    private final MaterialRepository materialRepository;
    private final BacteriaRepository bacteriaRepository;
    private final AntibioticRepository antibioticRepository;
    private final ExaminationRepository examinationRepository;

    private final AntibiogramRepository antibiogramRepository;
    private final AntibiogramMapper antibiogramMapper;

    @Transactional(readOnly = true)
    public List<AntibiogramResponse> getAll() {
        return antibiogramRepository.findAll()
                .stream()
                .map(antibiogramMapper::mapAntibiogramToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AntibiogramResponse getAntibiogramsById(Long id) {
        Antibiogram antibiogram = antibiogramRepository.findById(id)
                .orElseThrow(() ->new AntibioticsException("No Antibiogram with id: " + id.toString()));

        return antibiogramMapper.mapAntibiogramToResponse(antibiogram);
    }

    @Transactional(readOnly = true)
    public List<AntibiogramResponse> getAllByPatient(Patient patient) {
        return antibiogramRepository.findByPatient(patient)
                .stream()
                .map(antibiogramMapper::mapAntibiogramToResponse)
                .collect(Collectors.toList());
    }

    Boolean parseStringToBoolean(String s) {
        if (s.startsWith("T") || s.startsWith("1") || s.startsWith("t"))
            return true;
        return false;
    }

    @Transactional()
    public void saveFromFile(MultipartFile reapExcelDataFile) throws IOException, ParseException {

        Workbook workbook = StreamingReader.builder().rowCacheSize(100) // number of rows to keep in memory
                .bufferSize(4096)
                .open(reapExcelDataFile.getInputStream()); // InputStream or File for XLSX file (required)

        Iterator<Row> rowIterator = workbook.getSheetAt(2).rowIterator();

        // whether we insert this new Examination number this time
        Set<Long> newExaminationNumbers = new HashSet<Long>();

        List<Antibiogram> toInsert = new ArrayList<>();

        int rowNo = -1;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            rowNo = rowNo + 1;
            if (rowNo == 0) {
                continue;
            }

            Instant date = Instant.now();

            Antibiogram antibiogram = new Antibiogram();
            antibiogram.setCreatedDate(date);

            List<String> items = new ArrayList<>();

            int cellIter = 0;
            for (Cell c : row) {
                if (cellIter == c.getColumnIndex()) {
                    items.add(c.getStringCellValue());
                } else {
                    for (int ii = cellIter; ii < c.getColumnIndex(); ii++)
                        items.add("");
                    items.add(c.getStringCellValue());
                    cellIter = c.getColumnIndex();
                }
                cellIter += 1;
                //log.info(String.valueOf(c.getColumnIndex()));
            }

            if (items.size() == 26) {
                items.add("");
            }

            if (items.size() < 27) {
                continue;
            }

            // do not proceed if sb previously pushed this file or file with this Examination
            if (!examinationRepository.existsByNumber(Long.parseLong(items.get(5)))) {
                newExaminationNumbers.add(Long.parseLong(items.get(5)));
            }
            if (!newExaminationNumbers.contains(Long.parseLong(items.get(5)))) {
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
                        .orderDate(dateInserted)
                        .build());
            }
            antibiogram.setExamination(examination);

            antibiogram.setSusceptibility(items.get(10));
            antibiogram.setMic(items.get(11));
            antibiogram.setAlert(parseStringToBoolean(items.get(12)));
            antibiogram.setPatogen(parseStringToBoolean(items.get(13)));
            antibiogram.setGrowth(items.get(14));
            antibiogram.setFirstIsolate(parseStringToBoolean(items.get(15)));
            antibiogram.setHospitalInfection(parseStringToBoolean(items.get(16)));
            antibiogram.setOrderId(Long.parseLong(items.get(17)));
            antibiogram.setTestId(Long.parseLong(items.get(18)));
            antibiogram.setIsolationId(Long.parseLong(items.get(19)));
            antibiogram.setIsolationCode(items.get(20));
            antibiogram.setIsolationNum(Long.parseLong(items.get(21)));
            antibiogram.setResult(items.get(23));
            antibiogram.setDailyNumber(items.get(24));
            antibiogram.setMode(items.get(25));
            antibiogram.setPryw(items.get(26));

//            if (toInsert.size() > 0) {
//                if (toInsert.get(0).getOrderNumber() ==
//                        antibiogram.getOrderNumber()) {
//                    toInsert.add(antibiogram);
//                } else {
//                    antibiogramRepository.saveAll(toInsert);
//                    toInsert.clear();
//                    em.flush();
//                    em.clear();
//                }
//            } else {
//                toInsert.add(antibiogram);
//            }

            Antibiogram savedAntibiogram = antibiogramRepository.save(antibiogram);
            em.flush();
            em.clear();
        }

//        if (toInsert.size() > 0) {
//            antibiogramRepository.saveAll(toInsert);
//            toInsert.clear();
//            em.flush();
//            em.clear();
//        }
    }
}
