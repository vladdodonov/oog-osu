package com.dodonov.oogosu.service;

import com.dodonov.oogosu.domain.Appeal;
import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.dto.appeal.AppealCheckStatusDto;
import com.dodonov.oogosu.dto.appeal.AppealCriteria;
import com.dodonov.oogosu.dto.appeal.AppealDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface AppealService {
    Long createAppeal(Appeal appeal);

    AppealCheckStatusDto checkStatus(AppealCheckStatusDto dto);

    Page<AppealDto> findByCriteria(AppealCriteria dto);

    Appeal prolong(Long id, LocalDateTime dueDate);

    Appeal appoint(Employee employee, Long appealId, Boolean isComplaint);

    Appeal answer(AppealDto appealDto);

    Appeal returnToExecutor(AppealDto appealDto);

    Appeal sendAnswer(AppealDto appealDto);
}
