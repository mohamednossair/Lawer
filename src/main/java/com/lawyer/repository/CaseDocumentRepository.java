package com.lawyer.repository;

import com.lawyer.domain.CaseDocument;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CaseDocument entity.
 */
@Repository
public interface CaseDocumentRepository extends JpaRepository<CaseDocument, Long>, JpaSpecificationExecutor<CaseDocument> {
    @Query("select caseDocument from CaseDocument caseDocument where caseDocument.user.login = ?#{authentication.name}")
    List<CaseDocument> findByUserIsCurrentUser();

    default Optional<CaseDocument> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CaseDocument> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CaseDocument> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select caseDocument from CaseDocument caseDocument left join fetch caseDocument.client left join fetch caseDocument.courtCase left join fetch caseDocument.user",
        countQuery = "select count(caseDocument) from CaseDocument caseDocument"
    )
    Page<CaseDocument> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select caseDocument from CaseDocument caseDocument left join fetch caseDocument.client left join fetch caseDocument.courtCase left join fetch caseDocument.user"
    )
    List<CaseDocument> findAllWithToOneRelationships();

    @Query(
        "select caseDocument from CaseDocument caseDocument left join fetch caseDocument.client left join fetch caseDocument.courtCase left join fetch caseDocument.user where caseDocument.id =:id"
    )
    Optional<CaseDocument> findOneWithToOneRelationships(@Param("id") Long id);
}
