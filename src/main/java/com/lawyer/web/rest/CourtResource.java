package com.lawyer.web.rest;

import com.lawyer.repository.CourtRepository;
import com.lawyer.service.CourtService;
import com.lawyer.service.dto.CourtDTO;
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
 * REST controller for managing {@link com.lawyer.domain.Court}.
 */
@RestController
@RequestMapping("/api/courts")
public class CourtResource {

    private static final Logger LOG = LoggerFactory.getLogger(CourtResource.class);

    private static final String ENTITY_NAME = "court";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourtService courtService;

    private final CourtRepository courtRepository;

    public CourtResource(CourtService courtService, CourtRepository courtRepository) {
        this.courtService = courtService;
        this.courtRepository = courtRepository;
    }

    /**
     * {@code POST  /courts} : Create a new court.
     *
     * @param courtDTO the courtDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courtDTO, or with status {@code 400 (Bad Request)} if the court has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CourtDTO> createCourt(@Valid @RequestBody CourtDTO courtDTO) throws URISyntaxException {
        LOG.debug("REST request to save Court : {}", courtDTO);
        if (courtDTO.getId() != null) {
            throw new BadRequestAlertException("A new court cannot already have an ID", ENTITY_NAME, "idexists");
        }
        courtDTO = courtService.save(courtDTO);
        return ResponseEntity.created(new URI("/api/courts/" + courtDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, courtDTO.getId().toString()))
            .body(courtDTO);
    }

    /**
     * {@code PUT  /courts/:id} : Updates an existing court.
     *
     * @param id the id of the courtDTO to save.
     * @param courtDTO the courtDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courtDTO,
     * or with status {@code 400 (Bad Request)} if the courtDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courtDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CourtDTO> updateCourt(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CourtDTO courtDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Court : {}, {}", id, courtDTO);
        if (courtDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courtDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        courtDTO = courtService.update(courtDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courtDTO.getId().toString()))
            .body(courtDTO);
    }

    /**
     * {@code PATCH  /courts/:id} : Partial updates given fields of an existing court, field will ignore if it is null
     *
     * @param id the id of the courtDTO to save.
     * @param courtDTO the courtDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courtDTO,
     * or with status {@code 400 (Bad Request)} if the courtDTO is not valid,
     * or with status {@code 404 (Not Found)} if the courtDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the courtDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CourtDTO> partialUpdateCourt(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CourtDTO courtDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Court partially : {}, {}", id, courtDTO);
        if (courtDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courtDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourtDTO> result = courtService.partialUpdate(courtDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courtDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /courts} : get all the courts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courts in body.
     */
    @GetMapping("")
    public List<CourtDTO> getAllCourts() {
        LOG.debug("REST request to get all Courts");
        return courtService.findAll();
    }

    /**
     * {@code GET  /courts/:id} : get the "id" court.
     *
     * @param id the id of the courtDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courtDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourtDTO> getCourt(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Court : {}", id);
        Optional<CourtDTO> courtDTO = courtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courtDTO);
    }

    /**
     * {@code DELETE  /courts/:id} : delete the "id" court.
     *
     * @param id the id of the courtDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Court : {}", id);
        courtService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
