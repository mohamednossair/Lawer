package com.lawyer.web.rest;

import static com.lawyer.domain.CaseDocumentAsserts.*;
import static com.lawyer.web.rest.TestUtil.createUpdateProxyForBean;
import static com.lawyer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawyer.IntegrationTest;
import com.lawyer.domain.CaseDocument;
import com.lawyer.domain.Client;
import com.lawyer.domain.CourtCase;
import com.lawyer.domain.User;
import com.lawyer.repository.CaseDocumentRepository;
import com.lawyer.repository.UserRepository;
import com.lawyer.service.CaseDocumentService;
import com.lawyer.service.dto.CaseDocumentDTO;
import com.lawyer.service.mapper.CaseDocumentMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
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
 * Integration tests for the {@link CaseDocumentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CaseDocumentResourceIT {

    private static final String DEFAULT_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DOCUMENT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOCUMENT_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DOCUMENT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOCUMENT_FILE_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/case-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CaseDocumentRepository caseDocumentRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private CaseDocumentRepository caseDocumentRepositoryMock;

    @Autowired
    private CaseDocumentMapper caseDocumentMapper;

    @Mock
    private CaseDocumentService caseDocumentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCaseDocumentMockMvc;

    private CaseDocument caseDocument;

    private CaseDocument insertedCaseDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CaseDocument createEntity(EntityManager em) {
        CaseDocument caseDocument = new CaseDocument()
            .documentName(DEFAULT_DOCUMENT_NAME)
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .documentFile(DEFAULT_DOCUMENT_FILE)
            .documentFileContentType(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        caseDocument.setClient(client);
        // Add required entity
        CourtCase courtCase;
        if (TestUtil.findAll(em, CourtCase.class).isEmpty()) {
            courtCase = CourtCaseResourceIT.createEntity(em);
            em.persist(courtCase);
            em.flush();
        } else {
            courtCase = TestUtil.findAll(em, CourtCase.class).get(0);
        }
        caseDocument.setCourtCase(courtCase);
        return caseDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CaseDocument createUpdatedEntity(EntityManager em) {
        CaseDocument updatedCaseDocument = new CaseDocument()
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentFile(UPDATED_DOCUMENT_FILE)
            .documentFileContentType(UPDATED_DOCUMENT_FILE_CONTENT_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        updatedCaseDocument.setClient(client);
        // Add required entity
        CourtCase courtCase;
        if (TestUtil.findAll(em, CourtCase.class).isEmpty()) {
            courtCase = CourtCaseResourceIT.createUpdatedEntity(em);
            em.persist(courtCase);
            em.flush();
        } else {
            courtCase = TestUtil.findAll(em, CourtCase.class).get(0);
        }
        updatedCaseDocument.setCourtCase(courtCase);
        return updatedCaseDocument;
    }

    @BeforeEach
    public void initTest() {
        caseDocument = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCaseDocument != null) {
            caseDocumentRepository.delete(insertedCaseDocument);
            insertedCaseDocument = null;
        }
    }

    @Test
    @Transactional
    void createCaseDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CaseDocument
        CaseDocumentDTO caseDocumentDTO = caseDocumentMapper.toDto(caseDocument);
        var returnedCaseDocumentDTO = om.readValue(
            restCaseDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseDocumentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CaseDocumentDTO.class
        );

        // Validate the CaseDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCaseDocument = caseDocumentMapper.toEntity(returnedCaseDocumentDTO);
        assertCaseDocumentUpdatableFieldsEquals(returnedCaseDocument, getPersistedCaseDocument(returnedCaseDocument));

        insertedCaseDocument = returnedCaseDocument;
    }

    @Test
    @Transactional
    void createCaseDocumentWithExistingId() throws Exception {
        // Create the CaseDocument with an existing ID
        caseDocument.setId(1L);
        CaseDocumentDTO caseDocumentDTO = caseDocumentMapper.toDto(caseDocument);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaseDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CaseDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        caseDocument.setDocumentName(null);

        // Create the CaseDocument, which fails.
        CaseDocumentDTO caseDocumentDTO = caseDocumentMapper.toDto(caseDocument);

        restCaseDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        caseDocument.setDocumentType(null);

        // Create the CaseDocument, which fails.
        CaseDocumentDTO caseDocumentDTO = caseDocumentMapper.toDto(caseDocument);

        restCaseDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCaseDocuments() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList
        restCaseDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caseDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentName").value(hasItem(DEFAULT_DOCUMENT_NAME)))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)))
            .andExpect(jsonPath("$.[*].documentFileContentType").value(hasItem(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].documentFile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_DOCUMENT_FILE))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCaseDocumentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(caseDocumentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCaseDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(caseDocumentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCaseDocumentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(caseDocumentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCaseDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(caseDocumentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCaseDocument() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get the caseDocument
        restCaseDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, caseDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caseDocument.getId().intValue()))
            .andExpect(jsonPath("$.documentName").value(DEFAULT_DOCUMENT_NAME))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE))
            .andExpect(jsonPath("$.documentFileContentType").value(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.documentFile").value(Base64.getEncoder().encodeToString(DEFAULT_DOCUMENT_FILE)))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getCaseDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        Long id = caseDocument.getId();

        defaultCaseDocumentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCaseDocumentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCaseDocumentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByDocumentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where documentName equals to
        defaultCaseDocumentFiltering("documentName.equals=" + DEFAULT_DOCUMENT_NAME, "documentName.equals=" + UPDATED_DOCUMENT_NAME);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByDocumentNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where documentName in
        defaultCaseDocumentFiltering(
            "documentName.in=" + DEFAULT_DOCUMENT_NAME + "," + UPDATED_DOCUMENT_NAME,
            "documentName.in=" + UPDATED_DOCUMENT_NAME
        );
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByDocumentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where documentName is not null
        defaultCaseDocumentFiltering("documentName.specified=true", "documentName.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByDocumentNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where documentName contains
        defaultCaseDocumentFiltering("documentName.contains=" + DEFAULT_DOCUMENT_NAME, "documentName.contains=" + UPDATED_DOCUMENT_NAME);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByDocumentNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where documentName does not contain
        defaultCaseDocumentFiltering(
            "documentName.doesNotContain=" + UPDATED_DOCUMENT_NAME,
            "documentName.doesNotContain=" + DEFAULT_DOCUMENT_NAME
        );
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByDocumentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where documentType equals to
        defaultCaseDocumentFiltering("documentType.equals=" + DEFAULT_DOCUMENT_TYPE, "documentType.equals=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByDocumentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where documentType in
        defaultCaseDocumentFiltering(
            "documentType.in=" + DEFAULT_DOCUMENT_TYPE + "," + UPDATED_DOCUMENT_TYPE,
            "documentType.in=" + UPDATED_DOCUMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByDocumentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where documentType is not null
        defaultCaseDocumentFiltering("documentType.specified=true", "documentType.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByDocumentTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where documentType contains
        defaultCaseDocumentFiltering("documentType.contains=" + DEFAULT_DOCUMENT_TYPE, "documentType.contains=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByDocumentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where documentType does not contain
        defaultCaseDocumentFiltering(
            "documentType.doesNotContain=" + UPDATED_DOCUMENT_TYPE,
            "documentType.doesNotContain=" + DEFAULT_DOCUMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where createdAt equals to
        defaultCaseDocumentFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where createdAt in
        defaultCaseDocumentFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where createdAt is not null
        defaultCaseDocumentFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where createdAt is greater than or equal to
        defaultCaseDocumentFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where createdAt is less than or equal to
        defaultCaseDocumentFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where createdAt is less than
        defaultCaseDocumentFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where createdAt is greater than
        defaultCaseDocumentFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where updatedAt equals to
        defaultCaseDocumentFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where updatedAt in
        defaultCaseDocumentFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where updatedAt is not null
        defaultCaseDocumentFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where updatedAt is greater than or equal to
        defaultCaseDocumentFiltering(
            "updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT,
            "updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where updatedAt is less than or equal to
        defaultCaseDocumentFiltering("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT, "updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where updatedAt is less than
        defaultCaseDocumentFiltering("updatedAt.lessThan=" + UPDATED_UPDATED_AT, "updatedAt.lessThan=" + DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        // Get all the caseDocumentList where updatedAt is greater than
        defaultCaseDocumentFiltering("updatedAt.greaterThan=" + SMALLER_UPDATED_AT, "updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            caseDocumentRepository.saveAndFlush(caseDocument);
            client = ClientResourceIT.createEntity();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        caseDocument.setClient(client);
        caseDocumentRepository.saveAndFlush(caseDocument);
        Long clientId = client.getId();
        // Get all the caseDocumentList where client equals to clientId
        defaultCaseDocumentShouldBeFound("clientId.equals=" + clientId);

        // Get all the caseDocumentList where client equals to (clientId + 1)
        defaultCaseDocumentShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByCourtCaseIsEqualToSomething() throws Exception {
        CourtCase courtCase;
        if (TestUtil.findAll(em, CourtCase.class).isEmpty()) {
            caseDocumentRepository.saveAndFlush(caseDocument);
            courtCase = CourtCaseResourceIT.createEntity(em);
        } else {
            courtCase = TestUtil.findAll(em, CourtCase.class).get(0);
        }
        em.persist(courtCase);
        em.flush();
        caseDocument.setCourtCase(courtCase);
        caseDocumentRepository.saveAndFlush(caseDocument);
        Long courtCaseId = courtCase.getId();
        // Get all the caseDocumentList where courtCase equals to courtCaseId
        defaultCaseDocumentShouldBeFound("courtCaseId.equals=" + courtCaseId);

        // Get all the caseDocumentList where courtCase equals to (courtCaseId + 1)
        defaultCaseDocumentShouldNotBeFound("courtCaseId.equals=" + (courtCaseId + 1));
    }

    @Test
    @Transactional
    void getAllCaseDocumentsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            caseDocumentRepository.saveAndFlush(caseDocument);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        caseDocument.setUser(user);
        caseDocumentRepository.saveAndFlush(caseDocument);
        Long userId = user.getId();
        // Get all the caseDocumentList where user equals to userId
        defaultCaseDocumentShouldBeFound("userId.equals=" + userId);

        // Get all the caseDocumentList where user equals to (userId + 1)
        defaultCaseDocumentShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultCaseDocumentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCaseDocumentShouldBeFound(shouldBeFound);
        defaultCaseDocumentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCaseDocumentShouldBeFound(String filter) throws Exception {
        restCaseDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caseDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentName").value(hasItem(DEFAULT_DOCUMENT_NAME)))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)))
            .andExpect(jsonPath("$.[*].documentFileContentType").value(hasItem(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].documentFile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_DOCUMENT_FILE))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restCaseDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCaseDocumentShouldNotBeFound(String filter) throws Exception {
        restCaseDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCaseDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCaseDocument() throws Exception {
        // Get the caseDocument
        restCaseDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCaseDocument() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caseDocument
        CaseDocument updatedCaseDocument = caseDocumentRepository.findById(caseDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCaseDocument are not directly saved in db
        em.detach(updatedCaseDocument);
        updatedCaseDocument
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentFile(UPDATED_DOCUMENT_FILE)
            .documentFileContentType(UPDATED_DOCUMENT_FILE_CONTENT_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        CaseDocumentDTO caseDocumentDTO = caseDocumentMapper.toDto(updatedCaseDocument);

        restCaseDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caseDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(caseDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the CaseDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCaseDocumentToMatchAllProperties(updatedCaseDocument);
    }

    @Test
    @Transactional
    void putNonExistingCaseDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseDocument.setId(longCount.incrementAndGet());

        // Create the CaseDocument
        CaseDocumentDTO caseDocumentDTO = caseDocumentMapper.toDto(caseDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaseDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caseDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(caseDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaseDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseDocument.setId(longCount.incrementAndGet());

        // Create the CaseDocument
        CaseDocumentDTO caseDocumentDTO = caseDocumentMapper.toDto(caseDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(caseDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaseDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseDocument.setId(longCount.incrementAndGet());

        // Create the CaseDocument
        CaseDocumentDTO caseDocumentDTO = caseDocumentMapper.toDto(caseDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CaseDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCaseDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caseDocument using partial update
        CaseDocument partialUpdatedCaseDocument = new CaseDocument();
        partialUpdatedCaseDocument.setId(caseDocument.getId());

        partialUpdatedCaseDocument
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentFile(UPDATED_DOCUMENT_FILE)
            .documentFileContentType(UPDATED_DOCUMENT_FILE_CONTENT_TYPE);

        restCaseDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaseDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCaseDocument))
            )
            .andExpect(status().isOk());

        // Validate the CaseDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCaseDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCaseDocument, caseDocument),
            getPersistedCaseDocument(caseDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateCaseDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caseDocument using partial update
        CaseDocument partialUpdatedCaseDocument = new CaseDocument();
        partialUpdatedCaseDocument.setId(caseDocument.getId());

        partialUpdatedCaseDocument
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentFile(UPDATED_DOCUMENT_FILE)
            .documentFileContentType(UPDATED_DOCUMENT_FILE_CONTENT_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCaseDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaseDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCaseDocument))
            )
            .andExpect(status().isOk());

        // Validate the CaseDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCaseDocumentUpdatableFieldsEquals(partialUpdatedCaseDocument, getPersistedCaseDocument(partialUpdatedCaseDocument));
    }

    @Test
    @Transactional
    void patchNonExistingCaseDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseDocument.setId(longCount.incrementAndGet());

        // Create the CaseDocument
        CaseDocumentDTO caseDocumentDTO = caseDocumentMapper.toDto(caseDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaseDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, caseDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(caseDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaseDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseDocument.setId(longCount.incrementAndGet());

        // Create the CaseDocument
        CaseDocumentDTO caseDocumentDTO = caseDocumentMapper.toDto(caseDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(caseDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaseDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseDocument.setId(longCount.incrementAndGet());

        // Create the CaseDocument
        CaseDocumentDTO caseDocumentDTO = caseDocumentMapper.toDto(caseDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseDocumentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(caseDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CaseDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaseDocument() throws Exception {
        // Initialize the database
        insertedCaseDocument = caseDocumentRepository.saveAndFlush(caseDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the caseDocument
        restCaseDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, caseDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return caseDocumentRepository.count();
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

    protected CaseDocument getPersistedCaseDocument(CaseDocument caseDocument) {
        return caseDocumentRepository.findById(caseDocument.getId()).orElseThrow();
    }

    protected void assertPersistedCaseDocumentToMatchAllProperties(CaseDocument expectedCaseDocument) {
        assertCaseDocumentAllPropertiesEquals(expectedCaseDocument, getPersistedCaseDocument(expectedCaseDocument));
    }

    protected void assertPersistedCaseDocumentToMatchUpdatableProperties(CaseDocument expectedCaseDocument) {
        assertCaseDocumentAllUpdatablePropertiesEquals(expectedCaseDocument, getPersistedCaseDocument(expectedCaseDocument));
    }
}
