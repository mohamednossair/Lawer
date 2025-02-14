package com.lawyer.repository;

import com.lawyer.domain.CourtCase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CourtCase entity.
 */
@Repository
public interface CourtCaseRepository extends JpaRepository<CourtCase, Long>, JpaSpecificationExecutor<CourtCase> {
    default Optional<CourtCase> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CourtCase> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CourtCase> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select courtCase from CourtCase courtCase left join fetch courtCase.court left join fetch courtCase.client left join fetch courtCase.courtCaseType left join fetch courtCase.caseStatus left join fetch courtCase.opponentLawyerId",
        countQuery = "select count(courtCase) from CourtCase courtCase"
    )
    Page<CourtCase> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select courtCase from CourtCase courtCase left join fetch courtCase.court left join fetch courtCase.client left join fetch courtCase.courtCaseType left join fetch courtCase.caseStatus left join fetch courtCase.opponentLawyerId"
    )
    List<CourtCase> findAllWithToOneRelationships();

    @Query(
        "select courtCase from CourtCase courtCase left join fetch courtCase.court left join fetch courtCase.client left join fetch courtCase.courtCaseType left join fetch courtCase.caseStatus left join fetch courtCase.opponentLawyerId where courtCase.id =:id"
    )
    Optional<CourtCase> findOneWithToOneRelationships(@Param("id") Long id);
}
