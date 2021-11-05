package com.dodonov.oogosu.service;

import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.domain.dict.Topic;
import com.dodonov.oogosu.dto.TopicDto;

import java.util.List;

public interface TopicService {
    List<Topic> findAll();

    List<Topic> findAllWithDeleted();

    List<Topic> findAllByDepartment(Department department);

    Topic addTopicToDepartment(Topic topic);

    void deleteById(Long topicId);

    Topic restore(Long topicId);

    Topic changeTopicName(TopicDto dto);
}
