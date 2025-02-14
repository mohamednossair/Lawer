package com.lawyer.repository;

import com.lawyer.domain.CaseSession;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CaseSession entity.
 */
@Repository
public interface CaseSessionRepository extends JpaRepository<CaseSession, Long>, JpaSpecificationExecutor<CaseSession> {
    default Optional<CaseSession> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CaseSession> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CaseSession> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select caseSession from CaseSession caseSession left join fetch caseSession.courtCase",
        countQuery = "select count(caseSession) from CaseSession caseSession"
    )
    Page<CaseSession> findAllWithToOneRelationships(Pageable pageable);

    @Query("select caseSession from CaseSession caseSession left join fetch caseSession.courtCase")
    List<CaseSession> findAllWithToOneRelationships();

    @Query("select caseSession from CaseSession caseSession left join fetch caseSession.courtCase where caseSession.id =:id")
    Optional<CaseSession> findOneWithToOneRelationships(@Param("id") Long id);
}
