package com.top.antibiotic.servcice;

import com.top.antibiotic.dto.PatientDto;
import com.top.antibiotic.dto.WardDto;
import com.top.antibiotic.entities.Patient;
import com.top.antibiotic.entities.Ward;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.mapper.PatientMapper;
import com.top.antibiotic.repository.PatientRepository;
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
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Transactional
    public PatientDto save(PatientDto patientDto) {
        Patient savedPatient = patientRepository.save(
                patientMapper.mapDtoToPatient(patientDto));
        patientDto.setPatientId(savedPatient.getPatientId());

        return patientDto;
    }

    @Transactional(readOnly = true)
    public List<PatientDto> getAll() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::mapPatientToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientDto getPatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new AntibioticsException("No patient found with given id: " + id));

        return patientMapper.mapPatientToDto(patient);
    }
}
