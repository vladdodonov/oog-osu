package com.dodonov.oogosu.repository;

import com.dodonov.oogosu.domain.Appeal;

import java.util.Optional;

public interface AppealRepository extends BaseRepository<Appeal> {
    Optional<Appeal> findByIdAndCitizen_lastName(Long id, String lastName);
}
