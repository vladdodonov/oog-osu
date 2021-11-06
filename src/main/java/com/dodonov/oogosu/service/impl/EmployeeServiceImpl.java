package com.dodonov.oogosu.service.impl;

import com.dodonov.oogosu.config.security.Principal;
import com.dodonov.oogosu.config.security.UserRole;
import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.domain.enums.Qualification;
import com.dodonov.oogosu.dto.EmployeeSaveDto;
import com.dodonov.oogosu.dto.appeal.AppealMatchingEmployeeDto;
import com.dodonov.oogosu.mapstruct.EmployeeMapper;
import com.dodonov.oogosu.repository.AppealRepository;
import com.dodonov.oogosu.repository.EmployeeRepository;
import com.dodonov.oogosu.repository.PrincipalRepository;
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
import static com.dodonov.oogosu.domain.enums.Qualification.MIDDLE;
import static com.dodonov.oogosu.domain.enums.Qualification.SENIOR;
import static com.dodonov.oogosu.domain.enums.Qualification.SPECIAL;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AppealRepository appealRepository;
    private final SecurityService securityService;
    private final PrincipalRepository principalRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Employee save(EmployeeSaveDto saveDto) {
        if (employeeRepository.existsByUsername(saveDto.getUsername())) {
            throw new RuntimeException("Уже есть с таким логином");
        }
        if (saveDto.getDepartment() == null || saveDto.getDepartment().getId() == null) {
            throw new RuntimeException("Не представлен департамент");
        }

        var leadOpt = employeeRepository
                .findLeadByDepartmentId(saveDto.getDepartment().getId());
        var leadIsPresent = leadOpt.isPresent();
        if (UserRole.LEAD.equals(saveDto.getRole())) {
            if (leadIsPresent) {
                if (saveDto.getId() == null || !leadOpt.get().getId().equals(saveDto.getId())) {
                    throw new RuntimeException("Смена начальника - отдельный эндпойнт");
                }
            }
        }
        if (!leadIsPresent) {
            saveDto.setRole(UserRole.LEAD);
        }
        if (UserRole.INSPECTOR.equals(saveDto.getRole()) || UserRole.ADMIN.equals(saveDto.getRole())) {
            saveDto.setQualification(SPECIAL);
        }
        if (saveDto.getId() != null) {
            var principalFromDb = principalRepository.findByEmployeeId(saveDto.getId())
                    .orElseThrow(EntityNotFoundException::new);
            var empFromDb = employeeRepository.findById(saveDto.getId())
                    .orElseThrow(EntityNotFoundException::new);
            if (saveDto.getRole() != null) {
                principalFromDb.setRole(saveDto.getRole());
            }
            if (!leadIsPresent) {
                principalFromDb.setRole(UserRole.LEAD);
            }
            if (saveDto.getPassword() != null) {
                principalFromDb.setPassword(saveDto.getPassword());
            }
            if (isNotBlank(saveDto.getFirstName())) {
                empFromDb.setFirstName(saveDto.getFirstName());
            }
            if (isNotBlank(saveDto.getLastName())) {
                empFromDb.setLastName(saveDto.getLastName());
            }
            if (isNotBlank(saveDto.getMiddleName())) {
                empFromDb.setMiddleName(saveDto.getMiddleName());
            }
            if (isNotBlank(saveDto.getEmail())) {
                empFromDb.setEmail(saveDto.getEmail());
            }
            if (isNotBlank(saveDto.getPhone())) {
                empFromDb.setPhone(saveDto.getPhone());
            }
            if (saveDto.getQualification() != null) {
                empFromDb.setQualification(saveDto.getQualification());
            }
            if (empFromDb.getDepartment() == null) {
                empFromDb.setDepartment(Department.builder().id(saveDto.getDepartment().getId()).build());
            }
            principalRepository.save(principalFromDb);
            return employeeRepository.save(empFromDb);
        } else {
            var principal = Principal.builder()
                    .username(saveDto.getUsername())
                    .password(saveDto.getPassword())
                    .role(saveDto.getRole())
                    .build();
            principalRepository.save(principal);
            var emp = EmployeeMapper.INSTANCE.toEntity(saveDto);
            return employeeRepository.save(emp);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        var principalFromDb = principalRepository.findByEmployeeId(id)
                .orElseThrow(EntityNotFoundException::new);
        principalRepository.archive(principalFromDb.getUsername());
        employeeRepository.archive(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Employee restore(Long id) {
        var emp = employeeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (isTrue(emp.getDepartment().getArchived())) {
            throw new RuntimeException("Работник принадлежит к архивированному департаменту");
        }
        emp.setArchived(null);
        var principalFromDb = principalRepository.findByEmployeeId(id)
                .orElseThrow(EntityNotFoundException::new);
        principalFromDb.setArchived(null);
        principalRepository.save(principalFromDb);
        return employeeRepository.save(emp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAllByDepartmentId(Long departmentId) {
        return employeeRepository.findAllByDepartmentId(departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAllByDepartmentIdWithDeleted(Long departmentId) {
        return employeeRepository.findAllByDepartmentIdWithDeleted(departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Employee, Long> findAllByDepartmentIdAndQualifications(Long departmentId, Set<Qualification> qualification) {
        return appealRepository.getCountAppealsOnExecutorInDepartment(departmentId, qualification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Employee findLeadByDepartmentId(Long departmentId) {
        return employeeRepository.findLeadByDepartmentId(departmentId).orElse(null);
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
        if (securityService.hasRole(UserRole.ADMIN)) {
            return employeeRepository.findAllExecutors();
        }
        return employeeRepository.findAllExecutorsByDepartmentId(securityService.getCurrentDepartment().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllFromMyDepartmentWithDeleted() {
        if (securityService.hasRole(UserRole.ADMIN)) {
            return employeeRepository.findAllExecutorsWithDeleted();
        }
        return employeeRepository.findAllExecutorsByDepartmentIdWithDeleted(securityService.getCurrentDepartment().getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Employee changeLead(Long employeeId) {
        var nextLead = employeeRepository.findById(employeeId)
                .orElseThrow(EntityNotFoundException::new);
        var currentLead = findLeadByDepartmentId(nextLead.getDepartment().getId());
        if (nextLead.equals(currentLead)) {
            throw new RuntimeException("Назначаете того же самого начальником");
        }
        var nextLeadPrincipal = principalRepository.findByUsername(nextLead.getUsername())
                .orElseThrow(EntityNotFoundException::new);
        if (currentLead != null) {
            var currentLeadPrincipal = principalRepository.findByUsername(currentLead.getUsername())
                    .orElseThrow(EntityNotFoundException::new);
            currentLeadPrincipal.setRole(UserRole.EXECUTOR);
            principalRepository.save(currentLeadPrincipal);
        }
        nextLeadPrincipal.setRole(UserRole.LEAD);
        employeeRepository.save(currentLead);
        principalRepository.save(nextLeadPrincipal);
        return employeeRepository.save(nextLead);
    }
}
