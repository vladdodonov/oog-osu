package com.dodonov.oogosu.service.impl;

import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.domain.dict.Topic;
import com.dodonov.oogosu.dto.TopicAddDto;
import com.dodonov.oogosu.dto.TopicDto;
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
    public List<Topic> findAllByDepartmentIdWithDeleted(Long id) {
        return topicRepository.findAllByDepartment(Department.builder().id(id).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Topic addTopicToDepartment(TopicAddDto dto) {
        var dep = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(EntityNotFoundException::new);
        if (isTrue(dep.getArchived())){
            throw new RuntimeException("Пытаетесь добавить тему к удаленному департаменту");
        }
        var topic = new Topic();
        topic.setName(dto.getName());
        topic.setDepartment(dep);
        return topicRepository.save(topic);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long topicId) {
        topicRepository.archive(topicId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Topic restore(Long topicId) {
        var topic = topicRepository.findById(topicId).orElseThrow(EntityNotFoundException::new);
        if (isTrue(topic.getDepartment().getArchived())){
            throw new RuntimeException("Тема принадлежит к архивированному департаменту");
        }
        topic.setArchived(null);
        return topicRepository.save(topic);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Topic changeTopicName(TopicDto dto) {
        var topic = topicRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        topic.setName(dto.getName());
        return topicRepository.save(topic);
    }
}
