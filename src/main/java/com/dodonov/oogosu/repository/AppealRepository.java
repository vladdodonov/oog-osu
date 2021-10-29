package com.dodonov.oogosu.repository;

import com.dodonov.oogosu.domain.Appeal;
import com.dodonov.oogosu.repository.extend.AppealExtendRepository;

import java.util.Optional;

public interface AppealRepository extends BaseRepository<Appeal>, AppealExtendRepository {
    Optional<Appeal> findByIdAndCitizen_lastNameIgnoreCase(Long id, String lastName);
}
