package com.lawyer.service;

import com.lawyer.domain.*; // for static metamodels
import com.lawyer.domain.CourtCase;
import com.lawyer.repository.CourtCaseRepository;
import com.lawyer.service.criteria.CourtCaseCriteria;
import com.lawyer.service.dto.CourtCaseDTO;
import com.lawyer.service.mapper.CourtCaseMapper;
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
 * Service for executing complex queries for {@link CourtCase} entities in the database.
 * The main input is a {@link CourtCaseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CourtCaseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CourtCaseQueryService extends QueryService<CourtCase> {

    private static final Logger LOG = LoggerFactory.getLogger(CourtCaseQueryService.class);

    private final CourtCaseRepository courtCaseRepository;

    private final CourtCaseMapper courtCaseMapper;

    public CourtCaseQueryService(CourtCaseRepository courtCaseRepository, CourtCaseMapper courtCaseMapper) {
        this.courtCaseRepository = courtCaseRepository;
        this.courtCaseMapper = courtCaseMapper;
    }

    /**
     * Return a {@link Page} of {@link CourtCaseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CourtCaseDTO> findByCriteria(CourtCaseCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CourtCase> specification = createSpecification(criteria);
        return courtCaseRepository.findAll(specification, page).map(courtCaseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CourtCaseCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CourtCase> specification = createSpecification(criteria);
        return courtCaseRepository.count(specification);
    }

    /**
     * Function to convert {@link CourtCaseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CourtCase> createSpecification(CourtCaseCriteria criteria) {
        Specification<CourtCase> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CourtCase_.id));
            }
            if (criteria.getCaseNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCaseNumber(), CourtCase_.caseNumber));
            }
            if (criteria.getCaseYear() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCaseYear(), CourtCase_.caseYear));
            }
            if (criteria.getCourtCircuit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCourtCircuit(), CourtCase_.courtCircuit));
            }
            if (criteria.getRegistrationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegistrationDate(), CourtCase_.registrationDate));
            }
            if (criteria.getAttorneyNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAttorneyNumber(), CourtCase_.attorneyNumber));
            }
            if (criteria.getAttorneyYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAttorneyYear(), CourtCase_.attorneyYear));
            }
            if (criteria.getAttorneyAuthentication() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getAttorneyAuthentication(), CourtCase_.attorneyAuthentication)
                );
            }
            if (criteria.getOpponentName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOpponentName(), CourtCase_.opponentName));
            }
            if (criteria.getOpponentDescription() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getOpponentDescription(), CourtCase_.opponentDescription)
                );
            }
            if (criteria.getOpponentAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOpponentAddress(), CourtCase_.opponentAddress));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubject(), CourtCase_.subject));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), CourtCase_.notes));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), CourtCase_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), CourtCase_.updatedAt));
            }
            if (criteria.getCourtId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCourtId(), root -> root.join(CourtCase_.court, JoinType.LEFT).get(Court_.id))
                );
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getClientId(), root -> root.join(CourtCase_.client, JoinType.LEFT).get(Client_.id))
                );
            }
            if (criteria.getCourtCaseTypeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCourtCaseTypeId(), root ->
                        root.join(CourtCase_.courtCaseType, JoinType.LEFT).get(CourtCaseType_.id)
                    )
                );
            }
            if (criteria.getCaseStatusId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCaseStatusId(), root ->
                        root.join(CourtCase_.caseStatus, JoinType.LEFT).get(CaseStatus_.id)
                    )
                );
            }
            if (criteria.getOpponentLawyerIdId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOpponentLawyerIdId(), root ->
                        root.join(CourtCase_.opponentLawyerId, JoinType.LEFT).get(Lawyer_.id)
                    )
                );
            }
        }
        return specification;
    }
}
