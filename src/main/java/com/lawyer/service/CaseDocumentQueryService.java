package com.lawyer.service;

import com.lawyer.domain.*; // for static metamodels
import com.lawyer.domain.CaseDocument;
import com.lawyer.repository.CaseDocumentRepository;
import com.lawyer.service.criteria.CaseDocumentCriteria;
import com.lawyer.service.dto.CaseDocumentDTO;
import com.lawyer.service.mapper.CaseDocumentMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CaseDocument} entities in the database.
 * The main input is a {@link CaseDocumentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CaseDocumentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CaseDocumentQueryService extends QueryService<CaseDocument> {

    private static final Logger LOG = LoggerFactory.getLogger(CaseDocumentQueryService.class);

    private final CaseDocumentRepository caseDocumentRepository;

    private final CaseDocumentMapper caseDocumentMapper;

    public CaseDocumentQueryService(CaseDocumentRepository caseDocumentRepository, CaseDocumentMapper caseDocumentMapper) {
        this.caseDocumentRepository = caseDocumentRepository;
        this.caseDocumentMapper = caseDocumentMapper;
    }

    /**
     * Return a {@link Page} of {@link CaseDocumentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CaseDocumentDTO> findByCriteria(CaseDocumentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CaseDocument> specification = createSpecification(criteria);
        return caseDocumentRepository.findAll(specification, page).map(caseDocumentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CaseDocumentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CaseDocument> specification = createSpecification(criteria);
        return caseDocumentRepository.count(specification);
    }

    /**
     * Function to convert {@link CaseDocumentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CaseDocument> createSpecification(CaseDocumentCriteria criteria) {
        Specification<CaseDocument> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CaseDocument_.id));
            }
            if (criteria.getDocumentName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumentName(), CaseDocument_.documentName));
            }
            if (criteria.getDocumentType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumentType(), CaseDocument_.documentType));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), CaseDocument_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), CaseDocument_.updatedAt));
            }
            if (criteria.getCourtCaseId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCourtCaseId(), root ->
                        root.join(CaseDocument_.courtCase, JoinType.LEFT).get(CourtCase_.id)
                    )
                );
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(CaseDocument_.user, JoinType.LEFT).get(User_.id))
                );
            }
        }
        return specification;
    }
}
