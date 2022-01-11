package com.top.antibiotic.servcice;

import com.top.antibiotic.dto.AntibiogramResponse;
import com.top.antibiotic.entities.Antibiogram;
import com.top.antibiotic.entities.Patient;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.mapper.AntibiogramMapper;
import com.top.antibiotic.repository.AntibiogramRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AntibiogramService {
    // TODO
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
}
