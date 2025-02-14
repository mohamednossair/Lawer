package com.lawyer.service;

import com.lawyer.domain.*; // for static metamodels
import com.lawyer.domain.Lawyer;
import com.lawyer.repository.LawyerRepository;
import com.lawyer.service.criteria.LawyerCriteria;
import com.lawyer.service.dto.LawyerDTO;
import com.lawyer.service.mapper.LawyerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Lawyer} entities in the database.
 * The main input is a {@link LawyerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link LawyerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LawyerQueryService extends QueryService<Lawyer> {

    private static final Logger LOG = LoggerFactory.getLogger(LawyerQueryService.class);

    private final LawyerRepository lawyerRepository;

    private final LawyerMapper lawyerMapper;

    public LawyerQueryService(LawyerRepository lawyerRepository, LawyerMapper lawyerMapper) {
        this.lawyerRepository = lawyerRepository;
        this.lawyerMapper = lawyerMapper;
    }

    /**
     * Return a {@link Page} of {@link LawyerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LawyerDTO> findByCriteria(LawyerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Lawyer> specification = createSpecification(criteria);
        return lawyerRepository.findAll(specification, page).map(lawyerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LawyerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Lawyer> specification = createSpecification(criteria);
        return lawyerRepository.count(specification);
    }

    /**
     * Function to convert {@link LawyerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Lawyer> createSpecification(LawyerCriteria criteria) {
        Specification<Lawyer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Lawyer_.id));
            }
            if (criteria.getLawyerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLawyerName(), Lawyer_.lawyerName));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Lawyer_.address));
            }
            if (criteria.getContactNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactNumber(), Lawyer_.contactNumber));
            }
            if (criteria.getSpecialization() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpecialization(), Lawyer_.specialization));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Lawyer_.email));
            }
            if (criteria.getRegistrationNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRegistrationNumber(), Lawyer_.registrationNumber));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Lawyer_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Lawyer_.updatedAt));
            }
        }
        return specification;
    }
}
