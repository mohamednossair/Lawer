package com.lawyer.repository;

import com.lawyer.domain.CourtCaseType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CourtCaseType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourtCaseTypeRepository extends JpaRepository<CourtCaseType, Long> {}
