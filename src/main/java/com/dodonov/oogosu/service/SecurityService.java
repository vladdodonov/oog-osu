package com.dodonov.oogosu.service;

import com.dodonov.oogosu.config.security.UserRole;
import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.domain.dict.Employee;

import java.util.Set;

public interface SecurityService {

    Department getCurrentDepartment();

    Employee getCurrentEmployee();

    boolean hasRole(UserRole role);

    boolean hasAnyRole(Set<UserRole> roles);
}
