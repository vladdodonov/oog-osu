package com.dodonov.oogosu.service.impl;

import com.dodonov.oogosu.config.security.Principal;
import com.dodonov.oogosu.config.security.UserRole;
import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.repository.EmployeeRepository;
import com.dodonov.oogosu.repository.PrincipalRepository;
import com.dodonov.oogosu.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import java.util.Objects;
import java.util.Set;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    private final PrincipalRepository principalRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Department getCurrentDepartment() {
        var principal = getPrincipal();
        var employee = employeeRepository.findByUsername(principal.getUsername())
                .orElseThrow(EntityNotFoundException::new);
        return employee.getDepartment();
    }

    @Override
    public Employee getCurrentEmployee() {
        var principal = getPrincipal();
        return employeeRepository.findByUsername(principal.getUsername())
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public boolean hasRole(UserRole role) {
        var principal = getPrincipal();
        return Objects.equals(role, principal.getRole());
    }

    @Override
    public boolean hasAnyRole(Set<UserRole> roles) {
        var principal = getPrincipal();
        return roles.contains(principal.getRole());
    }

    private Principal getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (isNull(auth)) {
            throw new RuntimeException("Не выполнена авторизация");
        }
        return principalRepository.findByUsername((String) auth.getPrincipal()).orElseThrow(EntityNotFoundException::new);
    }
}
