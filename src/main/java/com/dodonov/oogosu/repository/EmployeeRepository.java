package com.dodonov.oogosu.repository;

import com.dodonov.oogosu.domain.dict.Employee;

import java.util.Optional;

public interface EmployeeRepository extends BaseRepository<Employee> {
    Optional<Employee> findByUsername(String username);
}
