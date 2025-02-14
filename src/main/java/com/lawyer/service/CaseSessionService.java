package com.lawyer.service;

import com.lawyer.domain.CaseSession;
import com.lawyer.repository.CaseSessionRepository;
import com.lawyer.service.dto.CaseSessionDTO;
import com.lawyer.service.mapper.CaseSessionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lawyer.domain.CaseSession}.
 */
@Service
@Transactional
public class CaseSessionService {

    private static final Logger LOG = LoggerFactory.getLogger(CaseSessionService.class);

    private final CaseSessionRepository caseSessionRepository;

    private final CaseSessionMapper caseSessionMapper;

    public CaseSessionService(CaseSessionRepository caseSessionRepository, CaseSessionMapper caseSessionMapper) {
        this.caseSessionRepository = caseSessionRepository;
        this.caseSessionMapper = caseSessionMapper;
    }

    /**
     * Save a caseSession.
     *
     * @param caseSessionDTO the entity to save.
     * @return the persisted entity.
     */
    public CaseSessionDTO save(CaseSessionDTO caseSessionDTO) {
        LOG.debug("Request to save CaseSession : {}", caseSessionDTO);
        CaseSession caseSession = caseSessionMapper.toEntity(caseSessionDTO);
        caseSession = caseSessionRepository.save(caseSession);
        return caseSessionMapper.toDto(caseSession);
    }

    /**
     * Update a caseSession.
     *
     * @param caseSessionDTO the entity to save.
     * @return the persisted entity.
     */
    public CaseSessionDTO update(CaseSessionDTO caseSessionDTO) {
        LOG.debug("Request to update CaseSession : {}", caseSessionDTO);
        CaseSession caseSession = caseSessionMapper.toEntity(caseSessionDTO);
        caseSession = caseSessionRepository.save(caseSession);
        return caseSessionMapper.toDto(caseSession);
    }

    /**
     * Partially update a caseSession.
     *
     * @param caseSessionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CaseSessionDTO> partialUpdate(CaseSessionDTO caseSessionDTO) {
        LOG.debug("Request to partially update CaseSession : {}", caseSessionDTO);

        return caseSessionRepository
            .findById(caseSessionDTO.getId())
            .map(existingCaseSession -> {
                caseSessionMapper.partialUpdate(existingCaseSession, caseSessionDTO);

                return existingCaseSession;
            })
            .map(caseSessionRepository::save)
            .map(caseSessionMapper::toDto);
    }

    /**
     * Get all the caseSessions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CaseSessionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return caseSessionRepository.findAllWithEagerRelationships(pageable).map(caseSessionMapper::toDto);
    }

    /**
     * Get one caseSession by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CaseSessionDTO> findOne(Long id) {
        LOG.debug("Request to get CaseSession : {}", id);
        return caseSessionRepository.findOneWithEagerRelationships(id).map(caseSessionMapper::toDto);
    }

    /**
     * Delete the caseSession by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CaseSession : {}", id);
        caseSessionRepository.deleteById(id);
    }
}
