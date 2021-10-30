package com.dodonov.oogosu.repository;

import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.domain.dict.Topic;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicRepository extends BaseRepository<Topic> {
    List<Topic> findAllByDepartment(Department department);
    @Modifying
    @Query(value = "update d_topic set archived = true where id = :id", nativeQuery = true)
    void archive(@Param("id") Long id);
    @Modifying
    @Query(value = "update d_topic set archived = true where department_id = :id", nativeQuery = true)
    void archiveByDepartment(@Param("id") Long departmentId);
}
