package com.top.antibiotic.servcice;

import com.top.antibiotic.dto.MaterialDto;
import com.top.antibiotic.entities.Material;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.mapper.MaterialMapper;
import com.top.antibiotic.repository.MaterialRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;

    @Transactional
    public MaterialDto save(MaterialDto materialDto) {
        Material savedMaterial = materialRepository.save(
                materialMapper.mapDtoToMaterial(materialDto));

        materialDto.setMaterialId(savedMaterial.getMaterialId());
        return materialDto;
    }

    @Transactional(readOnly = true)
    public List<MaterialDto> getAll() {
        return materialRepository.findAll()
                .stream()
                .map(materialMapper::mapMaterialToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MaterialDto getMaterial(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new AntibioticsException("No material found with given id: " + id));

        return materialMapper.mapMaterialToDto(material);
    }
}
