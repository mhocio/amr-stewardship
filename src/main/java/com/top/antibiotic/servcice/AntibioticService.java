package com.top.antibiotic.servcice;

import com.top.antibiotic.dto.AntibioticDto;
import com.top.antibiotic.entities.Antibiotic;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.mapper.AntibioticMapper;
import com.top.antibiotic.repository.AntibioticRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class AntibioticService {

    private final AntibioticRepository antibioticRepository;
    private final AntibioticMapper antibioticMapper;

    @Transactional
    public AntibioticDto save(AntibioticDto antibioticDto) {
        Antibiotic savedAntibiotic = antibioticRepository.save(
                antibioticMapper.mapDtoToAntibiotic(antibioticDto));

        antibioticDto.setAntibioticId(savedAntibiotic.getAntibioticId());
        return antibioticDto;
    }

    @Transactional(readOnly = true)
    public List<AntibioticDto> getAll() {
        return antibioticRepository.findAll()
                .stream()
                .map(antibioticMapper::mapAntibioticToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public AntibioticDto getAntibiotic(Long id) {
        Antibiotic antibiotic = antibioticRepository.findById(id)
                .orElseThrow(() -> new AntibioticsException("No antibiotic found with given id: " + id));

        return antibioticMapper.mapAntibioticToDto(antibiotic);
    }
}
