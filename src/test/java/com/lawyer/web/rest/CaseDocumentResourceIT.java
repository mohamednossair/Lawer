package com.lawyer.web.rest;

import static com.lawyer.domain.CaseDocumentAsserts.*;
import static com.lawyer.web.rest.TestUtil.createUpdateProxyForBean;
import static com.lawyer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawyer.IntegrationTest;
import com.lawyer.domain.CaseDocument;
import com.lawyer.repository.CaseDocumentRepository;
import com.lawyer.repository.UserRepository;
import com.lawyer.service.dto.CaseDocumentDTO;
import com.lawyer.service.mapper.CaseDocumentMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
 * Integration tests for the {@link CaseDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CaseDocumentResourceIT {

    private static final String DEFAULT_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final Integer DEFAULT_UPLOADED_BY = 1;
    private static final Integer UPDATED_UPLOADED_BY = 2;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

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

    @Autowired
    private CaseDocumentMapper caseDocumentMapper;

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
    public static CaseDocument createEntity() {
        return new CaseDocument()
            .documentName(DEFAULT_DOCUMENT_NAME)
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .filePath(DEFAULT_FILE_PATH)
            .uploadedBy(DEFAULT_UPLOADED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CaseDocument createUpdatedEntity() {
        return new CaseDocument()
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .filePath(UPDATED_FILE_PATH)
            .uploadedBy(UPDATED_UPLOADED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        caseDocument = createEntity();
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
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].uploadedBy").value(hasItem(DEFAULT_UPLOADED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
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
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.uploadedBy").value(DEFAULT_UPLOADED_BY))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
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
            .filePath(UPDATED_FILE_PATH)
            .uploadedBy(UPDATED_UPLOADED_BY)
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
            .filePath(UPDATED_FILE_PATH)
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
            .filePath(UPDATED_FILE_PATH)
            .uploadedBy(UPDATED_UPLOADED_BY)
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
