package com.lawyer.web.rest;

import static com.lawyer.domain.CaseStatusAsserts.*;
import static com.lawyer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawyer.IntegrationTest;
import com.lawyer.domain.CaseStatus;
import com.lawyer.repository.CaseStatusRepository;
import com.lawyer.service.dto.CaseStatusDTO;
import com.lawyer.service.mapper.CaseStatusMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link CaseStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CaseStatusResourceIT {

    private static final String DEFAULT_CASE_STATUS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CASE_STATUS_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/case-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CaseStatusRepository caseStatusRepository;

    @Autowired
    private CaseStatusMapper caseStatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCaseStatusMockMvc;

    private CaseStatus caseStatus;

    private CaseStatus insertedCaseStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CaseStatus createEntity() {
        return new CaseStatus().caseStatusName(DEFAULT_CASE_STATUS_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CaseStatus createUpdatedEntity() {
        return new CaseStatus().caseStatusName(UPDATED_CASE_STATUS_NAME);
    }

    @BeforeEach
    public void initTest() {
        caseStatus = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCaseStatus != null) {
            caseStatusRepository.delete(insertedCaseStatus);
            insertedCaseStatus = null;
        }
    }

    @Test
    @Transactional
    void createCaseStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CaseStatus
        CaseStatusDTO caseStatusDTO = caseStatusMapper.toDto(caseStatus);
        var returnedCaseStatusDTO = om.readValue(
            restCaseStatusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseStatusDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CaseStatusDTO.class
        );

        // Validate the CaseStatus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCaseStatus = caseStatusMapper.toEntity(returnedCaseStatusDTO);
        assertCaseStatusUpdatableFieldsEquals(returnedCaseStatus, getPersistedCaseStatus(returnedCaseStatus));

        insertedCaseStatus = returnedCaseStatus;
    }

    @Test
    @Transactional
    void createCaseStatusWithExistingId() throws Exception {
        // Create the CaseStatus with an existing ID
        caseStatus.setId(1L);
        CaseStatusDTO caseStatusDTO = caseStatusMapper.toDto(caseStatus);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaseStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CaseStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCaseStatusNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        caseStatus.setCaseStatusName(null);

        // Create the CaseStatus, which fails.
        CaseStatusDTO caseStatusDTO = caseStatusMapper.toDto(caseStatus);

        restCaseStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCaseStatuses() throws Exception {
        // Initialize the database
        insertedCaseStatus = caseStatusRepository.saveAndFlush(caseStatus);

        // Get all the caseStatusList
        restCaseStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caseStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].caseStatusName").value(hasItem(DEFAULT_CASE_STATUS_NAME)));
    }

    @Test
    @Transactional
    void getCaseStatus() throws Exception {
        // Initialize the database
        insertedCaseStatus = caseStatusRepository.saveAndFlush(caseStatus);

        // Get the caseStatus
        restCaseStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, caseStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caseStatus.getId().intValue()))
            .andExpect(jsonPath("$.caseStatusName").value(DEFAULT_CASE_STATUS_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCaseStatus() throws Exception {
        // Get the caseStatus
        restCaseStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCaseStatus() throws Exception {
        // Initialize the database
        insertedCaseStatus = caseStatusRepository.saveAndFlush(caseStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caseStatus
        CaseStatus updatedCaseStatus = caseStatusRepository.findById(caseStatus.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCaseStatus are not directly saved in db
        em.detach(updatedCaseStatus);
        updatedCaseStatus.caseStatusName(UPDATED_CASE_STATUS_NAME);
        CaseStatusDTO caseStatusDTO = caseStatusMapper.toDto(updatedCaseStatus);

        restCaseStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caseStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(caseStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the CaseStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCaseStatusToMatchAllProperties(updatedCaseStatus);
    }

    @Test
    @Transactional
    void putNonExistingCaseStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseStatus.setId(longCount.incrementAndGet());

        // Create the CaseStatus
        CaseStatusDTO caseStatusDTO = caseStatusMapper.toDto(caseStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaseStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caseStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(caseStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaseStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseStatus.setId(longCount.incrementAndGet());

        // Create the CaseStatus
        CaseStatusDTO caseStatusDTO = caseStatusMapper.toDto(caseStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(caseStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaseStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseStatus.setId(longCount.incrementAndGet());

        // Create the CaseStatus
        CaseStatusDTO caseStatusDTO = caseStatusMapper.toDto(caseStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CaseStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCaseStatusWithPatch() throws Exception {
        // Initialize the database
        insertedCaseStatus = caseStatusRepository.saveAndFlush(caseStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caseStatus using partial update
        CaseStatus partialUpdatedCaseStatus = new CaseStatus();
        partialUpdatedCaseStatus.setId(caseStatus.getId());

        restCaseStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaseStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCaseStatus))
            )
            .andExpect(status().isOk());

        // Validate the CaseStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCaseStatusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCaseStatus, caseStatus),
            getPersistedCaseStatus(caseStatus)
        );
    }

    @Test
    @Transactional
    void fullUpdateCaseStatusWithPatch() throws Exception {
        // Initialize the database
        insertedCaseStatus = caseStatusRepository.saveAndFlush(caseStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caseStatus using partial update
        CaseStatus partialUpdatedCaseStatus = new CaseStatus();
        partialUpdatedCaseStatus.setId(caseStatus.getId());

        partialUpdatedCaseStatus.caseStatusName(UPDATED_CASE_STATUS_NAME);

        restCaseStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaseStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCaseStatus))
            )
            .andExpect(status().isOk());

        // Validate the CaseStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCaseStatusUpdatableFieldsEquals(partialUpdatedCaseStatus, getPersistedCaseStatus(partialUpdatedCaseStatus));
    }

    @Test
    @Transactional
    void patchNonExistingCaseStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseStatus.setId(longCount.incrementAndGet());

        // Create the CaseStatus
        CaseStatusDTO caseStatusDTO = caseStatusMapper.toDto(caseStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaseStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, caseStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(caseStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaseStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseStatus.setId(longCount.incrementAndGet());

        // Create the CaseStatus
        CaseStatusDTO caseStatusDTO = caseStatusMapper.toDto(caseStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(caseStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaseStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseStatus.setId(longCount.incrementAndGet());

        // Create the CaseStatus
        CaseStatusDTO caseStatusDTO = caseStatusMapper.toDto(caseStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseStatusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(caseStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CaseStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaseStatus() throws Exception {
        // Initialize the database
        insertedCaseStatus = caseStatusRepository.saveAndFlush(caseStatus);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the caseStatus
        restCaseStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, caseStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return caseStatusRepository.count();
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

    protected CaseStatus getPersistedCaseStatus(CaseStatus caseStatus) {
        return caseStatusRepository.findById(caseStatus.getId()).orElseThrow();
    }

    protected void assertPersistedCaseStatusToMatchAllProperties(CaseStatus expectedCaseStatus) {
        assertCaseStatusAllPropertiesEquals(expectedCaseStatus, getPersistedCaseStatus(expectedCaseStatus));
    }

    protected void assertPersistedCaseStatusToMatchUpdatableProperties(CaseStatus expectedCaseStatus) {
        assertCaseStatusAllUpdatablePropertiesEquals(expectedCaseStatus, getPersistedCaseStatus(expectedCaseStatus));
    }
}
