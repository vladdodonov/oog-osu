package com.dodonov.oogosu.mapstruct.appeal;

import com.dodonov.oogosu.domain.Appeal;
import com.dodonov.oogosu.dto.appeal.AppealDto;
import com.dodonov.oogosu.mapstruct.CitizenDtoMapper;
import com.dodonov.oogosu.mapstruct.DepartmentMapper;
import com.dodonov.oogosu.mapstruct.EmployeeMapper;
import com.dodonov.oogosu.mapstruct.TopicDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {CitizenDtoMapper.class, TopicDtoMapper.class, DepartmentMapper.class, EmployeeMapper.class})
public interface AppealDtoMapper {
    AppealDtoMapper INSTANCE = Mappers.getMapper(AppealDtoMapper.class);

    List<AppealDto> toDto(List<Appeal> appeal);
}
