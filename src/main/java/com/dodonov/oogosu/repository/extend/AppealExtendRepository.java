package com.dodonov.oogosu.repository.extend;

import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.domain.enums.Qualification;
import com.dodonov.oogosu.dto.appeal.AppealCriteria;
import com.dodonov.oogosu.dto.appeal.AppealDto;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.Set;

public interface AppealExtendRepository {
    Page<AppealDto> findByCriteria(AppealCriteria criteria);
    Map<Employee, Long> getCountAppealsOnExecutorInDepartment(Long departmentId, Set<Qualification> qualifications);
}
