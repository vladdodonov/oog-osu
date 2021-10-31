package com.dodonov.oogosu.mapstruct;

import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.dto.EmployeeDto;
import com.dodonov.oogosu.dto.EmployeeSaveDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);
    EmployeeDto toDto(Employee employee);
    Employee toEntity(EmployeeDto employeeDto);
    Employee toEntity(EmployeeSaveDto employeeDto);
    List<EmployeeDto> toDtos(List<Employee> employee);
    List<Employee> toEntities(List<EmployeeDto> employeeDto);
}
