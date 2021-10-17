package com.dodonov.oogosu.service.impl;

import com.dodonov.oogosu.domain.dict.Topic;
import com.dodonov.oogosu.repository.TopicRepository;
import com.dodonov.oogosu.service.AppealService;
import com.dodonov.oogosu.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    @Override
    public List<Topic> findAll() {
        return topicRepository.findAll();
    }
}
