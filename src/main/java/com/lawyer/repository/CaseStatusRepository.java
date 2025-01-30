package com.lawyer.repository;

import com.lawyer.domain.CaseStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CaseStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaseStatusRepository extends JpaRepository<CaseStatus, Long> {}
