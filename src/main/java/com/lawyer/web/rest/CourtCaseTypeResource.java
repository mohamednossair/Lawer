package com.lawyer.web.rest;

import com.lawyer.repository.CourtCaseTypeRepository;
import com.lawyer.service.CourtCaseTypeService;
import com.lawyer.service.dto.CourtCaseTypeDTO;
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
 * REST controller for managing {@link com.lawyer.domain.CourtCaseType}.
 */
@RestController
@RequestMapping("/api/court-case-types")
public class CourtCaseTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CourtCaseTypeResource.class);

    private static final String ENTITY_NAME = "courtCaseType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourtCaseTypeService courtCaseTypeService;

    private final CourtCaseTypeRepository courtCaseTypeRepository;

    public CourtCaseTypeResource(CourtCaseTypeService courtCaseTypeService, CourtCaseTypeRepository courtCaseTypeRepository) {
        this.courtCaseTypeService = courtCaseTypeService;
        this.courtCaseTypeRepository = courtCaseTypeRepository;
    }

    /**
     * {@code POST  /court-case-types} : Create a new courtCaseType.
     *
     * @param courtCaseTypeDTO the courtCaseTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courtCaseTypeDTO, or with status {@code 400 (Bad Request)} if the courtCaseType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CourtCaseTypeDTO> createCourtCaseType(@Valid @RequestBody CourtCaseTypeDTO courtCaseTypeDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CourtCaseType : {}", courtCaseTypeDTO);
        if (courtCaseTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new courtCaseType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        courtCaseTypeDTO = courtCaseTypeService.save(courtCaseTypeDTO);
        return ResponseEntity.created(new URI("/api/court-case-types/" + courtCaseTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, courtCaseTypeDTO.getId().toString()))
            .body(courtCaseTypeDTO);
    }

    /**
     * {@code PUT  /court-case-types/:id} : Updates an existing courtCaseType.
     *
     * @param id the id of the courtCaseTypeDTO to save.
     * @param courtCaseTypeDTO the courtCaseTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courtCaseTypeDTO,
     * or with status {@code 400 (Bad Request)} if the courtCaseTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courtCaseTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CourtCaseTypeDTO> updateCourtCaseType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CourtCaseTypeDTO courtCaseTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CourtCaseType : {}, {}", id, courtCaseTypeDTO);
        if (courtCaseTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courtCaseTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courtCaseTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        courtCaseTypeDTO = courtCaseTypeService.update(courtCaseTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courtCaseTypeDTO.getId().toString()))
            .body(courtCaseTypeDTO);
    }

    /**
     * {@code PATCH  /court-case-types/:id} : Partial updates given fields of an existing courtCaseType, field will ignore if it is null
     *
     * @param id the id of the courtCaseTypeDTO to save.
     * @param courtCaseTypeDTO the courtCaseTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courtCaseTypeDTO,
     * or with status {@code 400 (Bad Request)} if the courtCaseTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the courtCaseTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the courtCaseTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CourtCaseTypeDTO> partialUpdateCourtCaseType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CourtCaseTypeDTO courtCaseTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CourtCaseType partially : {}, {}", id, courtCaseTypeDTO);
        if (courtCaseTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courtCaseTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courtCaseTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourtCaseTypeDTO> result = courtCaseTypeService.partialUpdate(courtCaseTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courtCaseTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /court-case-types} : get all the courtCaseTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courtCaseTypes in body.
     */
    @GetMapping("")
    public List<CourtCaseTypeDTO> getAllCourtCaseTypes() {
        LOG.debug("REST request to get all CourtCaseTypes");
        return courtCaseTypeService.findAll();
    }

    /**
     * {@code GET  /court-case-types/:id} : get the "id" courtCaseType.
     *
     * @param id the id of the courtCaseTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courtCaseTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourtCaseTypeDTO> getCourtCaseType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CourtCaseType : {}", id);
        Optional<CourtCaseTypeDTO> courtCaseTypeDTO = courtCaseTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courtCaseTypeDTO);
    }

    /**
     * {@code DELETE  /court-case-types/:id} : delete the "id" courtCaseType.
     *
     * @param id the id of the courtCaseTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourtCaseType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CourtCaseType : {}", id);
        courtCaseTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
