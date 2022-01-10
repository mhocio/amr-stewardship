package com.top.antibiotic.mapper;

import com.top.antibiotic.dto.AntibiogramResponse;
import com.top.antibiotic.entities.Antibiogram;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AntibiogramMapper {

    @Mapping(target = "bacteria", expression = "java(antibiogram.getBacteria().getName())")
    @Mapping(target = "antibiotic", expression = "java(antibiogram.getAntibiotic().getName())")
    @Mapping(target = "material", expression = "java(antibiogram.getMaterial().getName())")
    @Mapping(target = "ward", expression = "java(antibiogram.getWard().getName())")
    @Mapping(target = "pesel", expression = "java(antibiogram.getPatient().getPesel())")
    AntibiogramResponse mapAntibiogramToResponse(Antibiogram antibiogram);
}
