package com.dodonov.oogosu.repository.extend;

import com.dodonov.oogosu.dto.appeal.AppealCriteria;
import com.dodonov.oogosu.dto.appeal.AppealDto;
import org.springframework.data.domain.Page;

public interface AppealExtendRepository {
    Page<AppealDto> findByCriteria(AppealCriteria criteria);
}
