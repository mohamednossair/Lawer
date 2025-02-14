package com.lawyer.service;

import com.lawyer.domain.CourtCase;
import com.lawyer.repository.CourtCaseRepository;
import com.lawyer.service.dto.CourtCaseDTO;
import com.lawyer.service.mapper.CourtCaseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lawyer.domain.CourtCase}.
 */
@Service
@Transactional
public class CourtCaseService {

    private static final Logger LOG = LoggerFactory.getLogger(CourtCaseService.class);

    private final CourtCaseRepository courtCaseRepository;

    private final CourtCaseMapper courtCaseMapper;

    public CourtCaseService(CourtCaseRepository courtCaseRepository, CourtCaseMapper courtCaseMapper) {
        this.courtCaseRepository = courtCaseRepository;
        this.courtCaseMapper = courtCaseMapper;
    }

    /**
     * Save a courtCase.
     *
     * @param courtCaseDTO the entity to save.
     * @return the persisted entity.
     */
    public CourtCaseDTO save(CourtCaseDTO courtCaseDTO) {
        LOG.debug("Request to save CourtCase : {}", courtCaseDTO);
        CourtCase courtCase = courtCaseMapper.toEntity(courtCaseDTO);
        courtCase = courtCaseRepository.save(courtCase);
        return courtCaseMapper.toDto(courtCase);
    }

    /**
     * Update a courtCase.
     *
     * @param courtCaseDTO the entity to save.
     * @return the persisted entity.
     */
    public CourtCaseDTO update(CourtCaseDTO courtCaseDTO) {
        LOG.debug("Request to update CourtCase : {}", courtCaseDTO);
        CourtCase courtCase = courtCaseMapper.toEntity(courtCaseDTO);
        courtCase = courtCaseRepository.save(courtCase);
        return courtCaseMapper.toDto(courtCase);
    }

    /**
     * Partially update a courtCase.
     *
     * @param courtCaseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CourtCaseDTO> partialUpdate(CourtCaseDTO courtCaseDTO) {
        LOG.debug("Request to partially update CourtCase : {}", courtCaseDTO);

        return courtCaseRepository
            .findById(courtCaseDTO.getId())
            .map(existingCourtCase -> {
                courtCaseMapper.partialUpdate(existingCourtCase, courtCaseDTO);

                return existingCourtCase;
            })
            .map(courtCaseRepository::save)
            .map(courtCaseMapper::toDto);
    }

    /**
     * Get all the courtCases with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CourtCaseDTO> findAllWithEagerRelationships(Pageable pageable) {
        return courtCaseRepository.findAllWithEagerRelationships(pageable).map(courtCaseMapper::toDto);
    }

    /**
     * Get one courtCase by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CourtCaseDTO> findOne(Long id) {
        LOG.debug("Request to get CourtCase : {}", id);
        return courtCaseRepository.findOneWithEagerRelationships(id).map(courtCaseMapper::toDto);
    }

    /**
     * Delete the courtCase by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CourtCase : {}", id);
        courtCaseRepository.deleteById(id);
    }
}
