package com.dodonov.oogosu.repository;

import com.dodonov.oogosu.domain.dict.Department;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends BaseRepository<Department> {
    @Modifying
    @Query(value = "update d_department set archived = true where id = :id", nativeQuery = true)
    void archive(@Param("id") Long id);
}
