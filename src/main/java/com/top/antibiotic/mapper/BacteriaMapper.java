package com.top.antibiotic.mapper;

import com.top.antibiotic.dto.BacteriaDto;
import com.top.antibiotic.entities.Bacteria;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BacteriaMapper {
    BacteriaDto mapBacteriaToDto(Bacteria bacteria);

    @InheritInverseConfiguration
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "bacteriaId", ignore = true)
    Bacteria mapDtoToBacteria(BacteriaDto bacteriaDto);
}
