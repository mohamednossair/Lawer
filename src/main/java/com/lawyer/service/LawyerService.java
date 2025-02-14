package com.lawyer.service;

import com.lawyer.domain.Lawyer;
import com.lawyer.repository.LawyerRepository;
import com.lawyer.service.dto.LawyerDTO;
import com.lawyer.service.mapper.LawyerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.lawyer.domain.Lawyer}.
 */
@Service
@Transactional
public class LawyerService {

    private static final Logger LOG = LoggerFactory.getLogger(LawyerService.class);

    private final LawyerRepository lawyerRepository;

    private final LawyerMapper lawyerMapper;

    public LawyerService(LawyerRepository lawyerRepository, LawyerMapper lawyerMapper) {
        this.lawyerRepository = lawyerRepository;
        this.lawyerMapper = lawyerMapper;
    }

    /**
     * Save a lawyer.
     *
     * @param lawyerDTO the entity to save.
     * @return the persisted entity.
     */
    public LawyerDTO save(LawyerDTO lawyerDTO) {
        LOG.debug("Request to save Lawyer : {}", lawyerDTO);
        Lawyer lawyer = lawyerMapper.toEntity(lawyerDTO);
        lawyer = lawyerRepository.save(lawyer);
        return lawyerMapper.toDto(lawyer);
    }

    /**
     * Update a lawyer.
     *
     * @param lawyerDTO the entity to save.
     * @return the persisted entity.
     */
    public LawyerDTO update(LawyerDTO lawyerDTO) {
        LOG.debug("Request to update Lawyer : {}", lawyerDTO);
        Lawyer lawyer = lawyerMapper.toEntity(lawyerDTO);
        lawyer = lawyerRepository.save(lawyer);
        return lawyerMapper.toDto(lawyer);
    }

    /**
     * Partially update a lawyer.
     *
     * @param lawyerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LawyerDTO> partialUpdate(LawyerDTO lawyerDTO) {
        LOG.debug("Request to partially update Lawyer : {}", lawyerDTO);

        return lawyerRepository
            .findById(lawyerDTO.getId())
            .map(existingLawyer -> {
                lawyerMapper.partialUpdate(existingLawyer, lawyerDTO);

                return existingLawyer;
            })
            .map(lawyerRepository::save)
            .map(lawyerMapper::toDto);
    }

    /**
     * Get one lawyer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LawyerDTO> findOne(Long id) {
        LOG.debug("Request to get Lawyer : {}", id);
        return lawyerRepository.findById(id).map(lawyerMapper::toDto);
    }

    /**
     * Delete the lawyer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Lawyer : {}", id);
        lawyerRepository.deleteById(id);
    }
}
