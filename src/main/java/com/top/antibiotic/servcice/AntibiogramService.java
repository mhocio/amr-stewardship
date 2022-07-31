package com.top.antibiotic.servcice;

import com.monitorjbl.xlsx.StreamingReader;
import com.top.antibiotic.dto.AntibiogramResponse;
import com.top.antibiotic.entities.*;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.mapper.AntibiogramMapper;
import com.top.antibiotic.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;
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
    private final ExaminationProviderRepository examinationProviderRepository;

    private final AntibiogramRepository antibiogramRepository;
    private final AntibiogramMapper antibiogramMapper;

    private final Boolean SKIP_DUPLICATE_EXAMINATION = true;

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
        return s.startsWith("T") || s.startsWith("1") || s.startsWith("t");
    }

    List<String> generateListOfItems(String s) {
        return Arrays.asList(s.replaceAll("\\[", "")
                .replaceAll("\\]", "").replaceAll(" ", "").split(","));
    }

    Ward parseWard(String name, Instant date) {
        Ward ward;
        try {
            ward = wardRepository.findByName(name)
                    .orElseThrow(() -> new AntibioticsException("No ward found"));
        } catch (Exception e) {
            ward = wardRepository.save(Ward.builder()
                    .name(name)
                    .createdDate(date)
                    .build());
        }
        return ward;
    }

    Patient parsePatient(String firstName, String secondName, String pesel, Instant date) {
        Patient patient;
        try {
            patient = patientRepository.findByPesel(pesel)
                    .orElseThrow(() -> new AntibioticsException("No patient found"));
        } catch (Exception e) {
            patient = patientRepository.save(Patient.builder()
                    .firstName(firstName)
                    .secondName(secondName)
                    .pesel(pesel)
                    .createdDate(date)
                    .build());
        }
        return patient;
    }

    Material parseMaterial(String materialName, Instant date) {
        Material material;
        try {
            material = materialRepository.findByName(materialName)
                    .orElseThrow(() -> new AntibioticsException("No material found"));
        } catch (Exception e) {
            material = materialRepository.save(Material.builder()
                    .name(materialName)
                    .createdDate(date)
                    .build());
        }
        return material;
    }

    Bacteria parseBacteria(String name, String subtype, Instant date) {
        Bacteria bacteria;
        try {
            bacteria = bacteriaRepository.findByName(name)
                    .orElseThrow(() -> new AntibioticsException("No bacteria found"));
        } catch (Exception e) {
            bacteria = bacteriaRepository.save(Bacteria.builder()
                    .name(name)
                    .subtype(subtype)
                    .createdDate(date)
                    .build());
        }
        return bacteria;
    }

    Antibiotic parseAntibiotic(String name, String code, Instant date) {
        Antibiotic antibiotic;
        try {
            antibiotic = antibioticRepository.findByName(name)
                    .orElseThrow(() -> new AntibioticsException("No antibiotic found"));
        } catch (Exception e) {
            antibiotic = antibioticRepository.save(Antibiotic.builder()
                    .name(name)
                    .code(code)
                    .createdDate(date)
                    .build());
        }
        return antibiotic;
    }

    private void parseAntibiogram(String resistantAntibioticName, String suspectability,
                                     Ward ward, Instant date, Patient patient, Date dateInserted,
                                     Material material, Bacteria bacteria, Examination examination) {
        if (!suspectability.equals("R") && !suspectability.equals("I") && !suspectability.equals("S")) {
            throw new RuntimeException("wrong suspectability type");
        }

        if (resistantAntibioticName.isEmpty())
            return;
        Antibiogram antibiogram = new Antibiogram();
        antibiogram.setWard(ward);
        antibiogram.setCreatedDate(date);
        antibiogram.setPatient(patient);
        antibiogram.setOrderDate(dateInserted);
        antibiogram.setMaterial(material);
        antibiogram.setBacteria(bacteria);
        Antibiotic antibiotic = parseAntibiotic(resistantAntibioticName, null, date);
        antibiogram.setAntibiotic(antibiotic);
        antibiogram.setSusceptibility(suspectability);

        antibiogram.setExamination(examination);
        antibiogramRepository.save(antibiogram);
    }

    @Async()
    //@Transactional
    public void saveFromFileCGM(ByteArrayResource reapExcelDataFile, Optional<Integer> sheetNumber) throws IOException, ParseException {
        String providerName = "CGM";

        Workbook workbook = StreamingReader.builder().rowCacheSize(100) // number of rows to keep in memory
                .bufferSize(4096)
                .open(reapExcelDataFile.getInputStream()); // InputStream or File for XLSX file (required)

        Iterator<Row> rowIterator = workbook.getSheetAt(sheetNumber.orElse(1)).rowIterator();

        // whether we insert this new Examination number this time
        Set<Long> newExaminationNumbers = new HashSet<Long>();

        ExaminationProvider examinationProvider = examinationProviderRepository.findByName(providerName);
        if (examinationProvider == null) {
            examinationProvider = examinationProviderRepository.save(ExaminationProvider.builder().name(providerName).build());
        }

        int rowNo = -1;
        while (rowIterator.hasNext()) {
            try {
                Row row = rowIterator.next();
                rowNo = rowNo + 1;
                if (rowNo == 0) {
                    continue;
                }

                List<String> items = extractRow(row);

                Long examinationNumber = Long.parseLong(items.get(3));

                if (SKIP_DUPLICATE_EXAMINATION) {
                    // do not proceed if sb previously pushed this file or file with this Examination
                    if (!examinationRepository.existsByNumberAndExaminationProvider(
                            examinationNumber, examinationProvider)) {
                        newExaminationNumbers.add(examinationNumber);
                    }
                    if (!newExaminationNumbers.contains(examinationNumber)) {
                        continue;
                    }
                }

                Instant date = Instant.now();

                Ward ward = parseWard(items.get(1), date);
                Patient patient = parsePatient(items.get(19), items.get(18), items.get(21), date);
                Bacteria bacteria = parseBacteria(items.get(10), null, date);
                Material material = parseMaterial(items.get(14), date);

                Examination examination;
                String pattern = "yy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                Date dateInserted = simpleDateFormat.parse(items.get(2));
                try {
                    examination = examinationRepository.findByNumberAndExaminationProvider(
                                    examinationNumber, examinationProvider)
                            .orElseThrow(() -> new AntibioticsException("No Order found"));
                } catch (Exception e) {
                    examination = examinationRepository.save(Examination.builder()
                            .number(examinationNumber)
                            .ward(ward)
                            .material(material)
                            .patient(patient)
                            .examinationProvider(examinationProvider)
                            .orderDate(dateInserted)
                            .build());
                }

                for (String resistantAntibioticName : generateListOfItems(items.get(15))) {
                    parseAntibiogram(resistantAntibioticName, "R", ward, date, patient,
                            dateInserted, material, bacteria, examination);
                }
                for (String resistantAntibioticName : generateListOfItems(items.get(16))) {
                    parseAntibiogram(resistantAntibioticName, "I", ward, date, patient,
                            dateInserted, material, bacteria, examination);
                }
                for (String resistantAntibioticName : generateListOfItems(items.get(17))) {
                    parseAntibiogram(resistantAntibioticName, "S", ward, date, patient,
                            dateInserted, material, bacteria, examination);
                }
                try {
                    em.flush();
                    em.clear();
                } catch (TransactionRequiredException e) {}
            } catch (Exception e) {}
        }
        log.info("done...CGM");
    }

    private List<String> extractRow(Row row) {
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
        }
        return items;
    }

    @Async()
    //@Transactional()
    public void saveFromFile(ByteArrayResource reapExcelDataFile, Optional<Integer> sheetNumber) throws IOException, ParseException {

        String providerName = "ASSECO";

        Workbook workbook = StreamingReader.builder().rowCacheSize(100) // number of rows to keep in memory
                .bufferSize(4096)
                .open(reapExcelDataFile.getInputStream()); // InputStream or File for XLSX file (required)

        Iterator<Row> rowIterator = workbook.getSheetAt(sheetNumber.orElse(2)).rowIterator();

        // whether we insert this new Examination number this time
        Set<Long> newExaminationNumbers = new HashSet<Long>();

        List<Antibiogram> toInsert = new ArrayList<>();

        ExaminationProvider examinationProvider = examinationProviderRepository.findByName(providerName);
        if (examinationProvider == null) {
            examinationProvider = examinationProviderRepository.save(ExaminationProvider.builder().name(providerName).build());
        }

        int rowNo = -1;
        while (rowIterator.hasNext()) {
            try {
                Row row = rowIterator.next();
                rowNo = rowNo + 1;
                if (rowNo == 0) {
                    continue;  // skip header row
                }

                Instant date = Instant.now();

                Antibiogram antibiogram = new Antibiogram();
                antibiogram.setCreatedDate(date);

                List<String> items = extractRow(row);

                if (items.size() == 26) {
                    items.add("");  // sometimes last column is null
                }

                if (items.size() < 27) {
                    continue;
                }

                if (SKIP_DUPLICATE_EXAMINATION) {
                    // do not proceed if sb previously pushed this file or file with this Examination
                    if (!examinationRepository.existsByNumberAndExaminationProvider(
                            Long.parseLong(items.get(5)), examinationProvider)) {
                        newExaminationNumbers.add(Long.parseLong(items.get(5)));
                    }
                    if (!newExaminationNumbers.contains(Long.parseLong(items.get(5)))) {
                        continue;
                    }
                }

                Ward ward = parseWard(items.get(0), date);
                antibiogram.setWard(ward);

                Patient patient = parsePatient(items.get(1), items.get(2), items.get(3), date);
                antibiogram.setPatient(patient);

                String pattern = "MM/dd/yy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                Date dateInserted = simpleDateFormat.parse(items.get(4));
                antibiogram.setOrderDate(dateInserted);

                antibiogram.setOrderNumber(Long.parseLong(items.get(5)));

                Material material = parseMaterial(items.get(6), date);
                antibiogram.setMaterial(material);

                Bacteria bacteria = parseBacteria(items.get(7), items.get(8), date);
                antibiogram.setBacteria(bacteria);

                Antibiotic antibiotic = parseAntibiotic(items.get(9), items.get(22), date);
                antibiogram.setAntibiotic(antibiotic);

                Examination examination;
                try {
                    examination = examinationRepository.findByNumberAndExaminationProvider(
                                    Long.parseLong(items.get(5)), examinationProvider)
                            .orElseThrow(() -> new AntibioticsException("No Order found"));
                } catch (Exception e) {
                    examination = examinationRepository.save(Examination.builder()
                            .number(Long.parseLong(items.get(5)))
                            .ward(ward)
                            .material(material)
                            .patient(patient)
                            .examinationProvider(examinationProvider)
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
                try {
                    em.flush();
                    em.clear();
                } catch (TransactionRequiredException e) {}
            } catch (Exception e) {}
        }

//        if (toInsert.size() > 0) {
//            antibiogramRepository.saveAll(toInsert);
//            toInsert.clear();
//            em.flush();
//            em.clear();
//        }
        log.info("done...ASSECO");
    }
}
