package com.dodonov.oogosu.service.impl;

import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.domain.dict.Topic;
import com.dodonov.oogosu.repository.DepartmentRepository;
import com.dodonov.oogosu.repository.TopicRepository;
import com.dodonov.oogosu.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Topic> findAll() {
        return topicRepository.findAll().stream().filter(a -> a.getArchived() == null).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Topic> findAllWithDeleted() {
        return topicRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Topic> findAllByDepartment(Department department) {
        return topicRepository.findAllByDepartment(department).stream().filter(a -> a.getArchived() == null).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Topic addTopicToDepartment(Topic topic) {
        var dep = departmentRepository.findById(topic.getDepartment().getId())
                .orElseThrow(EntityNotFoundException::new);
        if (isTrue(dep.getArchived())){
            throw new RuntimeException("Пытаетесь добавить тему к удаленному департаменту");
        }
        return topicRepository.save(topic);
    }

    @Override
    @Transactional
    public void deleteById(Long topicId) {
        topicRepository.archive(topicId);
    }
}
