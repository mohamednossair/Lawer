package com.lawyer.web.rest;

import static com.lawyer.domain.CaseSessionAsserts.*;
import static com.lawyer.web.rest.TestUtil.createUpdateProxyForBean;
import static com.lawyer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawyer.IntegrationTest;
import com.lawyer.domain.CaseSession;
import com.lawyer.domain.CourtCase;
import com.lawyer.repository.CaseSessionRepository;
import com.lawyer.service.CaseSessionService;
import com.lawyer.service.dto.CaseSessionDTO;
import com.lawyer.service.mapper.CaseSessionMapper;
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
 * Integration tests for the {@link CaseSessionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CaseSessionResourceIT {

    private static final LocalDate DEFAULT_SESSION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SESSION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SESSION_DATE = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_SESSION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SESSION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_SESSION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/case-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CaseSessionRepository caseSessionRepository;

    @Mock
    private CaseSessionRepository caseSessionRepositoryMock;

    @Autowired
    private CaseSessionMapper caseSessionMapper;

    @Mock
    private CaseSessionService caseSessionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCaseSessionMockMvc;

    private CaseSession caseSession;

    private CaseSession insertedCaseSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CaseSession createEntity() {
        return new CaseSession()
            .sessionDate(DEFAULT_SESSION_DATE)
            .sessionTime(DEFAULT_SESSION_TIME)
            .description(DEFAULT_DESCRIPTION)
            .notes(DEFAULT_NOTES)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CaseSession createUpdatedEntity() {
        return new CaseSession()
            .sessionDate(UPDATED_SESSION_DATE)
            .sessionTime(UPDATED_SESSION_TIME)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        caseSession = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCaseSession != null) {
            caseSessionRepository.delete(insertedCaseSession);
            insertedCaseSession = null;
        }
    }

    @Test
    @Transactional
    void createCaseSession() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CaseSession
        CaseSessionDTO caseSessionDTO = caseSessionMapper.toDto(caseSession);
        var returnedCaseSessionDTO = om.readValue(
            restCaseSessionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseSessionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CaseSessionDTO.class
        );

        // Validate the CaseSession in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCaseSession = caseSessionMapper.toEntity(returnedCaseSessionDTO);
        assertCaseSessionUpdatableFieldsEquals(returnedCaseSession, getPersistedCaseSession(returnedCaseSession));

        insertedCaseSession = returnedCaseSession;
    }

    @Test
    @Transactional
    void createCaseSessionWithExistingId() throws Exception {
        // Create the CaseSession with an existing ID
        caseSession.setId(1L);
        CaseSessionDTO caseSessionDTO = caseSessionMapper.toDto(caseSession);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaseSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseSessionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CaseSession in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSessionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        caseSession.setSessionDate(null);

        // Create the CaseSession, which fails.
        CaseSessionDTO caseSessionDTO = caseSessionMapper.toDto(caseSession);

        restCaseSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCaseSessions() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList
        restCaseSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caseSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionDate").value(hasItem(DEFAULT_SESSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].sessionTime").value(hasItem(sameInstant(DEFAULT_SESSION_TIME))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCaseSessionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(caseSessionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCaseSessionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(caseSessionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCaseSessionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(caseSessionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCaseSessionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(caseSessionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCaseSession() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get the caseSession
        restCaseSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, caseSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caseSession.getId().intValue()))
            .andExpect(jsonPath("$.sessionDate").value(DEFAULT_SESSION_DATE.toString()))
            .andExpect(jsonPath("$.sessionTime").value(sameInstant(DEFAULT_SESSION_TIME)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getCaseSessionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        Long id = caseSession.getId();

        defaultCaseSessionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCaseSessionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCaseSessionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCaseSessionsBySessionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where sessionDate equals to
        defaultCaseSessionFiltering("sessionDate.equals=" + DEFAULT_SESSION_DATE, "sessionDate.equals=" + UPDATED_SESSION_DATE);
    }

    @Test
    @Transactional
    void getAllCaseSessionsBySessionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where sessionDate in
        defaultCaseSessionFiltering(
            "sessionDate.in=" + DEFAULT_SESSION_DATE + "," + UPDATED_SESSION_DATE,
            "sessionDate.in=" + UPDATED_SESSION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCaseSessionsBySessionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where sessionDate is not null
        defaultCaseSessionFiltering("sessionDate.specified=true", "sessionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseSessionsBySessionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where sessionDate is greater than or equal to
        defaultCaseSessionFiltering(
            "sessionDate.greaterThanOrEqual=" + DEFAULT_SESSION_DATE,
            "sessionDate.greaterThanOrEqual=" + UPDATED_SESSION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCaseSessionsBySessionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where sessionDate is less than or equal to
        defaultCaseSessionFiltering(
            "sessionDate.lessThanOrEqual=" + DEFAULT_SESSION_DATE,
            "sessionDate.lessThanOrEqual=" + SMALLER_SESSION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCaseSessionsBySessionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where sessionDate is less than
        defaultCaseSessionFiltering("sessionDate.lessThan=" + UPDATED_SESSION_DATE, "sessionDate.lessThan=" + DEFAULT_SESSION_DATE);
    }

    @Test
    @Transactional
    void getAllCaseSessionsBySessionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where sessionDate is greater than
        defaultCaseSessionFiltering("sessionDate.greaterThan=" + SMALLER_SESSION_DATE, "sessionDate.greaterThan=" + DEFAULT_SESSION_DATE);
    }

    @Test
    @Transactional
    void getAllCaseSessionsBySessionTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where sessionTime equals to
        defaultCaseSessionFiltering("sessionTime.equals=" + DEFAULT_SESSION_TIME, "sessionTime.equals=" + UPDATED_SESSION_TIME);
    }

    @Test
    @Transactional
    void getAllCaseSessionsBySessionTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where sessionTime in
        defaultCaseSessionFiltering(
            "sessionTime.in=" + DEFAULT_SESSION_TIME + "," + UPDATED_SESSION_TIME,
            "sessionTime.in=" + UPDATED_SESSION_TIME
        );
    }

    @Test
    @Transactional
    void getAllCaseSessionsBySessionTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where sessionTime is not null
        defaultCaseSessionFiltering("sessionTime.specified=true", "sessionTime.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseSessionsBySessionTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where sessionTime is greater than or equal to
        defaultCaseSessionFiltering(
            "sessionTime.greaterThanOrEqual=" + DEFAULT_SESSION_TIME,
            "sessionTime.greaterThanOrEqual=" + UPDATED_SESSION_TIME
        );
    }

    @Test
    @Transactional
    void getAllCaseSessionsBySessionTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where sessionTime is less than or equal to
        defaultCaseSessionFiltering(
            "sessionTime.lessThanOrEqual=" + DEFAULT_SESSION_TIME,
            "sessionTime.lessThanOrEqual=" + SMALLER_SESSION_TIME
        );
    }

    @Test
    @Transactional
    void getAllCaseSessionsBySessionTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where sessionTime is less than
        defaultCaseSessionFiltering("sessionTime.lessThan=" + UPDATED_SESSION_TIME, "sessionTime.lessThan=" + DEFAULT_SESSION_TIME);
    }

    @Test
    @Transactional
    void getAllCaseSessionsBySessionTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where sessionTime is greater than
        defaultCaseSessionFiltering("sessionTime.greaterThan=" + SMALLER_SESSION_TIME, "sessionTime.greaterThan=" + DEFAULT_SESSION_TIME);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where description equals to
        defaultCaseSessionFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where description in
        defaultCaseSessionFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCaseSessionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where description is not null
        defaultCaseSessionFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseSessionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where description contains
        defaultCaseSessionFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where description does not contain
        defaultCaseSessionFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCaseSessionsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where notes equals to
        defaultCaseSessionFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where notes in
        defaultCaseSessionFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where notes is not null
        defaultCaseSessionFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseSessionsByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where notes contains
        defaultCaseSessionFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where notes does not contain
        defaultCaseSessionFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where createdAt equals to
        defaultCaseSessionFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where createdAt in
        defaultCaseSessionFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where createdAt is not null
        defaultCaseSessionFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseSessionsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where createdAt is greater than or equal to
        defaultCaseSessionFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCaseSessionsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where createdAt is less than or equal to
        defaultCaseSessionFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where createdAt is less than
        defaultCaseSessionFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where createdAt is greater than
        defaultCaseSessionFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where updatedAt equals to
        defaultCaseSessionFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where updatedAt in
        defaultCaseSessionFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where updatedAt is not null
        defaultCaseSessionFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseSessionsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where updatedAt is greater than or equal to
        defaultCaseSessionFiltering(
            "updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT,
            "updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCaseSessionsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where updatedAt is less than or equal to
        defaultCaseSessionFiltering("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT, "updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where updatedAt is less than
        defaultCaseSessionFiltering("updatedAt.lessThan=" + UPDATED_UPDATED_AT, "updatedAt.lessThan=" + DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        // Get all the caseSessionList where updatedAt is greater than
        defaultCaseSessionFiltering("updatedAt.greaterThan=" + SMALLER_UPDATED_AT, "updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseSessionsByCourtCaseIsEqualToSomething() throws Exception {
        CourtCase courtCase;
        if (TestUtil.findAll(em, CourtCase.class).isEmpty()) {
            caseSessionRepository.saveAndFlush(caseSession);
            courtCase = CourtCaseResourceIT.createEntity(em);
        } else {
            courtCase = TestUtil.findAll(em, CourtCase.class).get(0);
        }
        em.persist(courtCase);
        em.flush();
        caseSession.setCourtCase(courtCase);
        caseSessionRepository.saveAndFlush(caseSession);
        Long courtCaseId = courtCase.getId();
        // Get all the caseSessionList where courtCase equals to courtCaseId
        defaultCaseSessionShouldBeFound("courtCaseId.equals=" + courtCaseId);

        // Get all the caseSessionList where courtCase equals to (courtCaseId + 1)
        defaultCaseSessionShouldNotBeFound("courtCaseId.equals=" + (courtCaseId + 1));
    }

    private void defaultCaseSessionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCaseSessionShouldBeFound(shouldBeFound);
        defaultCaseSessionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCaseSessionShouldBeFound(String filter) throws Exception {
        restCaseSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caseSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionDate").value(hasItem(DEFAULT_SESSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].sessionTime").value(hasItem(sameInstant(DEFAULT_SESSION_TIME))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restCaseSessionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCaseSessionShouldNotBeFound(String filter) throws Exception {
        restCaseSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCaseSessionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCaseSession() throws Exception {
        // Get the caseSession
        restCaseSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCaseSession() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caseSession
        CaseSession updatedCaseSession = caseSessionRepository.findById(caseSession.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCaseSession are not directly saved in db
        em.detach(updatedCaseSession);
        updatedCaseSession
            .sessionDate(UPDATED_SESSION_DATE)
            .sessionTime(UPDATED_SESSION_TIME)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        CaseSessionDTO caseSessionDTO = caseSessionMapper.toDto(updatedCaseSession);

        restCaseSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caseSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(caseSessionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CaseSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCaseSessionToMatchAllProperties(updatedCaseSession);
    }

    @Test
    @Transactional
    void putNonExistingCaseSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseSession.setId(longCount.incrementAndGet());

        // Create the CaseSession
        CaseSessionDTO caseSessionDTO = caseSessionMapper.toDto(caseSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaseSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caseSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(caseSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaseSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseSession.setId(longCount.incrementAndGet());

        // Create the CaseSession
        CaseSessionDTO caseSessionDTO = caseSessionMapper.toDto(caseSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(caseSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaseSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseSession.setId(longCount.incrementAndGet());

        // Create the CaseSession
        CaseSessionDTO caseSessionDTO = caseSessionMapper.toDto(caseSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CaseSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCaseSessionWithPatch() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caseSession using partial update
        CaseSession partialUpdatedCaseSession = new CaseSession();
        partialUpdatedCaseSession.setId(caseSession.getId());

        partialUpdatedCaseSession
            .sessionDate(UPDATED_SESSION_DATE)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCaseSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaseSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCaseSession))
            )
            .andExpect(status().isOk());

        // Validate the CaseSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCaseSessionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCaseSession, caseSession),
            getPersistedCaseSession(caseSession)
        );
    }

    @Test
    @Transactional
    void fullUpdateCaseSessionWithPatch() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caseSession using partial update
        CaseSession partialUpdatedCaseSession = new CaseSession();
        partialUpdatedCaseSession.setId(caseSession.getId());

        partialUpdatedCaseSession
            .sessionDate(UPDATED_SESSION_DATE)
            .sessionTime(UPDATED_SESSION_TIME)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCaseSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaseSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCaseSession))
            )
            .andExpect(status().isOk());

        // Validate the CaseSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCaseSessionUpdatableFieldsEquals(partialUpdatedCaseSession, getPersistedCaseSession(partialUpdatedCaseSession));
    }

    @Test
    @Transactional
    void patchNonExistingCaseSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseSession.setId(longCount.incrementAndGet());

        // Create the CaseSession
        CaseSessionDTO caseSessionDTO = caseSessionMapper.toDto(caseSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaseSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, caseSessionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(caseSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaseSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseSession.setId(longCount.incrementAndGet());

        // Create the CaseSession
        CaseSessionDTO caseSessionDTO = caseSessionMapper.toDto(caseSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(caseSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaseSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseSession.setId(longCount.incrementAndGet());

        // Create the CaseSession
        CaseSessionDTO caseSessionDTO = caseSessionMapper.toDto(caseSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseSessionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(caseSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CaseSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaseSession() throws Exception {
        // Initialize the database
        insertedCaseSession = caseSessionRepository.saveAndFlush(caseSession);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the caseSession
        restCaseSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, caseSession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return caseSessionRepository.count();
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

    protected CaseSession getPersistedCaseSession(CaseSession caseSession) {
        return caseSessionRepository.findById(caseSession.getId()).orElseThrow();
    }

    protected void assertPersistedCaseSessionToMatchAllProperties(CaseSession expectedCaseSession) {
        assertCaseSessionAllPropertiesEquals(expectedCaseSession, getPersistedCaseSession(expectedCaseSession));
    }

    protected void assertPersistedCaseSessionToMatchUpdatableProperties(CaseSession expectedCaseSession) {
        assertCaseSessionAllUpdatablePropertiesEquals(expectedCaseSession, getPersistedCaseSession(expectedCaseSession));
    }
}
