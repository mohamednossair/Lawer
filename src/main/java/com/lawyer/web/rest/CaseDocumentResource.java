package com.lawyer.web.rest;

import com.lawyer.repository.CaseDocumentRepository;
import com.lawyer.service.CaseDocumentQueryService;
import com.lawyer.service.CaseDocumentService;
import com.lawyer.service.criteria.CaseDocumentCriteria;
import com.lawyer.service.dto.CaseDocumentDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lawyer.domain.CaseDocument}.
 */
@RestController
@RequestMapping("/api/case-documents")
public class CaseDocumentResource {

    private static final Logger LOG = LoggerFactory.getLogger(CaseDocumentResource.class);

    private static final String ENTITY_NAME = "caseDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CaseDocumentService caseDocumentService;

    private final CaseDocumentRepository caseDocumentRepository;

    private final CaseDocumentQueryService caseDocumentQueryService;

    public CaseDocumentResource(
        CaseDocumentService caseDocumentService,
        CaseDocumentRepository caseDocumentRepository,
        CaseDocumentQueryService caseDocumentQueryService
    ) {
        this.caseDocumentService = caseDocumentService;
        this.caseDocumentRepository = caseDocumentRepository;
        this.caseDocumentQueryService = caseDocumentQueryService;
    }

    /**
     * {@code POST  /case-documents} : Create a new caseDocument.
     *
     * @param caseDocumentDTO the caseDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new caseDocumentDTO, or with status {@code 400 (Bad Request)} if the caseDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CaseDocumentDTO> createCaseDocument(@Valid @RequestBody CaseDocumentDTO caseDocumentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CaseDocument : {}", caseDocumentDTO);
        if (caseDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new caseDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        caseDocumentDTO = caseDocumentService.save(caseDocumentDTO);
        return ResponseEntity.created(new URI("/api/case-documents/" + caseDocumentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, caseDocumentDTO.getId().toString()))
            .body(caseDocumentDTO);
    }

    /**
     * {@code PUT  /case-documents/:id} : Updates an existing caseDocument.
     *
     * @param id the id of the caseDocumentDTO to save.
     * @param caseDocumentDTO the caseDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caseDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the caseDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the caseDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CaseDocumentDTO> updateCaseDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CaseDocumentDTO caseDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CaseDocument : {}, {}", id, caseDocumentDTO);
        if (caseDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caseDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caseDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        caseDocumentDTO = caseDocumentService.update(caseDocumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caseDocumentDTO.getId().toString()))
            .body(caseDocumentDTO);
    }

    /**
     * {@code PATCH  /case-documents/:id} : Partial updates given fields of an existing caseDocument, field will ignore if it is null
     *
     * @param id the id of the caseDocumentDTO to save.
     * @param caseDocumentDTO the caseDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caseDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the caseDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the caseDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the caseDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CaseDocumentDTO> partialUpdateCaseDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CaseDocumentDTO caseDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CaseDocument partially : {}, {}", id, caseDocumentDTO);
        if (caseDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caseDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caseDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CaseDocumentDTO> result = caseDocumentService.partialUpdate(caseDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caseDocumentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /case-documents} : get all the caseDocuments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of caseDocuments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CaseDocumentDTO>> getAllCaseDocuments(
        CaseDocumentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CaseDocuments by criteria: {}", criteria);

        Page<CaseDocumentDTO> page = caseDocumentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /case-documents/count} : count all the caseDocuments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCaseDocuments(CaseDocumentCriteria criteria) {
        LOG.debug("REST request to count CaseDocuments by criteria: {}", criteria);
        return ResponseEntity.ok().body(caseDocumentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /case-documents/:id} : get the "id" caseDocument.
     *
     * @param id the id of the caseDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the caseDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CaseDocumentDTO> getCaseDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CaseDocument : {}", id);
        Optional<CaseDocumentDTO> caseDocumentDTO = caseDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(caseDocumentDTO);
    }

    /**
     * {@code DELETE  /case-documents/:id} : delete the "id" caseDocument.
     *
     * @param id the id of the caseDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCaseDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CaseDocument : {}", id);
        caseDocumentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
