package com.dodonov.oogosu.service.impl;

import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.repository.DepartmentRepository;
import com.dodonov.oogosu.repository.TopicRepository;
import com.dodonov.oogosu.service.DepartmentService;
import com.dodonov.oogosu.service.EmployeeService;
import com.dodonov.oogosu.service.TopicService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeService employeeService;
    private final TopicRepository topicRepository;
    private final TopicService topicService;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 @Lazy EmployeeService employeeService,
                                 TopicRepository topicRepository,
                                 TopicService topicService) {
        this.departmentRepository = departmentRepository;
        this.employeeService = employeeService;
        this.topicRepository = topicRepository;
        this.topicService = topicService;
    }

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
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long departmentId) {
        if (3L == departmentId){
            throw new RuntimeException("???????????? ?????????????? ?????????????????????? ????????????????????????????");
        }
        employeeService.findAllByDepartmentId(departmentId)
                .forEach(employee -> employeeService.deleteById(employee.getId()));
        topicRepository.archiveByDepartment(departmentId);
        departmentRepository.archive(departmentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Department restore(Long departmentId) {
        var dep = departmentRepository.findById(departmentId).orElseThrow(EntityNotFoundException::new);
        var emps = employeeService.findAllByDepartmentIdWithDeleted(departmentId)
                .stream()
                .filter(a -> isTrue(a.getArchived()))
                .collect(toSet());
        var topics = topicService.findAllByDepartmentIdWithDeleted(departmentId)
                .stream()
                .filter(a -> isTrue(a.getArchived()))
                .collect(toSet());
        dep.setArchived(null);
        var saved = departmentRepository.saveAndFlush(dep);
        emps.forEach(e -> employeeService.restore(e.getId()));
        topics.forEach(t -> topicService.restore(t.getId()));
        return saved;
    }
}
