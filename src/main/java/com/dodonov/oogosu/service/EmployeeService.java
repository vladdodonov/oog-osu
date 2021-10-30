package com.dodonov.oogosu.service;

import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.domain.enums.Qualification;
import com.dodonov.oogosu.dto.appeal.AppealDto;
import com.dodonov.oogosu.dto.appeal.AppealMatchingEmployeeDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmployeeService {
    Employee save(Employee employee);

    void deleteById(Long id);

    List<Employee> findAllByDepartmentId(Long departmentId);

    Employee findLeadByDepartmentId(Long departmentId);

    List<Employee> findEmployeesMatching(AppealMatchingEmployeeDto dto);

    Map<Employee, Long> findAllByDepartmentIdAndQualifications(Long departmentId, Set<Qualification> qualification);

    Employee getCurrent();

    List<Employee> getAllFromMyDepartment();
}
