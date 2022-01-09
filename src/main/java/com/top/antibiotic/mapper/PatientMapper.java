package com.top.antibiotic.mapper;

import com.top.antibiotic.dto.PatientDto;
import com.top.antibiotic.entities.Patient;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDto mapPatientToDto(Patient patient);

    @InheritInverseConfiguration
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "patientId", ignore = true)
    Patient mapDtoToPatient(PatientDto patientDto);
}
