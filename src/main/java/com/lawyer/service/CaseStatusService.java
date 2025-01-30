package com.lawyer.service;

import com.lawyer.domain.CaseStatus;
import com.lawyer.repository.CaseStatusRepository;
import com.lawyer.service.dto.CaseStatusDTO;
import com.lawyer.service.mapper.CaseStatusMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lawyer.domain.CaseStatus}.
 */
@Service
@Transactional
public class CaseStatusService {

    private static final Logger LOG = LoggerFactory.getLogger(CaseStatusService.class);

    private final CaseStatusRepository caseStatusRepository;

    private final CaseStatusMapper caseStatusMapper;

    public CaseStatusService(CaseStatusRepository caseStatusRepository, CaseStatusMapper caseStatusMapper) {
        this.caseStatusRepository = caseStatusRepository;
        this.caseStatusMapper = caseStatusMapper;
    }

    /**
     * Save a caseStatus.
     *
     * @param caseStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public CaseStatusDTO save(CaseStatusDTO caseStatusDTO) {
        LOG.debug("Request to save CaseStatus : {}", caseStatusDTO);
        CaseStatus caseStatus = caseStatusMapper.toEntity(caseStatusDTO);
        caseStatus = caseStatusRepository.save(caseStatus);
        return caseStatusMapper.toDto(caseStatus);
    }

    /**
     * Update a caseStatus.
     *
     * @param caseStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public CaseStatusDTO update(CaseStatusDTO caseStatusDTO) {
        LOG.debug("Request to update CaseStatus : {}", caseStatusDTO);
        CaseStatus caseStatus = caseStatusMapper.toEntity(caseStatusDTO);
        caseStatus = caseStatusRepository.save(caseStatus);
        return caseStatusMapper.toDto(caseStatus);
    }

    /**
     * Partially update a caseStatus.
     *
     * @param caseStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CaseStatusDTO> partialUpdate(CaseStatusDTO caseStatusDTO) {
        LOG.debug("Request to partially update CaseStatus : {}", caseStatusDTO);

        return caseStatusRepository
            .findById(caseStatusDTO.getId())
            .map(existingCaseStatus -> {
                caseStatusMapper.partialUpdate(existingCaseStatus, caseStatusDTO);

                return existingCaseStatus;
            })
            .map(caseStatusRepository::save)
            .map(caseStatusMapper::toDto);
    }

    /**
     * Get all the caseStatuses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CaseStatusDTO> findAll() {
        LOG.debug("Request to get all CaseStatuses");
        return caseStatusRepository.findAll().stream().map(caseStatusMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one caseStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CaseStatusDTO> findOne(Long id) {
        LOG.debug("Request to get CaseStatus : {}", id);
        return caseStatusRepository.findById(id).map(caseStatusMapper::toDto);
    }

    /**
     * Delete the caseStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CaseStatus : {}", id);
        caseStatusRepository.deleteById(id);
    }
}
