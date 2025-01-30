package com.lawyer.web.rest;

import static com.lawyer.domain.CourtCaseAsserts.*;
import static com.lawyer.web.rest.TestUtil.createUpdateProxyForBean;
import static com.lawyer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawyer.IntegrationTest;
import com.lawyer.domain.CaseStatus;
import com.lawyer.domain.Client;
import com.lawyer.domain.Court;
import com.lawyer.domain.CourtCase;
import com.lawyer.domain.CourtCaseType;
import com.lawyer.repository.CourtCaseRepository;
import com.lawyer.service.dto.CourtCaseDTO;
import com.lawyer.service.mapper.CourtCaseMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CourtCaseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourtCaseResourceIT {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CASE_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_CASE_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_COURT_CIRCUIT = "AAAAAAAAAA";
    private static final String UPDATED_COURT_CIRCUIT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REGISTRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REGISTRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ATTORNEY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ATTORNEY_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_ATTORNEY_YEAR = 1;
    private static final Integer UPDATED_ATTORNEY_YEAR = 2;

    private static final String DEFAULT_ATTORNEY_AUTHENTICATION = "AAAAAAAAAA";
    private static final String UPDATED_ATTORNEY_AUTHENTICATION = "BBBBBBBBBB";

    private static final String DEFAULT_OPPONENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OPPONENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OPPONENT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_OPPONENT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_OPPONENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_OPPONENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/court-cases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CourtCaseRepository courtCaseRepository;

    @Autowired
    private CourtCaseMapper courtCaseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourtCaseMockMvc;

    private CourtCase courtCase;

    private CourtCase insertedCourtCase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourtCase createEntity(EntityManager em) {
        CourtCase courtCase = new CourtCase()
            .number(DEFAULT_NUMBER)
            .caseYear(DEFAULT_CASE_YEAR)
            .courtCircuit(DEFAULT_COURT_CIRCUIT)
            .registrationDate(DEFAULT_REGISTRATION_DATE)
            .attorneyNumber(DEFAULT_ATTORNEY_NUMBER)
            .attorneyYear(DEFAULT_ATTORNEY_YEAR)
            .attorneyAuthentication(DEFAULT_ATTORNEY_AUTHENTICATION)
            .opponentName(DEFAULT_OPPONENT_NAME)
            .opponentDescription(DEFAULT_OPPONENT_DESCRIPTION)
            .opponentAddress(DEFAULT_OPPONENT_ADDRESS)
            .subject(DEFAULT_SUBJECT)
            .notes(DEFAULT_NOTES)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Court court;
        if (TestUtil.findAll(em, Court.class).isEmpty()) {
            court = CourtResourceIT.createEntity();
            em.persist(court);
            em.flush();
        } else {
            court = TestUtil.findAll(em, Court.class).get(0);
        }
        courtCase.setCourt(court);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        courtCase.setClient(client);
        // Add required entity
        CourtCaseType courtCaseType;
        if (TestUtil.findAll(em, CourtCaseType.class).isEmpty()) {
            courtCaseType = CourtCaseTypeResourceIT.createEntity();
            em.persist(courtCaseType);
            em.flush();
        } else {
            courtCaseType = TestUtil.findAll(em, CourtCaseType.class).get(0);
        }
        courtCase.setCourtCaseType(courtCaseType);
        // Add required entity
        CaseStatus caseStatus;
        if (TestUtil.findAll(em, CaseStatus.class).isEmpty()) {
            caseStatus = CaseStatusResourceIT.createEntity();
            em.persist(caseStatus);
            em.flush();
        } else {
            caseStatus = TestUtil.findAll(em, CaseStatus.class).get(0);
        }
        courtCase.setCaseStatus(caseStatus);
        return courtCase;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourtCase createUpdatedEntity(EntityManager em) {
        CourtCase updatedCourtCase = new CourtCase()
            .number(UPDATED_NUMBER)
            .caseYear(UPDATED_CASE_YEAR)
            .courtCircuit(UPDATED_COURT_CIRCUIT)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .attorneyNumber(UPDATED_ATTORNEY_NUMBER)
            .attorneyYear(UPDATED_ATTORNEY_YEAR)
            .attorneyAuthentication(UPDATED_ATTORNEY_AUTHENTICATION)
            .opponentName(UPDATED_OPPONENT_NAME)
            .opponentDescription(UPDATED_OPPONENT_DESCRIPTION)
            .opponentAddress(UPDATED_OPPONENT_ADDRESS)
            .subject(UPDATED_SUBJECT)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Court court;
        if (TestUtil.findAll(em, Court.class).isEmpty()) {
            court = CourtResourceIT.createUpdatedEntity();
            em.persist(court);
            em.flush();
        } else {
            court = TestUtil.findAll(em, Court.class).get(0);
        }
        updatedCourtCase.setCourt(court);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        updatedCourtCase.setClient(client);
        // Add required entity
        CourtCaseType courtCaseType;
        if (TestUtil.findAll(em, CourtCaseType.class).isEmpty()) {
            courtCaseType = CourtCaseTypeResourceIT.createUpdatedEntity();
            em.persist(courtCaseType);
            em.flush();
        } else {
            courtCaseType = TestUtil.findAll(em, CourtCaseType.class).get(0);
        }
        updatedCourtCase.setCourtCaseType(courtCaseType);
        // Add required entity
        CaseStatus caseStatus;
        if (TestUtil.findAll(em, CaseStatus.class).isEmpty()) {
            caseStatus = CaseStatusResourceIT.createUpdatedEntity();
            em.persist(caseStatus);
            em.flush();
        } else {
            caseStatus = TestUtil.findAll(em, CaseStatus.class).get(0);
        }
        updatedCourtCase.setCaseStatus(caseStatus);
        return updatedCourtCase;
    }

    @BeforeEach
    public void initTest() {
        courtCase = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCourtCase != null) {
            courtCaseRepository.delete(insertedCourtCase);
            insertedCourtCase = null;
        }
    }

    @Test
    @Transactional
    void createCourtCase() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CourtCase
        CourtCaseDTO courtCaseDTO = courtCaseMapper.toDto(courtCase);
        var returnedCourtCaseDTO = om.readValue(
            restCourtCaseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtCaseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CourtCaseDTO.class
        );

        // Validate the CourtCase in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCourtCase = courtCaseMapper.toEntity(returnedCourtCaseDTO);
        assertCourtCaseUpdatableFieldsEquals(returnedCourtCase, getPersistedCourtCase(returnedCourtCase));

        insertedCourtCase = returnedCourtCase;
    }

    @Test
    @Transactional
    void createCourtCaseWithExistingId() throws Exception {
        // Create the CourtCase with an existing ID
        courtCase.setId(1L);
        CourtCaseDTO courtCaseDTO = courtCaseMapper.toDto(courtCase);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourtCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtCaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourtCase in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        courtCase.setNumber(null);

        // Create the CourtCase, which fails.
        CourtCaseDTO courtCaseDTO = courtCaseMapper.toDto(courtCase);

        restCourtCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtCaseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCaseYearIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        courtCase.setCaseYear(null);

        // Create the CourtCase, which fails.
        CourtCaseDTO courtCaseDTO = courtCaseMapper.toDto(courtCase);

        restCourtCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtCaseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRegistrationDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        courtCase.setRegistrationDate(null);

        // Create the CourtCase, which fails.
        CourtCaseDTO courtCaseDTO = courtCaseMapper.toDto(courtCase);

        restCourtCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtCaseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubjectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        courtCase.setSubject(null);

        // Create the CourtCase, which fails.
        CourtCaseDTO courtCaseDTO = courtCaseMapper.toDto(courtCase);

        restCourtCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtCaseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourtCases() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList
        restCourtCaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courtCase.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].caseYear").value(hasItem(DEFAULT_CASE_YEAR)))
            .andExpect(jsonPath("$.[*].courtCircuit").value(hasItem(DEFAULT_COURT_CIRCUIT)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].attorneyNumber").value(hasItem(DEFAULT_ATTORNEY_NUMBER)))
            .andExpect(jsonPath("$.[*].attorneyYear").value(hasItem(DEFAULT_ATTORNEY_YEAR)))
            .andExpect(jsonPath("$.[*].attorneyAuthentication").value(hasItem(DEFAULT_ATTORNEY_AUTHENTICATION)))
            .andExpect(jsonPath("$.[*].opponentName").value(hasItem(DEFAULT_OPPONENT_NAME)))
            .andExpect(jsonPath("$.[*].opponentDescription").value(hasItem(DEFAULT_OPPONENT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].opponentAddress").value(hasItem(DEFAULT_OPPONENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getCourtCase() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get the courtCase
        restCourtCaseMockMvc
            .perform(get(ENTITY_API_URL_ID, courtCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courtCase.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.caseYear").value(DEFAULT_CASE_YEAR))
            .andExpect(jsonPath("$.courtCircuit").value(DEFAULT_COURT_CIRCUIT))
            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()))
            .andExpect(jsonPath("$.attorneyNumber").value(DEFAULT_ATTORNEY_NUMBER))
            .andExpect(jsonPath("$.attorneyYear").value(DEFAULT_ATTORNEY_YEAR))
            .andExpect(jsonPath("$.attorneyAuthentication").value(DEFAULT_ATTORNEY_AUTHENTICATION))
            .andExpect(jsonPath("$.opponentName").value(DEFAULT_OPPONENT_NAME))
            .andExpect(jsonPath("$.opponentDescription").value(DEFAULT_OPPONENT_DESCRIPTION))
            .andExpect(jsonPath("$.opponentAddress").value(DEFAULT_OPPONENT_ADDRESS))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getNonExistingCourtCase() throws Exception {
        // Get the courtCase
        restCourtCaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCourtCase() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the courtCase
        CourtCase updatedCourtCase = courtCaseRepository.findById(courtCase.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCourtCase are not directly saved in db
        em.detach(updatedCourtCase);
        updatedCourtCase
            .number(UPDATED_NUMBER)
            .caseYear(UPDATED_CASE_YEAR)
            .courtCircuit(UPDATED_COURT_CIRCUIT)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .attorneyNumber(UPDATED_ATTORNEY_NUMBER)
            .attorneyYear(UPDATED_ATTORNEY_YEAR)
            .attorneyAuthentication(UPDATED_ATTORNEY_AUTHENTICATION)
            .opponentName(UPDATED_OPPONENT_NAME)
            .opponentDescription(UPDATED_OPPONENT_DESCRIPTION)
            .opponentAddress(UPDATED_OPPONENT_ADDRESS)
            .subject(UPDATED_SUBJECT)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        CourtCaseDTO courtCaseDTO = courtCaseMapper.toDto(updatedCourtCase);

        restCourtCaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courtCaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(courtCaseDTO))
            )
            .andExpect(status().isOk());

        // Validate the CourtCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCourtCaseToMatchAllProperties(updatedCourtCase);
    }

    @Test
    @Transactional
    void putNonExistingCourtCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courtCase.setId(longCount.incrementAndGet());

        // Create the CourtCase
        CourtCaseDTO courtCaseDTO = courtCaseMapper.toDto(courtCase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourtCaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courtCaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(courtCaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourtCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourtCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courtCase.setId(longCount.incrementAndGet());

        // Create the CourtCase
        CourtCaseDTO courtCaseDTO = courtCaseMapper.toDto(courtCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourtCaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(courtCaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourtCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourtCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courtCase.setId(longCount.incrementAndGet());

        // Create the CourtCase
        CourtCaseDTO courtCaseDTO = courtCaseMapper.toDto(courtCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourtCaseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtCaseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourtCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourtCaseWithPatch() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the courtCase using partial update
        CourtCase partialUpdatedCourtCase = new CourtCase();
        partialUpdatedCourtCase.setId(courtCase.getId());

        partialUpdatedCourtCase
            .number(UPDATED_NUMBER)
            .caseYear(UPDATED_CASE_YEAR)
            .courtCircuit(UPDATED_COURT_CIRCUIT)
            .attorneyAuthentication(UPDATED_ATTORNEY_AUTHENTICATION)
            .opponentDescription(UPDATED_OPPONENT_DESCRIPTION)
            .subject(UPDATED_SUBJECT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCourtCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourtCase.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCourtCase))
            )
            .andExpect(status().isOk());

        // Validate the CourtCase in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCourtCaseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCourtCase, courtCase),
            getPersistedCourtCase(courtCase)
        );
    }

    @Test
    @Transactional
    void fullUpdateCourtCaseWithPatch() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the courtCase using partial update
        CourtCase partialUpdatedCourtCase = new CourtCase();
        partialUpdatedCourtCase.setId(courtCase.getId());

        partialUpdatedCourtCase
            .number(UPDATED_NUMBER)
            .caseYear(UPDATED_CASE_YEAR)
            .courtCircuit(UPDATED_COURT_CIRCUIT)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .attorneyNumber(UPDATED_ATTORNEY_NUMBER)
            .attorneyYear(UPDATED_ATTORNEY_YEAR)
            .attorneyAuthentication(UPDATED_ATTORNEY_AUTHENTICATION)
            .opponentName(UPDATED_OPPONENT_NAME)
            .opponentDescription(UPDATED_OPPONENT_DESCRIPTION)
            .opponentAddress(UPDATED_OPPONENT_ADDRESS)
            .subject(UPDATED_SUBJECT)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCourtCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourtCase.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCourtCase))
            )
            .andExpect(status().isOk());

        // Validate the CourtCase in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCourtCaseUpdatableFieldsEquals(partialUpdatedCourtCase, getPersistedCourtCase(partialUpdatedCourtCase));
    }

    @Test
    @Transactional
    void patchNonExistingCourtCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courtCase.setId(longCount.incrementAndGet());

        // Create the CourtCase
        CourtCaseDTO courtCaseDTO = courtCaseMapper.toDto(courtCase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourtCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courtCaseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(courtCaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourtCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourtCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courtCase.setId(longCount.incrementAndGet());

        // Create the CourtCase
        CourtCaseDTO courtCaseDTO = courtCaseMapper.toDto(courtCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourtCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(courtCaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourtCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourtCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courtCase.setId(longCount.incrementAndGet());

        // Create the CourtCase
        CourtCaseDTO courtCaseDTO = courtCaseMapper.toDto(courtCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourtCaseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(courtCaseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourtCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourtCase() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the courtCase
        restCourtCaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, courtCase.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return courtCaseRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CourtCase getPersistedCourtCase(CourtCase courtCase) {
        return courtCaseRepository.findById(courtCase.getId()).orElseThrow();
    }

    protected void assertPersistedCourtCaseToMatchAllProperties(CourtCase expectedCourtCase) {
        assertCourtCaseAllPropertiesEquals(expectedCourtCase, getPersistedCourtCase(expectedCourtCase));
    }

    protected void assertPersistedCourtCaseToMatchUpdatableProperties(CourtCase expectedCourtCase) {
        assertCourtCaseAllUpdatablePropertiesEquals(expectedCourtCase, getPersistedCourtCase(expectedCourtCase));
    }
}
