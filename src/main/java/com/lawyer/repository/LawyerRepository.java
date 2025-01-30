package com.lawyer.repository;

import com.lawyer.domain.Lawyer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Lawyer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LawyerRepository extends JpaRepository<Lawyer, Long> {}
