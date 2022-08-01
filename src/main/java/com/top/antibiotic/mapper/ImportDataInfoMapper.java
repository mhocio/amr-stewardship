package com.top.antibiotic.mapper;

import com.top.antibiotic.dto.ImportDataInfoResponse;
import com.top.antibiotic.entities.ImportDataInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImportDataInfoMapper {

    ImportDataInfoResponse mapImportDataInfoToResponse(ImportDataInfo importDataInfo);
}
