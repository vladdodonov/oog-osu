package com.dodonov.oogosu.service;

import com.dodonov.oogosu.domain.dict.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> findAll();
    Department findById(Long departmentId);
    void deleteById(Long departmentId);
    Department saveDepartment(Department department);
}
