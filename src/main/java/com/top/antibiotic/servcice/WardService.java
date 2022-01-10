package com.top.antibiotic.servcice;

import com.top.antibiotic.dto.WardDto;
import com.top.antibiotic.entities.Ward;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.mapper.WardMapper;
import com.top.antibiotic.repository.WardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class WardService {

    private final WardRepository wardRepository;
    private final WardMapper wardMapper;

    @Transactional
    public WardDto save(WardDto wardDto) {
        Ward savedWard = wardRepository.save(
                wardMapper.mapDtoToWard(wardDto));

        wardDto.setWardId(savedWard.getWardId());
        return wardDto;
    }

    @Transactional(readOnly = true)
    public List<WardDto> getAll() {
        return wardRepository.findAll()
                .stream()
                .map(wardMapper::mapWardToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public WardDto getWard(Long id) {
        Ward ward = wardRepository.findById(id)
                .orElseThrow(() -> new AntibioticsException("No ward found with given id: " + id));

        return wardMapper.mapWardToDto(ward);
    }

    @Transactional(readOnly = true)
    public WardDto getWard(String name) {
        Ward ward = wardRepository.findByName(name)
                .orElseThrow(() -> new AntibioticsException("No ward found with given name: " + name));

        return wardMapper.mapWardToDto(ward);
    }
}
