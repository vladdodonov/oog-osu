package com.dodonov.oogosu.service.impl;

import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.repository.DepartmentRepository;
import com.dodonov.oogosu.repository.EmployeeRepository;
import com.dodonov.oogosu.repository.TopicRepository;
import com.dodonov.oogosu.service.DepartmentService;
import com.dodonov.oogosu.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeService employeeService;
    private final TopicRepository topicRepository;
    @Override
    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAll().stream().filter(a -> a.getArchived() == null).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> findAllWithDeleted() {
        return departmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Department findById(Long departmentId) {
        return departmentRepository.findById(departmentId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void deleteById(Long departmentId) {
        employeeService.findAllByDepartmentId(departmentId)
                .forEach(employee -> employeeService.deleteById(employee.getId()));
        topicRepository.archiveByDepartment(departmentId);
        departmentRepository.archive(departmentId);
    }
}
