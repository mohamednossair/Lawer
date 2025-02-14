package com.lawyer.web.rest;

import static com.lawyer.domain.ClientAsserts.*;
import static com.lawyer.web.rest.TestUtil.createUpdateProxyForBean;
import static com.lawyer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawyer.IntegrationTest;
import com.lawyer.domain.Client;
import com.lawyer.repository.ClientRepository;
import com.lawyer.service.dto.ClientDTO;
import com.lawyer.service.mapper.ClientMapper;
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
 * Integration tests for the {@link ClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientResourceIT {

    private static final String DEFAULT_CLIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_NATIONAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private Client client;

    private Client insertedClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity() {
        return new Client()
            .clientName(DEFAULT_CLIENT_NAME)
            .clientDescription(DEFAULT_CLIENT_DESCRIPTION)
            .contactNumber(DEFAULT_CONTACT_NUMBER)
            .address(DEFAULT_ADDRESS)
            .nationalId(DEFAULT_NATIONAL_ID)
            .email(DEFAULT_EMAIL)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createUpdatedEntity() {
        return new Client()
            .clientName(UPDATED_CLIENT_NAME)
            .clientDescription(UPDATED_CLIENT_DESCRIPTION)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .address(UPDATED_ADDRESS)
            .nationalId(UPDATED_NATIONAL_ID)
            .email(UPDATED_EMAIL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        client = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedClient != null) {
            clientRepository.delete(insertedClient);
            insertedClient = null;
        }
    }

    @Test
    @Transactional
    void createClient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);
        var returnedClientDTO = om.readValue(
            restClientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientDTO.class
        );

        // Validate the Client in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClient = clientMapper.toEntity(returnedClientDTO);
        assertClientUpdatableFieldsEquals(returnedClient, getPersistedClient(returnedClient));

        insertedClient = returnedClient;
    }

    @Test
    @Transactional
    void createClientWithExistingId() throws Exception {
        // Create the Client with an existing ID
        client.setId(1L);
        ClientDTO clientDTO = clientMapper.toDto(client);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkClientNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setClientName(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setContactNumber(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClients() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME)))
            .andExpect(jsonPath("$.[*].clientDescription").value(hasItem(DEFAULT_CLIENT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].nationalId").value(hasItem(DEFAULT_NATIONAL_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc
            .perform(get(ENTITY_API_URL_ID, client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.clientName").value(DEFAULT_CLIENT_NAME))
            .andExpect(jsonPath("$.clientDescription").value(DEFAULT_CLIENT_DESCRIPTION))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.nationalId").value(DEFAULT_NATIONAL_ID))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getClientsByIdFiltering() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        Long id = client.getId();

        defaultClientFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultClientFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultClientFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClientsByClientNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where clientName equals to
        defaultClientFiltering("clientName.equals=" + DEFAULT_CLIENT_NAME, "clientName.equals=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByClientNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where clientName in
        defaultClientFiltering("clientName.in=" + DEFAULT_CLIENT_NAME + "," + UPDATED_CLIENT_NAME, "clientName.in=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByClientNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where clientName is not null
        defaultClientFiltering("clientName.specified=true", "clientName.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByClientNameContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where clientName contains
        defaultClientFiltering("clientName.contains=" + DEFAULT_CLIENT_NAME, "clientName.contains=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByClientNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where clientName does not contain
        defaultClientFiltering("clientName.doesNotContain=" + UPDATED_CLIENT_NAME, "clientName.doesNotContain=" + DEFAULT_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByClientDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where clientDescription equals to
        defaultClientFiltering(
            "clientDescription.equals=" + DEFAULT_CLIENT_DESCRIPTION,
            "clientDescription.equals=" + UPDATED_CLIENT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllClientsByClientDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where clientDescription in
        defaultClientFiltering(
            "clientDescription.in=" + DEFAULT_CLIENT_DESCRIPTION + "," + UPDATED_CLIENT_DESCRIPTION,
            "clientDescription.in=" + UPDATED_CLIENT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllClientsByClientDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where clientDescription is not null
        defaultClientFiltering("clientDescription.specified=true", "clientDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByClientDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where clientDescription contains
        defaultClientFiltering(
            "clientDescription.contains=" + DEFAULT_CLIENT_DESCRIPTION,
            "clientDescription.contains=" + UPDATED_CLIENT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllClientsByClientDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where clientDescription does not contain
        defaultClientFiltering(
            "clientDescription.doesNotContain=" + UPDATED_CLIENT_DESCRIPTION,
            "clientDescription.doesNotContain=" + DEFAULT_CLIENT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllClientsByContactNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where contactNumber equals to
        defaultClientFiltering("contactNumber.equals=" + DEFAULT_CONTACT_NUMBER, "contactNumber.equals=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllClientsByContactNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where contactNumber in
        defaultClientFiltering(
            "contactNumber.in=" + DEFAULT_CONTACT_NUMBER + "," + UPDATED_CONTACT_NUMBER,
            "contactNumber.in=" + UPDATED_CONTACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByContactNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where contactNumber is not null
        defaultClientFiltering("contactNumber.specified=true", "contactNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByContactNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where contactNumber contains
        defaultClientFiltering("contactNumber.contains=" + DEFAULT_CONTACT_NUMBER, "contactNumber.contains=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllClientsByContactNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where contactNumber does not contain
        defaultClientFiltering(
            "contactNumber.doesNotContain=" + UPDATED_CONTACT_NUMBER,
            "contactNumber.doesNotContain=" + DEFAULT_CONTACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where address equals to
        defaultClientFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClientsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where address in
        defaultClientFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClientsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where address is not null
        defaultClientFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where address contains
        defaultClientFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClientsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where address does not contain
        defaultClientFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClientsByNationalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nationalId equals to
        defaultClientFiltering("nationalId.equals=" + DEFAULT_NATIONAL_ID, "nationalId.equals=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllClientsByNationalIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nationalId in
        defaultClientFiltering("nationalId.in=" + DEFAULT_NATIONAL_ID + "," + UPDATED_NATIONAL_ID, "nationalId.in=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllClientsByNationalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nationalId is not null
        defaultClientFiltering("nationalId.specified=true", "nationalId.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNationalIdContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nationalId contains
        defaultClientFiltering("nationalId.contains=" + DEFAULT_NATIONAL_ID, "nationalId.contains=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllClientsByNationalIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nationalId does not contain
        defaultClientFiltering("nationalId.doesNotContain=" + UPDATED_NATIONAL_ID, "nationalId.doesNotContain=" + DEFAULT_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllClientsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email equals to
        defaultClientFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email in
        defaultClientFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email is not null
        defaultClientFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email contains
        defaultClientFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email does not contain
        defaultClientFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdAt equals to
        defaultClientFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllClientsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdAt in
        defaultClientFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllClientsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdAt is not null
        defaultClientFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdAt is greater than or equal to
        defaultClientFiltering("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllClientsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdAt is less than or equal to
        defaultClientFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllClientsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdAt is less than
        defaultClientFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllClientsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where createdAt is greater than
        defaultClientFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllClientsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where updatedAt equals to
        defaultClientFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllClientsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where updatedAt in
        defaultClientFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllClientsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where updatedAt is not null
        defaultClientFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where updatedAt is greater than or equal to
        defaultClientFiltering("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT, "updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllClientsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where updatedAt is less than or equal to
        defaultClientFiltering("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT, "updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllClientsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where updatedAt is less than
        defaultClientFiltering("updatedAt.lessThan=" + UPDATED_UPDATED_AT, "updatedAt.lessThan=" + DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllClientsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where updatedAt is greater than
        defaultClientFiltering("updatedAt.greaterThan=" + SMALLER_UPDATED_AT, "updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);
    }

    private void defaultClientFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultClientShouldBeFound(shouldBeFound);
        defaultClientShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClientShouldBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME)))
            .andExpect(jsonPath("$.[*].clientDescription").value(hasItem(DEFAULT_CLIENT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].nationalId").value(hasItem(DEFAULT_NATIONAL_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClientShouldNotBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .clientName(UPDATED_CLIENT_NAME)
            .clientDescription(UPDATED_CLIENT_DESCRIPTION)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .address(UPDATED_ADDRESS)
            .nationalId(UPDATED_NATIONAL_ID)
            .email(UPDATED_EMAIL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ClientDTO clientDTO = clientMapper.toDto(updatedClient);

        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClientToMatchAllProperties(updatedClient);
    }

    @Test
    @Transactional
    void putNonExistingClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientWithPatch() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .clientName(UPDATED_CLIENT_NAME)
            .clientDescription(UPDATED_CLIENT_DESCRIPTION)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedClient, client), getPersistedClient(client));
    }

    @Test
    @Transactional
    void fullUpdateClientWithPatch() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .clientName(UPDATED_CLIENT_NAME)
            .clientDescription(UPDATED_CLIENT_DESCRIPTION)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .address(UPDATED_ADDRESS)
            .nationalId(UPDATED_NATIONAL_ID)
            .email(UPDATED_EMAIL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientUpdatableFieldsEquals(partialUpdatedClient, getPersistedClient(partialUpdatedClient));
    }

    @Test
    @Transactional
    void patchNonExistingClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the client
        restClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, client.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clientRepository.count();
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

    protected Client getPersistedClient(Client client) {
        return clientRepository.findById(client.getId()).orElseThrow();
    }

    protected void assertPersistedClientToMatchAllProperties(Client expectedClient) {
        assertClientAllPropertiesEquals(expectedClient, getPersistedClient(expectedClient));
    }

    protected void assertPersistedClientToMatchUpdatableProperties(Client expectedClient) {
        assertClientAllUpdatablePropertiesEquals(expectedClient, getPersistedClient(expectedClient));
    }
}
