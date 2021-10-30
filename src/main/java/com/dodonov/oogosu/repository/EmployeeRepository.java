package com.dodonov.oogosu.repository;

import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.domain.enums.Qualification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends BaseRepository<Employee> {
    Optional<Employee> findByUsername(String username);

    @Modifying
    @Query(value = "update d_employee set archived = true where id = :id", nativeQuery = true)
    void archive(@Param("id") Long id);

    @Modifying
    @Query(value = "update d_employee set archived = true where department_id = :id", nativeQuery = true)
    void archiveByDepartment(@Param("id") Long departmentId);

    List<Employee> findAllByDepartment_id(Long departmentId);

    Optional<Employee> findByQualificationAndDepartment_id(Qualification qualification, Long departmentId);
}
