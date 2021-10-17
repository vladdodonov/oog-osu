package com.dodonov.oogosu.mapstruct.appeal;

import com.dodonov.oogosu.domain.Appeal;
import com.dodonov.oogosu.dto.AppealCreateDto;
import com.dodonov.oogosu.mapstruct.CitizenDtoMapper;
import com.dodonov.oogosu.mapstruct.TopicDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CitizenDtoMapper.class, TopicDtoMapper.class})
public interface AppealCreateDtoMapper {
    AppealCreateDtoMapper INSTANCE = Mappers.getMapper(AppealCreateDtoMapper.class);

    Appeal toEntity(AppealCreateDto appealCreateDto);
}
