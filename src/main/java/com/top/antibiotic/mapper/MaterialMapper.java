package com.top.antibiotic.mapper;

import com.top.antibiotic.dto.MaterialDto;
import com.top.antibiotic.entities.Material;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaterialMapper {
    MaterialDto mapMaterialToDto(Material material);

    @InheritInverseConfiguration
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "materialId", ignore = true)
    Material mapDtoToMaterial(MaterialDto materialDto);
}
