package com.dodonov.oogosu.service;

import com.dodonov.oogosu.domain.dict.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> findAll();
    List<Department> findAllWithDeleted();
    Department findById(Long departmentId);
    void deleteById(Long departmentId);
    Department saveDepartment(Department department);

    Department restore(Long departmentId);
}
