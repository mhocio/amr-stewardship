package com.top.antibiotic.mapper;

import com.top.antibiotic.dto.ExaminationDto;
import com.top.antibiotic.entities.Examination;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExaminationMapper {
    @Mapping(target = "wardName", expression = "java(examination.getWard().getName())")
    @Mapping(target = "wardId", expression = "java(examination.getWard().getWardId())")
    @Mapping(target = "materialName", expression = "java(examination.getMaterial().getName())")
    @Mapping(target = "materialIdl", expression = "java(examination.getMaterial().getMaterialId())")
    @Mapping(target = "patientPesel", expression = "java(examination.getPatient().getPesel())")
    @Mapping(target = "patientId", expression = "java(examination.getPatient().getPatientId())")
    ExaminationDto mapExaminationToDtoResponse(Examination examination);
}
