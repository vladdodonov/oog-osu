package com.dodonov.oogosu.service.impl;

import com.dodonov.oogosu.domain.Appeal;
import com.dodonov.oogosu.domain.enums.State;
import com.dodonov.oogosu.dto.appeal.AppealCheckStatusDto;
import com.dodonov.oogosu.dto.appeal.AppealCriteria;
import com.dodonov.oogosu.dto.appeal.AppealDto;
import com.dodonov.oogosu.repository.AppealRepository;
import com.dodonov.oogosu.repository.CitizenRepository;
import com.dodonov.oogosu.repository.TopicRepository;
import com.dodonov.oogosu.service.AppealService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
        var topic = topicRepository.findById(appeal.getTopic().getId()).orElseThrow(EntityNotFoundException::new);
        appeal = Appeal.builder()
                .state(State.NEW)
                .creationDate(LocalDateTime.now())
                .citizen(citizenRepository.save(appeal.getCitizen()))
                .topic(topic)
                .department(topic.getDepartment())
                .isProlonged(false)
                .question(appeal.getQuestion())
                .build();
        return appealRepository.save(appeal).getId();
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public AppealCheckStatusDto checkStatus(AppealCheckStatusDto dto) {
        var appeal = appealRepository.findByIdAndCitizen_lastNameIgnoreCase(dto.getId(), dto.getCitizenLastName())
                .orElseThrow(EntityNotFoundException::new);
        dto.setState(appeal.getState());
        return dto;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Page<AppealDto> findByCriteria(AppealCriteria dto) {
        return appealRepository.findByCriteria(dto);
    }
}
