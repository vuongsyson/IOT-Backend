package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.SosRequest;
import com.sonpj.domain.enumeration.SosState;
import com.sonpj.repository.EntityManager;
import com.sonpj.repository.SosRequestRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link SosRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SosRequestResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final SosState DEFAULT_STATE = SosState.SEND_REQUEST;
    private static final SosState UPDATED_STATE = SosState.REQUEST_RECEIVED;

    private static final Integer DEFAULT_RATING = 0;
    private static final Integer UPDATED_RATING = 1;

    private static final Boolean DEFAULT_DONE = false;
    private static final Boolean UPDATED_DONE = true;

    private static final Instant DEFAULT_DONE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DONE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sos-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SosRequestRepository sosRequestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SosRequest sosRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SosRequest createEntity(EntityManager em) {
        SosRequest sosRequest = new SosRequest()
            .userId(DEFAULT_USER_ID)
            .phone(DEFAULT_PHONE)
            .deviceSerialNumber(DEFAULT_DEVICE_SERIAL_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .image(DEFAULT_IMAGE)
            .state(DEFAULT_STATE)
            .rating(DEFAULT_RATING)
            .done(DEFAULT_DONE)
            .doneTime(DEFAULT_DONE_TIME);
        return sosRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SosRequest createUpdatedEntity(EntityManager em) {
        SosRequest sosRequest = new SosRequest()
            .userId(UPDATED_USER_ID)
            .phone(UPDATED_PHONE)
            .deviceSerialNumber(UPDATED_DEVICE_SERIAL_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .state(UPDATED_STATE)
            .rating(UPDATED_RATING)
            .done(UPDATED_DONE)
            .doneTime(UPDATED_DONE_TIME);
        return sosRequest;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SosRequest.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        sosRequest = createEntity(em);
    }

    @Test
    void createSosRequest() throws Exception {
        int databaseSizeBeforeCreate = sosRequestRepository.findAll().collectList().block().size();
        // Create the SosRequest
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosRequest))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeCreate + 1);
        SosRequest testSosRequest = sosRequestList.get(sosRequestList.size() - 1);
        assertThat(testSosRequest.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testSosRequest.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSosRequest.getDeviceSerialNumber()).isEqualTo(DEFAULT_DEVICE_SERIAL_NUMBER);
        assertThat(testSosRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSosRequest.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testSosRequest.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testSosRequest.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testSosRequest.getDone()).isEqualTo(DEFAULT_DONE);
        assertThat(testSosRequest.getDoneTime()).isEqualTo(DEFAULT_DONE_TIME);
    }

    @Test
    void createSosRequestWithExistingId() throws Exception {
        // Create the SosRequest with an existing ID
        sosRequest.setId(1L);

        int databaseSizeBeforeCreate = sosRequestRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = sosRequestRepository.findAll().collectList().block().size();
        // set the field null
        sosRequest.setUserId(null);

        // Create the SosRequest, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = sosRequestRepository.findAll().collectList().block().size();
        // set the field null
        sosRequest.setPhone(null);

        // Create the SosRequest, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDeviceSerialNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = sosRequestRepository.findAll().collectList().block().size();
        // set the field null
        sosRequest.setDeviceSerialNumber(null);

        // Create the SosRequest, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllSosRequestsAsStream() {
        // Initialize the database
        sosRequestRepository.save(sosRequest).block();

        List<SosRequest> sosRequestList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(SosRequest.class)
            .getResponseBody()
            .filter(sosRequest::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(sosRequestList).isNotNull();
        assertThat(sosRequestList).hasSize(1);
        SosRequest testSosRequest = sosRequestList.get(0);
        assertThat(testSosRequest.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testSosRequest.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSosRequest.getDeviceSerialNumber()).isEqualTo(DEFAULT_DEVICE_SERIAL_NUMBER);
        assertThat(testSosRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSosRequest.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testSosRequest.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testSosRequest.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testSosRequest.getDone()).isEqualTo(DEFAULT_DONE);
        assertThat(testSosRequest.getDoneTime()).isEqualTo(DEFAULT_DONE_TIME);
    }

    @Test
    void getAllSosRequests() {
        // Initialize the database
        sosRequestRepository.save(sosRequest).block();

        // Get all the sosRequestList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(sosRequest.getId().intValue()))
            .jsonPath("$.[*].userId")
            .value(hasItem(DEFAULT_USER_ID.intValue()))
            .jsonPath("$.[*].phone")
            .value(hasItem(DEFAULT_PHONE))
            .jsonPath("$.[*].deviceSerialNumber")
            .value(hasItem(DEFAULT_DEVICE_SERIAL_NUMBER))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].image")
            .value(hasItem(DEFAULT_IMAGE))
            .jsonPath("$.[*].state")
            .value(hasItem(DEFAULT_STATE.toString()))
            .jsonPath("$.[*].rating")
            .value(hasItem(DEFAULT_RATING))
            .jsonPath("$.[*].done")
            .value(hasItem(DEFAULT_DONE.booleanValue()))
            .jsonPath("$.[*].doneTime")
            .value(hasItem(DEFAULT_DONE_TIME.toString()));
    }

    @Test
    void getSosRequest() {
        // Initialize the database
        sosRequestRepository.save(sosRequest).block();

        // Get the sosRequest
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sosRequest.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sosRequest.getId().intValue()))
            .jsonPath("$.userId")
            .value(is(DEFAULT_USER_ID.intValue()))
            .jsonPath("$.phone")
            .value(is(DEFAULT_PHONE))
            .jsonPath("$.deviceSerialNumber")
            .value(is(DEFAULT_DEVICE_SERIAL_NUMBER))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.image")
            .value(is(DEFAULT_IMAGE))
            .jsonPath("$.state")
            .value(is(DEFAULT_STATE.toString()))
            .jsonPath("$.rating")
            .value(is(DEFAULT_RATING))
            .jsonPath("$.done")
            .value(is(DEFAULT_DONE.booleanValue()))
            .jsonPath("$.doneTime")
            .value(is(DEFAULT_DONE_TIME.toString()));
    }

    @Test
    void getNonExistingSosRequest() {
        // Get the sosRequest
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSosRequest() throws Exception {
        // Initialize the database
        sosRequestRepository.save(sosRequest).block();

        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().collectList().block().size();

        // Update the sosRequest
        SosRequest updatedSosRequest = sosRequestRepository.findById(sosRequest.getId()).block();
        updatedSosRequest
            .userId(UPDATED_USER_ID)
            .phone(UPDATED_PHONE)
            .deviceSerialNumber(UPDATED_DEVICE_SERIAL_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .state(UPDATED_STATE)
            .rating(UPDATED_RATING)
            .done(UPDATED_DONE)
            .doneTime(UPDATED_DONE_TIME);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSosRequest.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedSosRequest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
        SosRequest testSosRequest = sosRequestList.get(sosRequestList.size() - 1);
        assertThat(testSosRequest.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testSosRequest.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSosRequest.getDeviceSerialNumber()).isEqualTo(UPDATED_DEVICE_SERIAL_NUMBER);
        assertThat(testSosRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSosRequest.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSosRequest.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSosRequest.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testSosRequest.getDone()).isEqualTo(UPDATED_DONE);
        assertThat(testSosRequest.getDoneTime()).isEqualTo(UPDATED_DONE_TIME);
    }

    @Test
    void putNonExistingSosRequest() throws Exception {
        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().collectList().block().size();
        sosRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sosRequest.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSosRequest() throws Exception {
        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().collectList().block().size();
        sosRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSosRequest() throws Exception {
        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().collectList().block().size();
        sosRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosRequest))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSosRequestWithPatch() throws Exception {
        // Initialize the database
        sosRequestRepository.save(sosRequest).block();

        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().collectList().block().size();

        // Update the sosRequest using partial update
        SosRequest partialUpdatedSosRequest = new SosRequest();
        partialUpdatedSosRequest.setId(sosRequest.getId());

        partialUpdatedSosRequest
            .userId(UPDATED_USER_ID)
            .deviceSerialNumber(UPDATED_DEVICE_SERIAL_NUMBER)
            .image(UPDATED_IMAGE)
            .state(UPDATED_STATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSosRequest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSosRequest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
        SosRequest testSosRequest = sosRequestList.get(sosRequestList.size() - 1);
        assertThat(testSosRequest.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testSosRequest.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSosRequest.getDeviceSerialNumber()).isEqualTo(UPDATED_DEVICE_SERIAL_NUMBER);
        assertThat(testSosRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSosRequest.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSosRequest.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSosRequest.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testSosRequest.getDone()).isEqualTo(DEFAULT_DONE);
        assertThat(testSosRequest.getDoneTime()).isEqualTo(DEFAULT_DONE_TIME);
    }

    @Test
    void fullUpdateSosRequestWithPatch() throws Exception {
        // Initialize the database
        sosRequestRepository.save(sosRequest).block();

        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().collectList().block().size();

        // Update the sosRequest using partial update
        SosRequest partialUpdatedSosRequest = new SosRequest();
        partialUpdatedSosRequest.setId(sosRequest.getId());

        partialUpdatedSosRequest
            .userId(UPDATED_USER_ID)
            .phone(UPDATED_PHONE)
            .deviceSerialNumber(UPDATED_DEVICE_SERIAL_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .state(UPDATED_STATE)
            .rating(UPDATED_RATING)
            .done(UPDATED_DONE)
            .doneTime(UPDATED_DONE_TIME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSosRequest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSosRequest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
        SosRequest testSosRequest = sosRequestList.get(sosRequestList.size() - 1);
        assertThat(testSosRequest.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testSosRequest.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSosRequest.getDeviceSerialNumber()).isEqualTo(UPDATED_DEVICE_SERIAL_NUMBER);
        assertThat(testSosRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSosRequest.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSosRequest.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSosRequest.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testSosRequest.getDone()).isEqualTo(UPDATED_DONE);
        assertThat(testSosRequest.getDoneTime()).isEqualTo(UPDATED_DONE_TIME);
    }

    @Test
    void patchNonExistingSosRequest() throws Exception {
        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().collectList().block().size();
        sosRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sosRequest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSosRequest() throws Exception {
        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().collectList().block().size();
        sosRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSosRequest() throws Exception {
        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().collectList().block().size();
        sosRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosRequest))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSosRequest() {
        // Initialize the database
        sosRequestRepository.save(sosRequest).block();

        int databaseSizeBeforeDelete = sosRequestRepository.findAll().collectList().block().size();

        // Delete the sosRequest
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sosRequest.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SosRequest> sosRequestList = sosRequestRepository.findAll().collectList().block();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
