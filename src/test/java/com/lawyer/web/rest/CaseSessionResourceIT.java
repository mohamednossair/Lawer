package com.lawyer.web.rest;

import static com.lawyer.domain.CaseSessionAsserts.*;
import static com.lawyer.web.rest.TestUtil.createUpdateProxyForBean;
import static com.lawyer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawyer.IntegrationTest;
import com.lawyer.domain.CaseSession;
import com.lawyer.repository.CaseSessionRepository;
import com.lawyer.service.dto.CaseSessionDTO;
import com.lawyer.service.mapper.CaseSessionMapper;
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
 * Integration tests for the {@link CaseSessionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CaseSessionResourceIT {

    private static final LocalDate DEFAULT_SESSION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SESSION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_SESSION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SESSION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/case-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CaseSessionRepository caseSessionRepository;

    @Autowired
    private CaseSessionMapper caseSessionMapper;

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
