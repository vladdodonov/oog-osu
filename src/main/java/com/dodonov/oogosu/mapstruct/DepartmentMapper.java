package com.dodonov.oogosu.mapstruct;

import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.dto.DepartmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {EmployeeMapper.class})
public interface DepartmentMapper {
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    Department toEntity(DepartmentDto departmentDto);

    DepartmentDto toDto(Department department);

    List<Department> toEntities(List<DepartmentDto> departmentDto);

    List<DepartmentDto> toDtos(List<Department> department);
}
