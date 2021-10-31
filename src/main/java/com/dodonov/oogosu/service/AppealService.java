package com.dodonov.oogosu.service;

import com.dodonov.oogosu.domain.Appeal;
import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.domain.enums.Difficulty;
import com.dodonov.oogosu.dto.appeal.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface AppealService {
    Long createAppeal(Appeal appeal);

    AppealCheckStatusDto checkStatus(AppealCheckStatusDto dto);

    Page<AppealDto> findByCriteria(AppealCriteria dto);

    Appeal prolong(Long id, LocalDateTime dueDate);

    Appeal appoint(AppealAppointmentDto dto);

    Appeal answer(AppealAnswerDto appealDto);

    Appeal returnToExecutor(AppealReturnDto appealDto);

    Appeal sendAnswer(Long id);

    Appeal findById(Long id);
}
