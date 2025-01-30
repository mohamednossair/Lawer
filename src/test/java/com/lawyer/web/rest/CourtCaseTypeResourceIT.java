package com.lawyer.web.rest;

import static com.lawyer.domain.CourtCaseTypeAsserts.*;
import static com.lawyer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawyer.IntegrationTest;
import com.lawyer.domain.CourtCaseType;
import com.lawyer.repository.CourtCaseTypeRepository;
import com.lawyer.service.dto.CourtCaseTypeDTO;
import com.lawyer.service.mapper.CourtCaseTypeMapper;
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
 * Integration tests for the {@link CourtCaseTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourtCaseTypeResourceIT {

    private static final String DEFAULT_CASE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CASE_TYPE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/court-case-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CourtCaseTypeRepository courtCaseTypeRepository;

    @Autowired
    private CourtCaseTypeMapper courtCaseTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourtCaseTypeMockMvc;

    private CourtCaseType courtCaseType;

    private CourtCaseType insertedCourtCaseType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourtCaseType createEntity() {
        return new CourtCaseType().caseTypeName(DEFAULT_CASE_TYPE_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourtCaseType createUpdatedEntity() {
        return new CourtCaseType().caseTypeName(UPDATED_CASE_TYPE_NAME);
    }

    @BeforeEach
    public void initTest() {
        courtCaseType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCourtCaseType != null) {
            courtCaseTypeRepository.delete(insertedCourtCaseType);
            insertedCourtCaseType = null;
        }
    }

    @Test
    @Transactional
    void createCourtCaseType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CourtCaseType
        CourtCaseTypeDTO courtCaseTypeDTO = courtCaseTypeMapper.toDto(courtCaseType);
        var returnedCourtCaseTypeDTO = om.readValue(
            restCourtCaseTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtCaseTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CourtCaseTypeDTO.class
        );

        // Validate the CourtCaseType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCourtCaseType = courtCaseTypeMapper.toEntity(returnedCourtCaseTypeDTO);
        assertCourtCaseTypeUpdatableFieldsEquals(returnedCourtCaseType, getPersistedCourtCaseType(returnedCourtCaseType));

        insertedCourtCaseType = returnedCourtCaseType;
    }

    @Test
    @Transactional
    void createCourtCaseTypeWithExistingId() throws Exception {
        // Create the CourtCaseType with an existing ID
        courtCaseType.setId(1L);
        CourtCaseTypeDTO courtCaseTypeDTO = courtCaseTypeMapper.toDto(courtCaseType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourtCaseTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtCaseTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourtCaseType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCaseTypeNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        courtCaseType.setCaseTypeName(null);

        // Create the CourtCaseType, which fails.
        CourtCaseTypeDTO courtCaseTypeDTO = courtCaseTypeMapper.toDto(courtCaseType);

        restCourtCaseTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtCaseTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourtCaseTypes() throws Exception {
        // Initialize the database
        insertedCourtCaseType = courtCaseTypeRepository.saveAndFlush(courtCaseType);

        // Get all the courtCaseTypeList
        restCourtCaseTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courtCaseType.getId().intValue())))
            .andExpect(jsonPath("$.[*].caseTypeName").value(hasItem(DEFAULT_CASE_TYPE_NAME)));
    }

    @Test
    @Transactional
    void getCourtCaseType() throws Exception {
        // Initialize the database
        insertedCourtCaseType = courtCaseTypeRepository.saveAndFlush(courtCaseType);

        // Get the courtCaseType
        restCourtCaseTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, courtCaseType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courtCaseType.getId().intValue()))
            .andExpect(jsonPath("$.caseTypeName").value(DEFAULT_CASE_TYPE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCourtCaseType() throws Exception {
        // Get the courtCaseType
        restCourtCaseTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCourtCaseType() throws Exception {
        // Initialize the database
        insertedCourtCaseType = courtCaseTypeRepository.saveAndFlush(courtCaseType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the courtCaseType
        CourtCaseType updatedCourtCaseType = courtCaseTypeRepository.findById(courtCaseType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCourtCaseType are not directly saved in db
        em.detach(updatedCourtCaseType);
        updatedCourtCaseType.caseTypeName(UPDATED_CASE_TYPE_NAME);
        CourtCaseTypeDTO courtCaseTypeDTO = courtCaseTypeMapper.toDto(updatedCourtCaseType);

        restCourtCaseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courtCaseTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(courtCaseTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CourtCaseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCourtCaseTypeToMatchAllProperties(updatedCourtCaseType);
    }

    @Test
    @Transactional
    void putNonExistingCourtCaseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courtCaseType.setId(longCount.incrementAndGet());

        // Create the CourtCaseType
        CourtCaseTypeDTO courtCaseTypeDTO = courtCaseTypeMapper.toDto(courtCaseType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourtCaseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courtCaseTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(courtCaseTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourtCaseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourtCaseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courtCaseType.setId(longCount.incrementAndGet());

        // Create the CourtCaseType
        CourtCaseTypeDTO courtCaseTypeDTO = courtCaseTypeMapper.toDto(courtCaseType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourtCaseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(courtCaseTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourtCaseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourtCaseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courtCaseType.setId(longCount.incrementAndGet());

        // Create the CourtCaseType
        CourtCaseTypeDTO courtCaseTypeDTO = courtCaseTypeMapper.toDto(courtCaseType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourtCaseTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtCaseTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourtCaseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourtCaseTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCourtCaseType = courtCaseTypeRepository.saveAndFlush(courtCaseType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the courtCaseType using partial update
        CourtCaseType partialUpdatedCourtCaseType = new CourtCaseType();
        partialUpdatedCourtCaseType.setId(courtCaseType.getId());

        restCourtCaseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourtCaseType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCourtCaseType))
            )
            .andExpect(status().isOk());

        // Validate the CourtCaseType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCourtCaseTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCourtCaseType, courtCaseType),
            getPersistedCourtCaseType(courtCaseType)
        );
    }

    @Test
    @Transactional
    void fullUpdateCourtCaseTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCourtCaseType = courtCaseTypeRepository.saveAndFlush(courtCaseType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the courtCaseType using partial update
        CourtCaseType partialUpdatedCourtCaseType = new CourtCaseType();
        partialUpdatedCourtCaseType.setId(courtCaseType.getId());

        partialUpdatedCourtCaseType.caseTypeName(UPDATED_CASE_TYPE_NAME);

        restCourtCaseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourtCaseType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCourtCaseType))
            )
            .andExpect(status().isOk());

        // Validate the CourtCaseType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCourtCaseTypeUpdatableFieldsEquals(partialUpdatedCourtCaseType, getPersistedCourtCaseType(partialUpdatedCourtCaseType));
    }

    @Test
    @Transactional
    void patchNonExistingCourtCaseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courtCaseType.setId(longCount.incrementAndGet());

        // Create the CourtCaseType
        CourtCaseTypeDTO courtCaseTypeDTO = courtCaseTypeMapper.toDto(courtCaseType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourtCaseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courtCaseTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(courtCaseTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourtCaseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourtCaseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courtCaseType.setId(longCount.incrementAndGet());

        // Create the CourtCaseType
        CourtCaseTypeDTO courtCaseTypeDTO = courtCaseTypeMapper.toDto(courtCaseType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourtCaseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(courtCaseTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourtCaseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourtCaseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courtCaseType.setId(longCount.incrementAndGet());

        // Create the CourtCaseType
        CourtCaseTypeDTO courtCaseTypeDTO = courtCaseTypeMapper.toDto(courtCaseType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourtCaseTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(courtCaseTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourtCaseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourtCaseType() throws Exception {
        // Initialize the database
        insertedCourtCaseType = courtCaseTypeRepository.saveAndFlush(courtCaseType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the courtCaseType
        restCourtCaseTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, courtCaseType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return courtCaseTypeRepository.count();
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

    protected CourtCaseType getPersistedCourtCaseType(CourtCaseType courtCaseType) {
        return courtCaseTypeRepository.findById(courtCaseType.getId()).orElseThrow();
    }

    protected void assertPersistedCourtCaseTypeToMatchAllProperties(CourtCaseType expectedCourtCaseType) {
        assertCourtCaseTypeAllPropertiesEquals(expectedCourtCaseType, getPersistedCourtCaseType(expectedCourtCaseType));
    }

    protected void assertPersistedCourtCaseTypeToMatchUpdatableProperties(CourtCaseType expectedCourtCaseType) {
        assertCourtCaseTypeAllUpdatablePropertiesEquals(expectedCourtCaseType, getPersistedCourtCaseType(expectedCourtCaseType));
    }
}
