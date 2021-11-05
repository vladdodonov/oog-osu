package com.dodonov.oogosu.repository;

import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.domain.enums.Qualification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends BaseRepository<Employee> {
    @Query("select e from Employee e where e.username = :username and coalesce(e.archived, false) is false")
    Optional<Employee> findByUsername(@Param("username") String username);

    @Modifying
    @Query(value = "update d_employee set archived = true where id = :id", nativeQuery = true)
    void archive(@Param("id") Long id);

    @Modifying
    @Query(value = "update d_employee set archived = true where department_id = :id", nativeQuery = true)
    void archiveByDepartment(@Param("id") Long departmentId);

    @Query(value = "select emp " +
            "from Employee emp " +
            "join Principal p on emp.username = p.username " +
            "where emp.department.id = :departmentId " +
            "and p.role = 'EXECUTOR' " +
            "and coalesce(emp.archived, false) is false")
    List<Employee> findAllExecutorsByDepartmentId(@Param("departmentId") Long departmentId);

    @Query(value = "select emp " +
            "from Employee emp " +
            "left join Principal p on emp.username = p.username " +
            "where emp.department.id = :departmentId " +
            "and (p.role is null or p.role = 'EXECUTOR')")
    List<Employee> findAllExecutorsByDepartmentIdWithDeleted(@Param("departmentId") Long departmentId);

    @Query(value = "select emp " +
            "from Employee emp " +
            "join Principal p on emp.username = p.username " +
            "where p.role = 'EXECUTOR' " +
            "and coalesce(emp.archived, false) is false")
    List<Employee> findAllExecutors();

    @Query(value = "select emp " +
            "from Employee emp " +
            "left join Principal p on emp.username = p.username " +
            "where (p.role is null or p.role = 'EXECUTOR')")
    List<Employee> findAllExecutorsWithDeleted();

    @Query(value = "select emp " +
            "from Employee emp " +
            "where emp.department.id = :departmentId " +
            "and coalesce(emp.archived, false) is false")
    List<Employee> findAllByDepartmentId(@Param("departmentId") Long departmentId);

    @Query(value = "select emp " +
            "from Employee emp " +
            "where emp.department.id = :departmentId")
    List<Employee> findAllByDepartmentIdWithDeleted(@Param("departmentId") Long departmentId);

    @Query(value = "select emp " +
            "from Employee emp " +
            "join Principal p on emp.username = p.username " +
            "where emp.department.id = :departmentId " +
            "and p.role = 'LEAD' " +
            "and coalesce(emp.archived, false) is false")
    Optional<Employee> findLeadByDepartmentId(@Param("departmentId") Long departmentId);

    boolean existsByUsername(String username);
}
