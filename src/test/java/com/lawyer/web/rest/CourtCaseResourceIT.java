package com.lawyer.web.rest;

import static com.lawyer.domain.CourtCaseAsserts.*;
import static com.lawyer.web.rest.TestUtil.createUpdateProxyForBean;
import static com.lawyer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawyer.IntegrationTest;
import com.lawyer.domain.CaseStatus;
import com.lawyer.domain.Client;
import com.lawyer.domain.Court;
import com.lawyer.domain.CourtCase;
import com.lawyer.domain.CourtCaseType;
import com.lawyer.domain.Lawyer;
import com.lawyer.repository.CourtCaseRepository;
import com.lawyer.service.CourtCaseService;
import com.lawyer.service.dto.CourtCaseDTO;
import com.lawyer.service.mapper.CourtCaseMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CourtCaseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CourtCaseResourceIT {

    private static final String DEFAULT_CASE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CASE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CASE_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_CASE_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_COURT_CIRCUIT = "AAAAAAAAAA";
    private static final String UPDATED_COURT_CIRCUIT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REGISTRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REGISTRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REGISTRATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ATTORNEY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ATTORNEY_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_ATTORNEY_YEAR = 1;
    private static final Integer UPDATED_ATTORNEY_YEAR = 2;
    private static final Integer SMALLER_ATTORNEY_YEAR = 1 - 1;

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
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/court-cases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CourtCaseRepository courtCaseRepository;

    @Mock
    private CourtCaseRepository courtCaseRepositoryMock;

    @Autowired
    private CourtCaseMapper courtCaseMapper;

    @Mock
    private CourtCaseService courtCaseServiceMock;

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
            .caseNumber(DEFAULT_CASE_NUMBER)
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
            .caseNumber(UPDATED_CASE_NUMBER)
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
    void checkCaseNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        courtCase.setCaseNumber(null);

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
            .andExpect(jsonPath("$.[*].caseNumber").value(hasItem(DEFAULT_CASE_NUMBER)))
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

    @SuppressWarnings({ "unchecked" })
    void getAllCourtCasesWithEagerRelationshipsIsEnabled() throws Exception {
        when(courtCaseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCourtCaseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(courtCaseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCourtCasesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(courtCaseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCourtCaseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(courtCaseRepositoryMock, times(1)).findAll(any(Pageable.class));
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
            .andExpect(jsonPath("$.caseNumber").value(DEFAULT_CASE_NUMBER))
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
    void getCourtCasesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        Long id = courtCase.getId();

        defaultCourtCaseFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCourtCaseFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCourtCaseFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCaseNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where caseNumber equals to
        defaultCourtCaseFiltering("caseNumber.equals=" + DEFAULT_CASE_NUMBER, "caseNumber.equals=" + UPDATED_CASE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCaseNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where caseNumber in
        defaultCourtCaseFiltering(
            "caseNumber.in=" + DEFAULT_CASE_NUMBER + "," + UPDATED_CASE_NUMBER,
            "caseNumber.in=" + UPDATED_CASE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByCaseNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where caseNumber is not null
        defaultCourtCaseFiltering("caseNumber.specified=true", "caseNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCourtCasesByCaseNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where caseNumber contains
        defaultCourtCaseFiltering("caseNumber.contains=" + DEFAULT_CASE_NUMBER, "caseNumber.contains=" + UPDATED_CASE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCaseNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where caseNumber does not contain
        defaultCourtCaseFiltering("caseNumber.doesNotContain=" + UPDATED_CASE_NUMBER, "caseNumber.doesNotContain=" + DEFAULT_CASE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCaseYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where caseYear equals to
        defaultCourtCaseFiltering("caseYear.equals=" + DEFAULT_CASE_YEAR, "caseYear.equals=" + UPDATED_CASE_YEAR);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCaseYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where caseYear in
        defaultCourtCaseFiltering("caseYear.in=" + DEFAULT_CASE_YEAR + "," + UPDATED_CASE_YEAR, "caseYear.in=" + UPDATED_CASE_YEAR);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCaseYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where caseYear is not null
        defaultCourtCaseFiltering("caseYear.specified=true", "caseYear.specified=false");
    }

    @Test
    @Transactional
    void getAllCourtCasesByCaseYearContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where caseYear contains
        defaultCourtCaseFiltering("caseYear.contains=" + DEFAULT_CASE_YEAR, "caseYear.contains=" + UPDATED_CASE_YEAR);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCaseYearNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where caseYear does not contain
        defaultCourtCaseFiltering("caseYear.doesNotContain=" + UPDATED_CASE_YEAR, "caseYear.doesNotContain=" + DEFAULT_CASE_YEAR);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCourtCircuitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where courtCircuit equals to
        defaultCourtCaseFiltering("courtCircuit.equals=" + DEFAULT_COURT_CIRCUIT, "courtCircuit.equals=" + UPDATED_COURT_CIRCUIT);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCourtCircuitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where courtCircuit in
        defaultCourtCaseFiltering(
            "courtCircuit.in=" + DEFAULT_COURT_CIRCUIT + "," + UPDATED_COURT_CIRCUIT,
            "courtCircuit.in=" + UPDATED_COURT_CIRCUIT
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByCourtCircuitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where courtCircuit is not null
        defaultCourtCaseFiltering("courtCircuit.specified=true", "courtCircuit.specified=false");
    }

    @Test
    @Transactional
    void getAllCourtCasesByCourtCircuitContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where courtCircuit contains
        defaultCourtCaseFiltering("courtCircuit.contains=" + DEFAULT_COURT_CIRCUIT, "courtCircuit.contains=" + UPDATED_COURT_CIRCUIT);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCourtCircuitNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where courtCircuit does not contain
        defaultCourtCaseFiltering(
            "courtCircuit.doesNotContain=" + UPDATED_COURT_CIRCUIT,
            "courtCircuit.doesNotContain=" + DEFAULT_COURT_CIRCUIT
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByRegistrationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where registrationDate equals to
        defaultCourtCaseFiltering(
            "registrationDate.equals=" + DEFAULT_REGISTRATION_DATE,
            "registrationDate.equals=" + UPDATED_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByRegistrationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where registrationDate in
        defaultCourtCaseFiltering(
            "registrationDate.in=" + DEFAULT_REGISTRATION_DATE + "," + UPDATED_REGISTRATION_DATE,
            "registrationDate.in=" + UPDATED_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByRegistrationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where registrationDate is not null
        defaultCourtCaseFiltering("registrationDate.specified=true", "registrationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCourtCasesByRegistrationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where registrationDate is greater than or equal to
        defaultCourtCaseFiltering(
            "registrationDate.greaterThanOrEqual=" + DEFAULT_REGISTRATION_DATE,
            "registrationDate.greaterThanOrEqual=" + UPDATED_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByRegistrationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where registrationDate is less than or equal to
        defaultCourtCaseFiltering(
            "registrationDate.lessThanOrEqual=" + DEFAULT_REGISTRATION_DATE,
            "registrationDate.lessThanOrEqual=" + SMALLER_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByRegistrationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where registrationDate is less than
        defaultCourtCaseFiltering(
            "registrationDate.lessThan=" + UPDATED_REGISTRATION_DATE,
            "registrationDate.lessThan=" + DEFAULT_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByRegistrationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where registrationDate is greater than
        defaultCourtCaseFiltering(
            "registrationDate.greaterThan=" + SMALLER_REGISTRATION_DATE,
            "registrationDate.greaterThan=" + DEFAULT_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyNumber equals to
        defaultCourtCaseFiltering("attorneyNumber.equals=" + DEFAULT_ATTORNEY_NUMBER, "attorneyNumber.equals=" + UPDATED_ATTORNEY_NUMBER);
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyNumber in
        defaultCourtCaseFiltering(
            "attorneyNumber.in=" + DEFAULT_ATTORNEY_NUMBER + "," + UPDATED_ATTORNEY_NUMBER,
            "attorneyNumber.in=" + UPDATED_ATTORNEY_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyNumber is not null
        defaultCourtCaseFiltering("attorneyNumber.specified=true", "attorneyNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyNumber contains
        defaultCourtCaseFiltering(
            "attorneyNumber.contains=" + DEFAULT_ATTORNEY_NUMBER,
            "attorneyNumber.contains=" + UPDATED_ATTORNEY_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyNumber does not contain
        defaultCourtCaseFiltering(
            "attorneyNumber.doesNotContain=" + UPDATED_ATTORNEY_NUMBER,
            "attorneyNumber.doesNotContain=" + DEFAULT_ATTORNEY_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyYear equals to
        defaultCourtCaseFiltering("attorneyYear.equals=" + DEFAULT_ATTORNEY_YEAR, "attorneyYear.equals=" + UPDATED_ATTORNEY_YEAR);
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyYear in
        defaultCourtCaseFiltering(
            "attorneyYear.in=" + DEFAULT_ATTORNEY_YEAR + "," + UPDATED_ATTORNEY_YEAR,
            "attorneyYear.in=" + UPDATED_ATTORNEY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyYear is not null
        defaultCourtCaseFiltering("attorneyYear.specified=true", "attorneyYear.specified=false");
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyYear is greater than or equal to
        defaultCourtCaseFiltering(
            "attorneyYear.greaterThanOrEqual=" + DEFAULT_ATTORNEY_YEAR,
            "attorneyYear.greaterThanOrEqual=" + UPDATED_ATTORNEY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyYear is less than or equal to
        defaultCourtCaseFiltering(
            "attorneyYear.lessThanOrEqual=" + DEFAULT_ATTORNEY_YEAR,
            "attorneyYear.lessThanOrEqual=" + SMALLER_ATTORNEY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyYearIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyYear is less than
        defaultCourtCaseFiltering("attorneyYear.lessThan=" + UPDATED_ATTORNEY_YEAR, "attorneyYear.lessThan=" + DEFAULT_ATTORNEY_YEAR);
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyYear is greater than
        defaultCourtCaseFiltering("attorneyYear.greaterThan=" + SMALLER_ATTORNEY_YEAR, "attorneyYear.greaterThan=" + DEFAULT_ATTORNEY_YEAR);
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyAuthenticationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyAuthentication equals to
        defaultCourtCaseFiltering(
            "attorneyAuthentication.equals=" + DEFAULT_ATTORNEY_AUTHENTICATION,
            "attorneyAuthentication.equals=" + UPDATED_ATTORNEY_AUTHENTICATION
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyAuthenticationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyAuthentication in
        defaultCourtCaseFiltering(
            "attorneyAuthentication.in=" + DEFAULT_ATTORNEY_AUTHENTICATION + "," + UPDATED_ATTORNEY_AUTHENTICATION,
            "attorneyAuthentication.in=" + UPDATED_ATTORNEY_AUTHENTICATION
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyAuthenticationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyAuthentication is not null
        defaultCourtCaseFiltering("attorneyAuthentication.specified=true", "attorneyAuthentication.specified=false");
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyAuthenticationContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyAuthentication contains
        defaultCourtCaseFiltering(
            "attorneyAuthentication.contains=" + DEFAULT_ATTORNEY_AUTHENTICATION,
            "attorneyAuthentication.contains=" + UPDATED_ATTORNEY_AUTHENTICATION
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByAttorneyAuthenticationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where attorneyAuthentication does not contain
        defaultCourtCaseFiltering(
            "attorneyAuthentication.doesNotContain=" + UPDATED_ATTORNEY_AUTHENTICATION,
            "attorneyAuthentication.doesNotContain=" + DEFAULT_ATTORNEY_AUTHENTICATION
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentName equals to
        defaultCourtCaseFiltering("opponentName.equals=" + DEFAULT_OPPONENT_NAME, "opponentName.equals=" + UPDATED_OPPONENT_NAME);
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentName in
        defaultCourtCaseFiltering(
            "opponentName.in=" + DEFAULT_OPPONENT_NAME + "," + UPDATED_OPPONENT_NAME,
            "opponentName.in=" + UPDATED_OPPONENT_NAME
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentName is not null
        defaultCourtCaseFiltering("opponentName.specified=true", "opponentName.specified=false");
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentName contains
        defaultCourtCaseFiltering("opponentName.contains=" + DEFAULT_OPPONENT_NAME, "opponentName.contains=" + UPDATED_OPPONENT_NAME);
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentName does not contain
        defaultCourtCaseFiltering(
            "opponentName.doesNotContain=" + UPDATED_OPPONENT_NAME,
            "opponentName.doesNotContain=" + DEFAULT_OPPONENT_NAME
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentDescription equals to
        defaultCourtCaseFiltering(
            "opponentDescription.equals=" + DEFAULT_OPPONENT_DESCRIPTION,
            "opponentDescription.equals=" + UPDATED_OPPONENT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentDescription in
        defaultCourtCaseFiltering(
            "opponentDescription.in=" + DEFAULT_OPPONENT_DESCRIPTION + "," + UPDATED_OPPONENT_DESCRIPTION,
            "opponentDescription.in=" + UPDATED_OPPONENT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentDescription is not null
        defaultCourtCaseFiltering("opponentDescription.specified=true", "opponentDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentDescription contains
        defaultCourtCaseFiltering(
            "opponentDescription.contains=" + DEFAULT_OPPONENT_DESCRIPTION,
            "opponentDescription.contains=" + UPDATED_OPPONENT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentDescription does not contain
        defaultCourtCaseFiltering(
            "opponentDescription.doesNotContain=" + UPDATED_OPPONENT_DESCRIPTION,
            "opponentDescription.doesNotContain=" + DEFAULT_OPPONENT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentAddress equals to
        defaultCourtCaseFiltering(
            "opponentAddress.equals=" + DEFAULT_OPPONENT_ADDRESS,
            "opponentAddress.equals=" + UPDATED_OPPONENT_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentAddress in
        defaultCourtCaseFiltering(
            "opponentAddress.in=" + DEFAULT_OPPONENT_ADDRESS + "," + UPDATED_OPPONENT_ADDRESS,
            "opponentAddress.in=" + UPDATED_OPPONENT_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentAddress is not null
        defaultCourtCaseFiltering("opponentAddress.specified=true", "opponentAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentAddress contains
        defaultCourtCaseFiltering(
            "opponentAddress.contains=" + DEFAULT_OPPONENT_ADDRESS,
            "opponentAddress.contains=" + UPDATED_OPPONENT_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where opponentAddress does not contain
        defaultCourtCaseFiltering(
            "opponentAddress.doesNotContain=" + UPDATED_OPPONENT_ADDRESS,
            "opponentAddress.doesNotContain=" + DEFAULT_OPPONENT_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where subject equals to
        defaultCourtCaseFiltering("subject.equals=" + DEFAULT_SUBJECT, "subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllCourtCasesBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where subject in
        defaultCourtCaseFiltering("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT, "subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllCourtCasesBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where subject is not null
        defaultCourtCaseFiltering("subject.specified=true", "subject.specified=false");
    }

    @Test
    @Transactional
    void getAllCourtCasesBySubjectContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where subject contains
        defaultCourtCaseFiltering("subject.contains=" + DEFAULT_SUBJECT, "subject.contains=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllCourtCasesBySubjectNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where subject does not contain
        defaultCourtCaseFiltering("subject.doesNotContain=" + UPDATED_SUBJECT, "subject.doesNotContain=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllCourtCasesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where notes equals to
        defaultCourtCaseFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCourtCasesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where notes in
        defaultCourtCaseFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCourtCasesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where notes is not null
        defaultCourtCaseFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllCourtCasesByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where notes contains
        defaultCourtCaseFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCourtCasesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where notes does not contain
        defaultCourtCaseFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where createdAt equals to
        defaultCourtCaseFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where createdAt in
        defaultCourtCaseFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where createdAt is not null
        defaultCourtCaseFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCourtCasesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where createdAt is greater than or equal to
        defaultCourtCaseFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where createdAt is less than or equal to
        defaultCourtCaseFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where createdAt is less than
        defaultCourtCaseFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where createdAt is greater than
        defaultCourtCaseFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCourtCasesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where updatedAt equals to
        defaultCourtCaseFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCourtCasesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where updatedAt in
        defaultCourtCaseFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCourtCasesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where updatedAt is not null
        defaultCourtCaseFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCourtCasesByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where updatedAt is greater than or equal to
        defaultCourtCaseFiltering(
            "updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT,
            "updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCourtCasesByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where updatedAt is less than or equal to
        defaultCourtCaseFiltering("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT, "updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCourtCasesByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where updatedAt is less than
        defaultCourtCaseFiltering("updatedAt.lessThan=" + UPDATED_UPDATED_AT, "updatedAt.lessThan=" + DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCourtCasesByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCourtCase = courtCaseRepository.saveAndFlush(courtCase);

        // Get all the courtCaseList where updatedAt is greater than
        defaultCourtCaseFiltering("updatedAt.greaterThan=" + SMALLER_UPDATED_AT, "updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCourtCasesByCourtIsEqualToSomething() throws Exception {
        Court court;
        if (TestUtil.findAll(em, Court.class).isEmpty()) {
            courtCaseRepository.saveAndFlush(courtCase);
            court = CourtResourceIT.createEntity();
        } else {
            court = TestUtil.findAll(em, Court.class).get(0);
        }
        em.persist(court);
        em.flush();
        courtCase.setCourt(court);
        courtCaseRepository.saveAndFlush(courtCase);
        Long courtId = court.getId();
        // Get all the courtCaseList where court equals to courtId
        defaultCourtCaseShouldBeFound("courtId.equals=" + courtId);

        // Get all the courtCaseList where court equals to (courtId + 1)
        defaultCourtCaseShouldNotBeFound("courtId.equals=" + (courtId + 1));
    }

    @Test
    @Transactional
    void getAllCourtCasesByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            courtCaseRepository.saveAndFlush(courtCase);
            client = ClientResourceIT.createEntity();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        courtCase.setClient(client);
        courtCaseRepository.saveAndFlush(courtCase);
        Long clientId = client.getId();
        // Get all the courtCaseList where client equals to clientId
        defaultCourtCaseShouldBeFound("clientId.equals=" + clientId);

        // Get all the courtCaseList where client equals to (clientId + 1)
        defaultCourtCaseShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    @Test
    @Transactional
    void getAllCourtCasesByCourtCaseTypeIsEqualToSomething() throws Exception {
        CourtCaseType courtCaseType;
        if (TestUtil.findAll(em, CourtCaseType.class).isEmpty()) {
            courtCaseRepository.saveAndFlush(courtCase);
            courtCaseType = CourtCaseTypeResourceIT.createEntity();
        } else {
            courtCaseType = TestUtil.findAll(em, CourtCaseType.class).get(0);
        }
        em.persist(courtCaseType);
        em.flush();
        courtCase.setCourtCaseType(courtCaseType);
        courtCaseRepository.saveAndFlush(courtCase);
        Long courtCaseTypeId = courtCaseType.getId();
        // Get all the courtCaseList where courtCaseType equals to courtCaseTypeId
        defaultCourtCaseShouldBeFound("courtCaseTypeId.equals=" + courtCaseTypeId);

        // Get all the courtCaseList where courtCaseType equals to (courtCaseTypeId + 1)
        defaultCourtCaseShouldNotBeFound("courtCaseTypeId.equals=" + (courtCaseTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCourtCasesByCaseStatusIsEqualToSomething() throws Exception {
        CaseStatus caseStatus;
        if (TestUtil.findAll(em, CaseStatus.class).isEmpty()) {
            courtCaseRepository.saveAndFlush(courtCase);
            caseStatus = CaseStatusResourceIT.createEntity();
        } else {
            caseStatus = TestUtil.findAll(em, CaseStatus.class).get(0);
        }
        em.persist(caseStatus);
        em.flush();
        courtCase.setCaseStatus(caseStatus);
        courtCaseRepository.saveAndFlush(courtCase);
        Long caseStatusId = caseStatus.getId();
        // Get all the courtCaseList where caseStatus equals to caseStatusId
        defaultCourtCaseShouldBeFound("caseStatusId.equals=" + caseStatusId);

        // Get all the courtCaseList where caseStatus equals to (caseStatusId + 1)
        defaultCourtCaseShouldNotBeFound("caseStatusId.equals=" + (caseStatusId + 1));
    }

    @Test
    @Transactional
    void getAllCourtCasesByOpponentLawyerIdIsEqualToSomething() throws Exception {
        Lawyer opponentLawyerId;
        if (TestUtil.findAll(em, Lawyer.class).isEmpty()) {
            courtCaseRepository.saveAndFlush(courtCase);
            opponentLawyerId = LawyerResourceIT.createEntity();
        } else {
            opponentLawyerId = TestUtil.findAll(em, Lawyer.class).get(0);
        }
        em.persist(opponentLawyerId);
        em.flush();
        courtCase.setOpponentLawyerId(opponentLawyerId);
        courtCaseRepository.saveAndFlush(courtCase);
        Long opponentLawyerIdId = opponentLawyerId.getId();
        // Get all the courtCaseList where opponentLawyerId equals to opponentLawyerIdId
        defaultCourtCaseShouldBeFound("opponentLawyerIdId.equals=" + opponentLawyerIdId);

        // Get all the courtCaseList where opponentLawyerId equals to (opponentLawyerIdId + 1)
        defaultCourtCaseShouldNotBeFound("opponentLawyerIdId.equals=" + (opponentLawyerIdId + 1));
    }

    private void defaultCourtCaseFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCourtCaseShouldBeFound(shouldBeFound);
        defaultCourtCaseShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCourtCaseShouldBeFound(String filter) throws Exception {
        restCourtCaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courtCase.getId().intValue())))
            .andExpect(jsonPath("$.[*].caseNumber").value(hasItem(DEFAULT_CASE_NUMBER)))
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

        // Check, that the count call also returns 1
        restCourtCaseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCourtCaseShouldNotBeFound(String filter) throws Exception {
        restCourtCaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCourtCaseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .caseNumber(UPDATED_CASE_NUMBER)
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
            .caseNumber(UPDATED_CASE_NUMBER)
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
            .caseNumber(UPDATED_CASE_NUMBER)
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
