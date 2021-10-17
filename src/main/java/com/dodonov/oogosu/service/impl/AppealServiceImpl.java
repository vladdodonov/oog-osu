package com.dodonov.oogosu.service.impl;

import com.dodonov.oogosu.domain.Appeal;
import com.dodonov.oogosu.domain.enums.State;
import com.dodonov.oogosu.repository.AppealRepository;
import com.dodonov.oogosu.repository.CitizenRepository;
import com.dodonov.oogosu.repository.TopicRepository;
import com.dodonov.oogosu.service.AppealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppealServiceImpl implements AppealService {
    private final AppealRepository appealRepository;
    private final CitizenRepository citizenRepository;
    private final TopicRepository topicRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAppeal(Appeal appeal) {
        appeal = Appeal.builder()
                .state(State.NEW)
                .creationDate(LocalDateTime.now())
                .citizen(citizenRepository.save(appeal.getCitizen()))
                .topic(topicRepository.findById(appeal.getTopic().getId()).orElseThrow(RuntimeException::new))
                .department(appeal.getTopic().getDepartment())
                .build();
        return appealRepository.save(appeal).getId();
    }
}
