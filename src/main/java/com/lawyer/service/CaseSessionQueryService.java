package com.lawyer.service;

import com.lawyer.domain.*; // for static metamodels
import com.lawyer.domain.CaseSession;
import com.lawyer.repository.CaseSessionRepository;
import com.lawyer.service.criteria.CaseSessionCriteria;
import com.lawyer.service.dto.CaseSessionDTO;
import com.lawyer.service.mapper.CaseSessionMapper;
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
 * Service for executing complex queries for {@link CaseSession} entities in the database.
 * The main input is a {@link CaseSessionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CaseSessionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CaseSessionQueryService extends QueryService<CaseSession> {

    private static final Logger LOG = LoggerFactory.getLogger(CaseSessionQueryService.class);

    private final CaseSessionRepository caseSessionRepository;

    private final CaseSessionMapper caseSessionMapper;

    public CaseSessionQueryService(CaseSessionRepository caseSessionRepository, CaseSessionMapper caseSessionMapper) {
        this.caseSessionRepository = caseSessionRepository;
        this.caseSessionMapper = caseSessionMapper;
    }

    /**
     * Return a {@link Page} of {@link CaseSessionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CaseSessionDTO> findByCriteria(CaseSessionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CaseSession> specification = createSpecification(criteria);
        return caseSessionRepository.findAll(specification, page).map(caseSessionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CaseSessionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CaseSession> specification = createSpecification(criteria);
        return caseSessionRepository.count(specification);
    }

    /**
     * Function to convert {@link CaseSessionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CaseSession> createSpecification(CaseSessionCriteria criteria) {
        Specification<CaseSession> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CaseSession_.id));
            }
            if (criteria.getSessionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSessionDate(), CaseSession_.sessionDate));
            }
            if (criteria.getSessionTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSessionTime(), CaseSession_.sessionTime));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CaseSession_.description));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), CaseSession_.notes));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), CaseSession_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), CaseSession_.updatedAt));
            }
            if (criteria.getCourtCaseId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCourtCaseId(), root ->
                        root.join(CaseSession_.courtCase, JoinType.LEFT).get(CourtCase_.id)
                    )
                );
            }
        }
        return specification;
    }
}
