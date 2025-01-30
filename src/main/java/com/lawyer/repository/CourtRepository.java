package com.lawyer.repository;

import com.lawyer.domain.Court;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Court entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {}
