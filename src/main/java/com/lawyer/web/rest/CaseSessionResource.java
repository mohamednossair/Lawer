package com.lawyer.web.rest;

import com.lawyer.repository.CaseSessionRepository;
import com.lawyer.service.CaseSessionService;
import com.lawyer.service.dto.CaseSessionDTO;
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
 * REST controller for managing {@link com.lawyer.domain.CaseSession}.
 */
@RestController
@RequestMapping("/api/case-sessions")
public class CaseSessionResource {

    private static final Logger LOG = LoggerFactory.getLogger(CaseSessionResource.class);

    private static final String ENTITY_NAME = "caseSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CaseSessionService caseSessionService;

    private final CaseSessionRepository caseSessionRepository;

    public CaseSessionResource(CaseSessionService caseSessionService, CaseSessionRepository caseSessionRepository) {
        this.caseSessionService = caseSessionService;
        this.caseSessionRepository = caseSessionRepository;
    }

    /**
     * {@code POST  /case-sessions} : Create a new caseSession.
     *
     * @param caseSessionDTO the caseSessionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new caseSessionDTO, or with status {@code 400 (Bad Request)} if the caseSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CaseSessionDTO> createCaseSession(@Valid @RequestBody CaseSessionDTO caseSessionDTO) throws URISyntaxException {
        LOG.debug("REST request to save CaseSession : {}", caseSessionDTO);
        if (caseSessionDTO.getId() != null) {
            throw new BadRequestAlertException("A new caseSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        caseSessionDTO = caseSessionService.save(caseSessionDTO);
        return ResponseEntity.created(new URI("/api/case-sessions/" + caseSessionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, caseSessionDTO.getId().toString()))
            .body(caseSessionDTO);
    }

    /**
     * {@code PUT  /case-sessions/:id} : Updates an existing caseSession.
     *
     * @param id the id of the caseSessionDTO to save.
     * @param caseSessionDTO the caseSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caseSessionDTO,
     * or with status {@code 400 (Bad Request)} if the caseSessionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the caseSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CaseSessionDTO> updateCaseSession(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CaseSessionDTO caseSessionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CaseSession : {}, {}", id, caseSessionDTO);
        if (caseSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caseSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caseSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        caseSessionDTO = caseSessionService.update(caseSessionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caseSessionDTO.getId().toString()))
            .body(caseSessionDTO);
    }

    /**
     * {@code PATCH  /case-sessions/:id} : Partial updates given fields of an existing caseSession, field will ignore if it is null
     *
     * @param id the id of the caseSessionDTO to save.
     * @param caseSessionDTO the caseSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caseSessionDTO,
     * or with status {@code 400 (Bad Request)} if the caseSessionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the caseSessionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the caseSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CaseSessionDTO> partialUpdateCaseSession(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CaseSessionDTO caseSessionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CaseSession partially : {}, {}", id, caseSessionDTO);
        if (caseSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caseSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caseSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CaseSessionDTO> result = caseSessionService.partialUpdate(caseSessionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caseSessionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /case-sessions} : get all the caseSessions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of caseSessions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CaseSessionDTO>> getAllCaseSessions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of CaseSessions");
        Page<CaseSessionDTO> page = caseSessionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /case-sessions/:id} : get the "id" caseSession.
     *
     * @param id the id of the caseSessionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the caseSessionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CaseSessionDTO> getCaseSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CaseSession : {}", id);
        Optional<CaseSessionDTO> caseSessionDTO = caseSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(caseSessionDTO);
    }

    /**
     * {@code DELETE  /case-sessions/:id} : delete the "id" caseSession.
     *
     * @param id the id of the caseSessionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCaseSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CaseSession : {}", id);
        caseSessionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
