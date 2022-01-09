package com.top.antibiotic.servcice;

import com.top.antibiotic.dto.BacteriaDto;
import com.top.antibiotic.entities.Bacteria;
import com.top.antibiotic.repository.BacteriaRepository;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.mapper.BacteriaMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class BacteriaService {

    private final BacteriaRepository bacteriaRepository;
    private final BacteriaMapper bacteriaMapper;

    @Transactional
    public BacteriaDto save(BacteriaDto bacteriaDto) {
        Bacteria savedWard = bacteriaRepository.save(
                bacteriaMapper.mapDtoToBacteria(bacteriaDto));

        bacteriaDto.setBacteriaId(savedWard.getBacteriaId());
        return bacteriaDto;
    }

    @Transactional(readOnly = true)
    public List<BacteriaDto> getAll() {
        return bacteriaRepository.findAll()
                .stream()
                .map(bacteriaMapper::mapBacteriaToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public BacteriaDto getBacteria(Long id) {
        Bacteria bacteria = bacteriaRepository.findById(id)
                .orElseThrow(() -> new AntibioticsException("No bacteria found with given id: " + id));

        return bacteriaMapper.mapBacteriaToDto(bacteria);
    }
}
