package com.top.antibiotic.servcice;

import com.top.antibiotic.dto.ExaminationDto;
import com.top.antibiotic.entities.Material;
import com.top.antibiotic.entities.Examination;
import com.top.antibiotic.entities.Patient;
import com.top.antibiotic.entities.Ward;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.mapper.ExaminationMapper;
import com.top.antibiotic.repository.MaterialRepository;
import com.top.antibiotic.repository.ExaminationRepository;
import com.top.antibiotic.repository.PatientRepository;
import com.top.antibiotic.repository.WardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class ExaminationService {

    private final ExaminationRepository examinationRepository;
    private final PatientRepository patientRepository;
    private final WardRepository wardRepository;
    private final MaterialRepository materialRepository;

    private final ExaminationMapper examinationMapper;

    @Transactional(readOnly = true)
    public List<ExaminationDto> getAll() {
        return examinationRepository.findAll()
                .stream()
                .map(examinationMapper::mapExaminationToDtoResponse)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public ExaminationDto getById(Long id) {
        Examination order = examinationRepository.findById(id)
                .orElseThrow(() -> new AntibioticsException("No Order with id: " + id.toString()));

        return examinationMapper.mapExaminationToDtoResponse(order);
    }

    @Transactional(readOnly = true)
    public List<ExaminationDto> getByPesel(String pesel) {
        Patient patient = patientRepository.findByPesel(pesel)
                .orElseThrow(() -> new AntibioticsException("No Patient with pesel: " + pesel));

        return examinationRepository.findByPatient(patient)
                .stream()
                .map(examinationMapper::mapExaminationToDtoResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExaminationDto> getByByWardAndMaterial(String wardName, String materialName) {
        Ward ward = wardRepository.findByName(wardName)
                .orElseThrow(() -> new AntibioticsException("No Ward with name: " + wardName));
        Material material = materialRepository.findByName(materialName)
                .orElseThrow(() -> new AntibioticsException("No Material found with name: " + materialName));

        return examinationRepository.findByWardAndMaterial(ward, material)
                .stream()
                .map(examinationMapper::mapExaminationToDtoResponse)
                .collect(Collectors.toList());
    }
}
