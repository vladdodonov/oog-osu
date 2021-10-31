package com.dodonov.oogosu.service.impl;

import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.domain.enums.Qualification;
import com.dodonov.oogosu.dto.appeal.AppealDto;
import com.dodonov.oogosu.dto.appeal.AppealMatchingEmployeeDto;
import com.dodonov.oogosu.repository.AppealRepository;
import com.dodonov.oogosu.repository.EmployeeRepository;
import com.dodonov.oogosu.service.EmployeeService;
import com.dodonov.oogosu.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.dodonov.oogosu.domain.dict.Employee.COMPARATOR_EMPLOYEE_APPEALS_NUMBER;
import static com.dodonov.oogosu.domain.enums.Difficulty.HARD;
import static com.dodonov.oogosu.domain.enums.Difficulty.MEDIUM;
import static com.dodonov.oogosu.domain.enums.Qualification.JUNIOR;
import static com.dodonov.oogosu.domain.enums.Qualification.LEAD;
import static com.dodonov.oogosu.domain.enums.Qualification.MIDDLE;
import static com.dodonov.oogosu.domain.enums.Qualification.SENIOR;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AppealRepository appealRepository;
    private final SecurityService securityService;

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        employeeRepository.archive(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAllByDepartmentId(Long departmentId) {
        return employeeRepository.findAllByDepartmentId(departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Employee, Long> findAllByDepartmentIdAndQualifications(Long departmentId, Set<Qualification> qualification) {
        return appealRepository.getCountAppealsOnExecutorInDepartment(departmentId, qualification);
    }

    @Override
    public Employee findLeadByDepartmentId(Long departmentId) {
        return employeeRepository.findByQualificationAndDepartment_id(Qualification.LEAD, departmentId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findEmployeesMatching(AppealMatchingEmployeeDto dto) {
        var isUrgent = LocalDateTime.now().until(dto.getDueDate(), DAYS) <= 10;
        var difficulty = dto.getDifficulty();
        Set<Qualification> qualifications = new HashSet<>();
        if (isUrgent && HARD.equals(difficulty)) {
            qualifications.add(SENIOR);
        } else if (isUrgent && MEDIUM.equals(difficulty) || !isUrgent && HARD.equals(difficulty)) {
            qualifications.add(SENIOR);
            qualifications.add(MIDDLE);
        } else {
            qualifications.add(MIDDLE);
            qualifications.add(JUNIOR);
        }
        var emps = findAllByDepartmentIdAndQualifications(securityService.getCurrentDepartment().getId(), qualifications);

        return emps.entrySet().stream()
                .filter(e -> {
                    var emp = e.getKey();
                    return !JUNIOR.equals(emp.getQualification()) || e.getValue() < 5;
                })
                .map(e -> {
                    var emp = e.getKey();
                    emp.setAppealsNumber(e.getValue());
                    return emp;
                })
                .sorted(COMPARATOR_EMPLOYEE_APPEALS_NUMBER)
                .limit(3)
                .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getCurrent() {
        return securityService.getCurrentEmployee();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllFromMyDepartment() {
        return employeeRepository.findAllExecutorsByDepartmentId(securityService.getCurrentDepartment().getId());
    }
}
