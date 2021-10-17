package com.dodonov.oogosu.service;

import com.dodonov.oogosu.domain.Appeal;
import com.dodonov.oogosu.dto.appeal.AppealCheckStatusDto;

public interface AppealService {
    Long createAppeal(Appeal appeal);

    AppealCheckStatusDto checkStatus(AppealCheckStatusDto dto);
}
