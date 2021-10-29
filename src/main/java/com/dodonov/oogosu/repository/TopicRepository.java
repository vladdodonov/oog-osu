package com.dodonov.oogosu.repository;

import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.domain.dict.Topic;

import java.util.List;

public interface TopicRepository extends BaseRepository<Topic> {
    List<Topic> findAllByDepartment(Department department);
}
