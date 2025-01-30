package com.lawyer.repository;

import com.lawyer.domain.CaseSession;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CaseSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaseSessionRepository extends JpaRepository<CaseSession, Long> {}
