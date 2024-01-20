package com.elphie.savings.repositories;

// =============================================================================
// File Name: reposoitories/ISavingsRepository.java
// File Description:
// This file contains the code of the ISavingsRepository Interface that
// handles the queries to the users table in the DB
// =============================================================================

// =============================================================================
// Imports
// =============================================================================
import java.util.List;

import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.elphie.savings.models.Savings;

// =============================================================================
// Interface
// =============================================================================
@Repository
public interface ISavingsRepository extends JpaRepository<Savings, Long> {

    List<Savings> findByUserIdAndNameContaining(Long userId, String name);

}
