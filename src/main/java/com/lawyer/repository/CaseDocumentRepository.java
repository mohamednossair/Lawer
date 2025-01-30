package com.lawyer.repository;

import com.lawyer.domain.CaseDocument;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CaseDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaseDocumentRepository extends JpaRepository<CaseDocument, Long> {
    @Query("select caseDocument from CaseDocument caseDocument where caseDocument.user.login = ?#{authentication.name}")
    List<CaseDocument> findByUserIsCurrentUser();
}
