package com.lawyer.service;

import com.lawyer.domain.CaseDocument;
import com.lawyer.repository.CaseDocumentRepository;
import com.lawyer.service.dto.CaseDocumentDTO;
import com.lawyer.service.mapper.CaseDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lawyer.domain.CaseDocument}.
 */
@Service
@Transactional
public class CaseDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(CaseDocumentService.class);

    private final CaseDocumentRepository caseDocumentRepository;

    private final CaseDocumentMapper caseDocumentMapper;

    public CaseDocumentService(CaseDocumentRepository caseDocumentRepository, CaseDocumentMapper caseDocumentMapper) {
        this.caseDocumentRepository = caseDocumentRepository;
        this.caseDocumentMapper = caseDocumentMapper;
    }

    /**
     * Save a caseDocument.
     *
     * @param caseDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public CaseDocumentDTO save(CaseDocumentDTO caseDocumentDTO) {
        LOG.debug("Request to save CaseDocument : {}", caseDocumentDTO);
        CaseDocument caseDocument = caseDocumentMapper.toEntity(caseDocumentDTO);
        caseDocument = caseDocumentRepository.save(caseDocument);
        return caseDocumentMapper.toDto(caseDocument);
    }

    /**
     * Update a caseDocument.
     *
     * @param caseDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public CaseDocumentDTO update(CaseDocumentDTO caseDocumentDTO) {
        LOG.debug("Request to update CaseDocument : {}", caseDocumentDTO);
        CaseDocument caseDocument = caseDocumentMapper.toEntity(caseDocumentDTO);
        caseDocument = caseDocumentRepository.save(caseDocument);
        return caseDocumentMapper.toDto(caseDocument);
    }

    /**
     * Partially update a caseDocument.
     *
     * @param caseDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CaseDocumentDTO> partialUpdate(CaseDocumentDTO caseDocumentDTO) {
        LOG.debug("Request to partially update CaseDocument : {}", caseDocumentDTO);

        return caseDocumentRepository
            .findById(caseDocumentDTO.getId())
            .map(existingCaseDocument -> {
                caseDocumentMapper.partialUpdate(existingCaseDocument, caseDocumentDTO);

                return existingCaseDocument;
            })
            .map(caseDocumentRepository::save)
            .map(caseDocumentMapper::toDto);
    }

    /**
     * Get all the caseDocuments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CaseDocumentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return caseDocumentRepository.findAllWithEagerRelationships(pageable).map(caseDocumentMapper::toDto);
    }

    /**
     * Get one caseDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CaseDocumentDTO> findOne(Long id) {
        LOG.debug("Request to get CaseDocument : {}", id);
        return caseDocumentRepository.findOneWithEagerRelationships(id).map(caseDocumentMapper::toDto);
    }

    /**
     * Delete the caseDocument by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CaseDocument : {}", id);
        caseDocumentRepository.deleteById(id);
    }
}
