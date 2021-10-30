package com.dodonov.oogosu.service;

import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.domain.enums.Qualification;
import com.dodonov.oogosu.dto.appeal.AppealDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmployeeService {
    Employee save(Employee employee);

    void deleteById(Long id);

    List<Employee> findAllByDepartmentId(Long departmentId);

    Employee findLeadByDepartmentId(Long departmentId);

    List<Employee> findEmployeesMatching(AppealDto dto);

    Map<Long, Employee> findAllByDepartmentIdAndQualifications(Long departmentId, Set<Qualification> qualification);
}
