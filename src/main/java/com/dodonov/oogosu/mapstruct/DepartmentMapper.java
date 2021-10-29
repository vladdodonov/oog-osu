package com.dodonov.oogosu.mapstruct;

import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.dto.DepartmentDto;
import org.mapstruct.Mapper;

@Mapper(uses = {EmployeeMapper.class})
public interface DepartmentMapper {
    Department toEntity(DepartmentDto departmentDto);
    DepartmentDto toDto(Department department);
}
