package com.top.antibiotic.mapper;

import com.top.antibiotic.dto.AntibioticDto;
import com.top.antibiotic.dto.WardDto;
import com.top.antibiotic.entities.Antibiotic;
import com.top.antibiotic.entities.Ward;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AntibioticMapper {
    AntibioticDto mapAntibioticToDto(Antibiotic antibiotic);

    @InheritInverseConfiguration
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "antibioticId", ignore = true)
    Antibiotic mapDtoToAntibiotic(AntibioticDto antibioticDto);
}
