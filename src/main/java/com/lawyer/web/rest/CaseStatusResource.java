package com.lawyer.web.rest;

import com.lawyer.repository.CaseStatusRepository;
import com.lawyer.service.CaseStatusService;
import com.lawyer.service.dto.CaseStatusDTO;
import com.lawyer.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lawyer.domain.CaseStatus}.
 */
@RestController
@RequestMapping("/api/case-statuses")
public class CaseStatusResource {

    private static final Logger LOG = LoggerFactory.getLogger(CaseStatusResource.class);

    private static final String ENTITY_NAME = "caseStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CaseStatusService caseStatusService;

    private final CaseStatusRepository caseStatusRepository;

    public CaseStatusResource(CaseStatusService caseStatusService, CaseStatusRepository caseStatusRepository) {
        this.caseStatusService = caseStatusService;
        this.caseStatusRepository = caseStatusRepository;
    }

    /**
     * {@code POST  /case-statuses} : Create a new caseStatus.
     *
     * @param caseStatusDTO the caseStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new caseStatusDTO, or with status {@code 400 (Bad Request)} if the caseStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CaseStatusDTO> createCaseStatus(@Valid @RequestBody CaseStatusDTO caseStatusDTO) throws URISyntaxException {
        LOG.debug("REST request to save CaseStatus : {}", caseStatusDTO);
        if (caseStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new caseStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        caseStatusDTO = caseStatusService.save(caseStatusDTO);
        return ResponseEntity.created(new URI("/api/case-statuses/" + caseStatusDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, caseStatusDTO.getId().toString()))
            .body(caseStatusDTO);
    }

    /**
     * {@code PUT  /case-statuses/:id} : Updates an existing caseStatus.
     *
     * @param id the id of the caseStatusDTO to save.
     * @param caseStatusDTO the caseStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caseStatusDTO,
     * or with status {@code 400 (Bad Request)} if the caseStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the caseStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CaseStatusDTO> updateCaseStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CaseStatusDTO caseStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CaseStatus : {}, {}", id, caseStatusDTO);
        if (caseStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caseStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caseStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        caseStatusDTO = caseStatusService.update(caseStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caseStatusDTO.getId().toString()))
            .body(caseStatusDTO);
    }

    /**
     * {@code PATCH  /case-statuses/:id} : Partial updates given fields of an existing caseStatus, field will ignore if it is null
     *
     * @param id the id of the caseStatusDTO to save.
     * @param caseStatusDTO the caseStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caseStatusDTO,
     * or with status {@code 400 (Bad Request)} if the caseStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the caseStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the caseStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CaseStatusDTO> partialUpdateCaseStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CaseStatusDTO caseStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CaseStatus partially : {}, {}", id, caseStatusDTO);
        if (caseStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caseStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caseStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CaseStatusDTO> result = caseStatusService.partialUpdate(caseStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caseStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /case-statuses} : get all the caseStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of caseStatuses in body.
     */
    @GetMapping("")
    public List<CaseStatusDTO> getAllCaseStatuses() {
        LOG.debug("REST request to get all CaseStatuses");
        return caseStatusService.findAll();
    }

    /**
     * {@code GET  /case-statuses/:id} : get the "id" caseStatus.
     *
     * @param id the id of the caseStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the caseStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CaseStatusDTO> getCaseStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CaseStatus : {}", id);
        Optional<CaseStatusDTO> caseStatusDTO = caseStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(caseStatusDTO);
    }

    /**
     * {@code DELETE  /case-statuses/:id} : delete the "id" caseStatus.
     *
     * @param id the id of the caseStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCaseStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CaseStatus : {}", id);
        caseStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
