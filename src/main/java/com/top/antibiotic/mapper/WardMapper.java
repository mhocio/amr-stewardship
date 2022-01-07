package com.top.antibiotic.mapper;

import com.top.antibiotic.dto.WardDto;
import com.top.antibiotic.entities.Ward;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WardMapper {

    WardDto mapWardToDto(Ward subreddit);

    @InheritInverseConfiguration
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    Ward mapDtoToWard(WardDto subredditDto);
}