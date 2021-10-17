package com.dodonov.oogosu.repository;



import com.dodonov.oogosu.config.security.Principal;

import java.util.Optional;

public interface PrincipalRepository extends BaseRepository<Principal> {
    Optional<Principal> findByUsername(String username);
}
