package com.dodonov.oogosu.service;

import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.domain.dict.Topic;

import java.util.List;

public interface TopicService {
    List<Topic> findAll();
    List<Topic> findAllByDepartment(Department department);
}
