package com.lawyer.web.rest;

import static com.lawyer.domain.LawyerAsserts.*;
import static com.lawyer.web.rest.TestUtil.createUpdateProxyForBean;
import static com.lawyer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawyer.IntegrationTest;
import com.lawyer.domain.Lawyer;
import com.lawyer.repository.LawyerRepository;
import com.lawyer.service.dto.LawyerDTO;
import com.lawyer.service.mapper.LawyerMapper;
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
 * Integration tests for the {@link LawyerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LawyerResourceIT {

    private static final String DEFAULT_LAWYER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAWYER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIALIZATION = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALIZATION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_NUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/lawyers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LawyerRepository lawyerRepository;

    @Autowired
    private LawyerMapper lawyerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLawyerMockMvc;

    private Lawyer lawyer;

    private Lawyer insertedLawyer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lawyer createEntity() {
        return new Lawyer()
            .lawyerName(DEFAULT_LAWYER_NAME)
            .address(DEFAULT_ADDRESS)
            .contactNumber(DEFAULT_CONTACT_NUMBER)
            .specialization(DEFAULT_SPECIALIZATION)
            .email(DEFAULT_EMAIL)
            .registrationNumber(DEFAULT_REGISTRATION_NUMBER)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lawyer createUpdatedEntity() {
        return new Lawyer()
            .lawyerName(UPDATED_LAWYER_NAME)
            .address(UPDATED_ADDRESS)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .specialization(UPDATED_SPECIALIZATION)
            .email(UPDATED_EMAIL)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        lawyer = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLawyer != null) {
            lawyerRepository.delete(insertedLawyer);
            insertedLawyer = null;
        }
    }

    @Test
    @Transactional
    void createLawyer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Lawyer
        LawyerDTO lawyerDTO = lawyerMapper.toDto(lawyer);
        var returnedLawyerDTO = om.readValue(
            restLawyerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lawyerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LawyerDTO.class
        );

        // Validate the Lawyer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLawyer = lawyerMapper.toEntity(returnedLawyerDTO);
        assertLawyerUpdatableFieldsEquals(returnedLawyer, getPersistedLawyer(returnedLawyer));

        insertedLawyer = returnedLawyer;
    }

    @Test
    @Transactional
    void createLawyerWithExistingId() throws Exception {
        // Create the Lawyer with an existing ID
        lawyer.setId(1L);
        LawyerDTO lawyerDTO = lawyerMapper.toDto(lawyer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLawyerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lawyerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lawyer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLawyerNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lawyer.setLawyerName(null);

        // Create the Lawyer, which fails.
        LawyerDTO lawyerDTO = lawyerMapper.toDto(lawyer);

        restLawyerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lawyerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLawyers() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList
        restLawyerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lawyer.getId().intValue())))
            .andExpect(jsonPath("$.[*].lawyerName").value(hasItem(DEFAULT_LAWYER_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].specialization").value(hasItem(DEFAULT_SPECIALIZATION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getLawyer() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get the lawyer
        restLawyerMockMvc
            .perform(get(ENTITY_API_URL_ID, lawyer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lawyer.getId().intValue()))
            .andExpect(jsonPath("$.lawyerName").value(DEFAULT_LAWYER_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER))
            .andExpect(jsonPath("$.specialization").value(DEFAULT_SPECIALIZATION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REGISTRATION_NUMBER))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getLawyersByIdFiltering() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        Long id = lawyer.getId();

        defaultLawyerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLawyerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLawyerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLawyersByLawyerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where lawyerName equals to
        defaultLawyerFiltering("lawyerName.equals=" + DEFAULT_LAWYER_NAME, "lawyerName.equals=" + UPDATED_LAWYER_NAME);
    }

    @Test
    @Transactional
    void getAllLawyersByLawyerNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where lawyerName in
        defaultLawyerFiltering("lawyerName.in=" + DEFAULT_LAWYER_NAME + "," + UPDATED_LAWYER_NAME, "lawyerName.in=" + UPDATED_LAWYER_NAME);
    }

    @Test
    @Transactional
    void getAllLawyersByLawyerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where lawyerName is not null
        defaultLawyerFiltering("lawyerName.specified=true", "lawyerName.specified=false");
    }

    @Test
    @Transactional
    void getAllLawyersByLawyerNameContainsSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where lawyerName contains
        defaultLawyerFiltering("lawyerName.contains=" + DEFAULT_LAWYER_NAME, "lawyerName.contains=" + UPDATED_LAWYER_NAME);
    }

    @Test
    @Transactional
    void getAllLawyersByLawyerNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where lawyerName does not contain
        defaultLawyerFiltering("lawyerName.doesNotContain=" + UPDATED_LAWYER_NAME, "lawyerName.doesNotContain=" + DEFAULT_LAWYER_NAME);
    }

    @Test
    @Transactional
    void getAllLawyersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where address equals to
        defaultLawyerFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLawyersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where address in
        defaultLawyerFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLawyersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where address is not null
        defaultLawyerFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllLawyersByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where address contains
        defaultLawyerFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLawyersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where address does not contain
        defaultLawyerFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLawyersByContactNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where contactNumber equals to
        defaultLawyerFiltering("contactNumber.equals=" + DEFAULT_CONTACT_NUMBER, "contactNumber.equals=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLawyersByContactNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where contactNumber in
        defaultLawyerFiltering(
            "contactNumber.in=" + DEFAULT_CONTACT_NUMBER + "," + UPDATED_CONTACT_NUMBER,
            "contactNumber.in=" + UPDATED_CONTACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllLawyersByContactNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where contactNumber is not null
        defaultLawyerFiltering("contactNumber.specified=true", "contactNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllLawyersByContactNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where contactNumber contains
        defaultLawyerFiltering("contactNumber.contains=" + DEFAULT_CONTACT_NUMBER, "contactNumber.contains=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLawyersByContactNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where contactNumber does not contain
        defaultLawyerFiltering(
            "contactNumber.doesNotContain=" + UPDATED_CONTACT_NUMBER,
            "contactNumber.doesNotContain=" + DEFAULT_CONTACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllLawyersBySpecializationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where specialization equals to
        defaultLawyerFiltering("specialization.equals=" + DEFAULT_SPECIALIZATION, "specialization.equals=" + UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    void getAllLawyersBySpecializationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where specialization in
        defaultLawyerFiltering(
            "specialization.in=" + DEFAULT_SPECIALIZATION + "," + UPDATED_SPECIALIZATION,
            "specialization.in=" + UPDATED_SPECIALIZATION
        );
    }

    @Test
    @Transactional
    void getAllLawyersBySpecializationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where specialization is not null
        defaultLawyerFiltering("specialization.specified=true", "specialization.specified=false");
    }

    @Test
    @Transactional
    void getAllLawyersBySpecializationContainsSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where specialization contains
        defaultLawyerFiltering("specialization.contains=" + DEFAULT_SPECIALIZATION, "specialization.contains=" + UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    void getAllLawyersBySpecializationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where specialization does not contain
        defaultLawyerFiltering(
            "specialization.doesNotContain=" + UPDATED_SPECIALIZATION,
            "specialization.doesNotContain=" + DEFAULT_SPECIALIZATION
        );
    }

    @Test
    @Transactional
    void getAllLawyersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where email equals to
        defaultLawyerFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllLawyersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where email in
        defaultLawyerFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllLawyersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where email is not null
        defaultLawyerFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllLawyersByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where email contains
        defaultLawyerFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllLawyersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where email does not contain
        defaultLawyerFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllLawyersByRegistrationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where registrationNumber equals to
        defaultLawyerFiltering(
            "registrationNumber.equals=" + DEFAULT_REGISTRATION_NUMBER,
            "registrationNumber.equals=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllLawyersByRegistrationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where registrationNumber in
        defaultLawyerFiltering(
            "registrationNumber.in=" + DEFAULT_REGISTRATION_NUMBER + "," + UPDATED_REGISTRATION_NUMBER,
            "registrationNumber.in=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllLawyersByRegistrationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where registrationNumber is not null
        defaultLawyerFiltering("registrationNumber.specified=true", "registrationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllLawyersByRegistrationNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where registrationNumber contains
        defaultLawyerFiltering(
            "registrationNumber.contains=" + DEFAULT_REGISTRATION_NUMBER,
            "registrationNumber.contains=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllLawyersByRegistrationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where registrationNumber does not contain
        defaultLawyerFiltering(
            "registrationNumber.doesNotContain=" + UPDATED_REGISTRATION_NUMBER,
            "registrationNumber.doesNotContain=" + DEFAULT_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllLawyersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where createdAt equals to
        defaultLawyerFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllLawyersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where createdAt in
        defaultLawyerFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllLawyersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where createdAt is not null
        defaultLawyerFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllLawyersByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where createdAt is greater than or equal to
        defaultLawyerFiltering("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllLawyersByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where createdAt is less than or equal to
        defaultLawyerFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllLawyersByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where createdAt is less than
        defaultLawyerFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllLawyersByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where createdAt is greater than
        defaultLawyerFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllLawyersByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where updatedAt equals to
        defaultLawyerFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllLawyersByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where updatedAt in
        defaultLawyerFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllLawyersByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where updatedAt is not null
        defaultLawyerFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllLawyersByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where updatedAt is greater than or equal to
        defaultLawyerFiltering("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT, "updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllLawyersByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where updatedAt is less than or equal to
        defaultLawyerFiltering("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT, "updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllLawyersByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where updatedAt is less than
        defaultLawyerFiltering("updatedAt.lessThan=" + UPDATED_UPDATED_AT, "updatedAt.lessThan=" + DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllLawyersByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        // Get all the lawyerList where updatedAt is greater than
        defaultLawyerFiltering("updatedAt.greaterThan=" + SMALLER_UPDATED_AT, "updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);
    }

    private void defaultLawyerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLawyerShouldBeFound(shouldBeFound);
        defaultLawyerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLawyerShouldBeFound(String filter) throws Exception {
        restLawyerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lawyer.getId().intValue())))
            .andExpect(jsonPath("$.[*].lawyerName").value(hasItem(DEFAULT_LAWYER_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].specialization").value(hasItem(DEFAULT_SPECIALIZATION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restLawyerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLawyerShouldNotBeFound(String filter) throws Exception {
        restLawyerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLawyerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLawyer() throws Exception {
        // Get the lawyer
        restLawyerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLawyer() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lawyer
        Lawyer updatedLawyer = lawyerRepository.findById(lawyer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLawyer are not directly saved in db
        em.detach(updatedLawyer);
        updatedLawyer
            .lawyerName(UPDATED_LAWYER_NAME)
            .address(UPDATED_ADDRESS)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .specialization(UPDATED_SPECIALIZATION)
            .email(UPDATED_EMAIL)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        LawyerDTO lawyerDTO = lawyerMapper.toDto(updatedLawyer);

        restLawyerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lawyerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lawyerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Lawyer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLawyerToMatchAllProperties(updatedLawyer);
    }

    @Test
    @Transactional
    void putNonExistingLawyer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lawyer.setId(longCount.incrementAndGet());

        // Create the Lawyer
        LawyerDTO lawyerDTO = lawyerMapper.toDto(lawyer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLawyerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lawyerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lawyerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lawyer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLawyer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lawyer.setId(longCount.incrementAndGet());

        // Create the Lawyer
        LawyerDTO lawyerDTO = lawyerMapper.toDto(lawyer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLawyerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lawyerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lawyer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLawyer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lawyer.setId(longCount.incrementAndGet());

        // Create the Lawyer
        LawyerDTO lawyerDTO = lawyerMapper.toDto(lawyer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLawyerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lawyerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lawyer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLawyerWithPatch() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lawyer using partial update
        Lawyer partialUpdatedLawyer = new Lawyer();
        partialUpdatedLawyer.setId(lawyer.getId());

        partialUpdatedLawyer.lawyerName(UPDATED_LAWYER_NAME).specialization(UPDATED_SPECIALIZATION).email(UPDATED_EMAIL);

        restLawyerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLawyer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLawyer))
            )
            .andExpect(status().isOk());

        // Validate the Lawyer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLawyerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLawyer, lawyer), getPersistedLawyer(lawyer));
    }

    @Test
    @Transactional
    void fullUpdateLawyerWithPatch() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lawyer using partial update
        Lawyer partialUpdatedLawyer = new Lawyer();
        partialUpdatedLawyer.setId(lawyer.getId());

        partialUpdatedLawyer
            .lawyerName(UPDATED_LAWYER_NAME)
            .address(UPDATED_ADDRESS)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .specialization(UPDATED_SPECIALIZATION)
            .email(UPDATED_EMAIL)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restLawyerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLawyer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLawyer))
            )
            .andExpect(status().isOk());

        // Validate the Lawyer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLawyerUpdatableFieldsEquals(partialUpdatedLawyer, getPersistedLawyer(partialUpdatedLawyer));
    }

    @Test
    @Transactional
    void patchNonExistingLawyer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lawyer.setId(longCount.incrementAndGet());

        // Create the Lawyer
        LawyerDTO lawyerDTO = lawyerMapper.toDto(lawyer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLawyerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lawyerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lawyerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lawyer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLawyer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lawyer.setId(longCount.incrementAndGet());

        // Create the Lawyer
        LawyerDTO lawyerDTO = lawyerMapper.toDto(lawyer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLawyerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lawyerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lawyer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLawyer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lawyer.setId(longCount.incrementAndGet());

        // Create the Lawyer
        LawyerDTO lawyerDTO = lawyerMapper.toDto(lawyer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLawyerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(lawyerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lawyer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLawyer() throws Exception {
        // Initialize the database
        insertedLawyer = lawyerRepository.saveAndFlush(lawyer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the lawyer
        restLawyerMockMvc
            .perform(delete(ENTITY_API_URL_ID, lawyer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return lawyerRepository.count();
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

    protected Lawyer getPersistedLawyer(Lawyer lawyer) {
        return lawyerRepository.findById(lawyer.getId()).orElseThrow();
    }

    protected void assertPersistedLawyerToMatchAllProperties(Lawyer expectedLawyer) {
        assertLawyerAllPropertiesEquals(expectedLawyer, getPersistedLawyer(expectedLawyer));
    }

    protected void assertPersistedLawyerToMatchUpdatableProperties(Lawyer expectedLawyer) {
        assertLawyerAllUpdatablePropertiesEquals(expectedLawyer, getPersistedLawyer(expectedLawyer));
    }
}
