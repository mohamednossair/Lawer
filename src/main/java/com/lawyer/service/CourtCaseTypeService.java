package com.lawyer.service;

import com.lawyer.domain.CourtCaseType;
import com.lawyer.repository.CourtCaseTypeRepository;
import com.lawyer.service.dto.CourtCaseTypeDTO;
import com.lawyer.service.mapper.CourtCaseTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lawyer.domain.CourtCaseType}.
 */
@Service
@Transactional
public class CourtCaseTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CourtCaseTypeService.class);

    private final CourtCaseTypeRepository courtCaseTypeRepository;

    private final CourtCaseTypeMapper courtCaseTypeMapper;

    public CourtCaseTypeService(CourtCaseTypeRepository courtCaseTypeRepository, CourtCaseTypeMapper courtCaseTypeMapper) {
        this.courtCaseTypeRepository = courtCaseTypeRepository;
        this.courtCaseTypeMapper = courtCaseTypeMapper;
    }

    /**
     * Save a courtCaseType.
     *
     * @param courtCaseTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public CourtCaseTypeDTO save(CourtCaseTypeDTO courtCaseTypeDTO) {
        LOG.debug("Request to save CourtCaseType : {}", courtCaseTypeDTO);
        CourtCaseType courtCaseType = courtCaseTypeMapper.toEntity(courtCaseTypeDTO);
        courtCaseType = courtCaseTypeRepository.save(courtCaseType);
        return courtCaseTypeMapper.toDto(courtCaseType);
    }

    /**
     * Update a courtCaseType.
     *
     * @param courtCaseTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public CourtCaseTypeDTO update(CourtCaseTypeDTO courtCaseTypeDTO) {
        LOG.debug("Request to update CourtCaseType : {}", courtCaseTypeDTO);
        CourtCaseType courtCaseType = courtCaseTypeMapper.toEntity(courtCaseTypeDTO);
        courtCaseType = courtCaseTypeRepository.save(courtCaseType);
        return courtCaseTypeMapper.toDto(courtCaseType);
    }

    /**
     * Partially update a courtCaseType.
     *
     * @param courtCaseTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CourtCaseTypeDTO> partialUpdate(CourtCaseTypeDTO courtCaseTypeDTO) {
        LOG.debug("Request to partially update CourtCaseType : {}", courtCaseTypeDTO);

        return courtCaseTypeRepository
            .findById(courtCaseTypeDTO.getId())
            .map(existingCourtCaseType -> {
                courtCaseTypeMapper.partialUpdate(existingCourtCaseType, courtCaseTypeDTO);

                return existingCourtCaseType;
            })
            .map(courtCaseTypeRepository::save)
            .map(courtCaseTypeMapper::toDto);
    }

    /**
     * Get all the courtCaseTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CourtCaseTypeDTO> findAll() {
        LOG.debug("Request to get all CourtCaseTypes");
        return courtCaseTypeRepository.findAll().stream().map(courtCaseTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one courtCaseType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CourtCaseTypeDTO> findOne(Long id) {
        LOG.debug("Request to get CourtCaseType : {}", id);
        return courtCaseTypeRepository.findById(id).map(courtCaseTypeMapper::toDto);
    }

    /**
     * Delete the courtCaseType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CourtCaseType : {}", id);
        courtCaseTypeRepository.deleteById(id);
    }
}
