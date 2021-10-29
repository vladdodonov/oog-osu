package com.dodonov.oogosu.service;

import com.dodonov.oogosu.domain.Appeal;
import com.dodonov.oogosu.dto.appeal.AppealCheckStatusDto;
import com.dodonov.oogosu.dto.appeal.AppealCriteria;
import com.dodonov.oogosu.dto.appeal.AppealDto;
import org.springframework.data.domain.Page;

public interface AppealService {
    Long createAppeal(Appeal appeal);

    AppealCheckStatusDto checkStatus(AppealCheckStatusDto dto);

    Page<AppealDto> findByCriteria(AppealCriteria dto);
}
