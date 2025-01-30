package com.lawyer.web.rest;

import com.lawyer.repository.LawyerRepository;
import com.lawyer.service.LawyerService;
import com.lawyer.service.dto.LawyerDTO;
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
 * REST controller for managing {@link com.lawyer.domain.Lawyer}.
 */
@RestController
@RequestMapping("/api/lawyers")
public class LawyerResource {

    private static final Logger LOG = LoggerFactory.getLogger(LawyerResource.class);

    private static final String ENTITY_NAME = "lawyer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LawyerService lawyerService;

    private final LawyerRepository lawyerRepository;

    public LawyerResource(LawyerService lawyerService, LawyerRepository lawyerRepository) {
        this.lawyerService = lawyerService;
        this.lawyerRepository = lawyerRepository;
    }

    /**
     * {@code POST  /lawyers} : Create a new lawyer.
     *
     * @param lawyerDTO the lawyerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lawyerDTO, or with status {@code 400 (Bad Request)} if the lawyer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LawyerDTO> createLawyer(@Valid @RequestBody LawyerDTO lawyerDTO) throws URISyntaxException {
        LOG.debug("REST request to save Lawyer : {}", lawyerDTO);
        if (lawyerDTO.getId() != null) {
            throw new BadRequestAlertException("A new lawyer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        lawyerDTO = lawyerService.save(lawyerDTO);
        return ResponseEntity.created(new URI("/api/lawyers/" + lawyerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, lawyerDTO.getId().toString()))
            .body(lawyerDTO);
    }

    /**
     * {@code PUT  /lawyers/:id} : Updates an existing lawyer.
     *
     * @param id the id of the lawyerDTO to save.
     * @param lawyerDTO the lawyerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lawyerDTO,
     * or with status {@code 400 (Bad Request)} if the lawyerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lawyerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LawyerDTO> updateLawyer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LawyerDTO lawyerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Lawyer : {}, {}", id, lawyerDTO);
        if (lawyerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lawyerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lawyerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        lawyerDTO = lawyerService.update(lawyerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lawyerDTO.getId().toString()))
            .body(lawyerDTO);
    }

    /**
     * {@code PATCH  /lawyers/:id} : Partial updates given fields of an existing lawyer, field will ignore if it is null
     *
     * @param id the id of the lawyerDTO to save.
     * @param lawyerDTO the lawyerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lawyerDTO,
     * or with status {@code 400 (Bad Request)} if the lawyerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lawyerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lawyerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LawyerDTO> partialUpdateLawyer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LawyerDTO lawyerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Lawyer partially : {}, {}", id, lawyerDTO);
        if (lawyerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lawyerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lawyerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LawyerDTO> result = lawyerService.partialUpdate(lawyerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lawyerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lawyers} : get all the lawyers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lawyers in body.
     */
    @GetMapping("")
    public List<LawyerDTO> getAllLawyers() {
        LOG.debug("REST request to get all Lawyers");
        return lawyerService.findAll();
    }

    /**
     * {@code GET  /lawyers/:id} : get the "id" lawyer.
     *
     * @param id the id of the lawyerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lawyerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LawyerDTO> getLawyer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Lawyer : {}", id);
        Optional<LawyerDTO> lawyerDTO = lawyerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lawyerDTO);
    }

    /**
     * {@code DELETE  /lawyers/:id} : delete the "id" lawyer.
     *
     * @param id the id of the lawyerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLawyer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Lawyer : {}", id);
        lawyerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
