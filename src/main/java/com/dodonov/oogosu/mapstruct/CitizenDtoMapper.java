package com.dodonov.oogosu.mapstruct;

import com.dodonov.oogosu.domain.Citizen;
import com.dodonov.oogosu.dto.CitizenDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CitizenDtoMapper {
    CitizenDtoMapper INSTANCE = Mappers.getMapper(CitizenDtoMapper.class);

    CitizenDto toEntity(Citizen citizen);

    Citizen toDto(CitizenDto citizenDto);
}
