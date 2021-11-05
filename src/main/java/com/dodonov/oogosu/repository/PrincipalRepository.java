package com.dodonov.oogosu.repository;


import com.dodonov.oogosu.config.security.Principal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PrincipalRepository extends JpaSpecificationExecutor<Principal>, JpaRepository<Principal, String> {
    Optional<Principal> findByUsername(String username);

    @Query(value = "select p " +
            "from Principal p " +
            "join Employee emp on emp.username = p.username " +
            "where emp.id = :id " +
            "and coalesce(emp.archived, false) is false")
    Optional<Principal> findByEmployeeId(@Param("id") Long id);
    @Modifying
    @Query(value = "update principal set archived = true where id = :id", nativeQuery = true)
    void archive(@Param("id") String username);
}
