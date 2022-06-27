package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.RentalHistory;
import com.sonpj.repository.EntityManager;
import com.sonpj.repository.RentalHistoryRepository;
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
 * Integration tests for the {@link RentalHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RentalHistoryResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_BATTERY_ID = 1L;
    private static final Long UPDATED_BATTERY_ID = 2L;

    private static final Instant DEFAULT_TIME_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TIME_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/rental-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RentalHistoryRepository rentalHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private RentalHistory rentalHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RentalHistory createEntity(EntityManager em) {
        RentalHistory rentalHistory = new RentalHistory()
            .userId(DEFAULT_USER_ID)
            .batteryId(DEFAULT_BATTERY_ID)
            .timeStart(DEFAULT_TIME_START)
            .timeEnd(DEFAULT_TIME_END);
        return rentalHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RentalHistory createUpdatedEntity(EntityManager em) {
        RentalHistory rentalHistory = new RentalHistory()
            .userId(UPDATED_USER_ID)
            .batteryId(UPDATED_BATTERY_ID)
            .timeStart(UPDATED_TIME_START)
            .timeEnd(UPDATED_TIME_END);
        return rentalHistory;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(RentalHistory.class).block();
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
        rentalHistory = createEntity(em);
    }

    @Test
    void createRentalHistory() throws Exception {
        int databaseSizeBeforeCreate = rentalHistoryRepository.findAll().collectList().block().size();
        // Create the RentalHistory
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rentalHistory))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll().collectList().block();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        RentalHistory testRentalHistory = rentalHistoryList.get(rentalHistoryList.size() - 1);
        assertThat(testRentalHistory.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testRentalHistory.getBatteryId()).isEqualTo(DEFAULT_BATTERY_ID);
        assertThat(testRentalHistory.getTimeStart()).isEqualTo(DEFAULT_TIME_START);
        assertThat(testRentalHistory.getTimeEnd()).isEqualTo(DEFAULT_TIME_END);
    }

    @Test
    void createRentalHistoryWithExistingId() throws Exception {
        // Create the RentalHistory with an existing ID
        rentalHistory.setId(1L);

        int databaseSizeBeforeCreate = rentalHistoryRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rentalHistory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll().collectList().block();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRentalHistoriesAsStream() {
        // Initialize the database
        rentalHistoryRepository.save(rentalHistory).block();

        List<RentalHistory> rentalHistoryList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(RentalHistory.class)
            .getResponseBody()
            .filter(rentalHistory::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(rentalHistoryList).isNotNull();
        assertThat(rentalHistoryList).hasSize(1);
        RentalHistory testRentalHistory = rentalHistoryList.get(0);
        assertThat(testRentalHistory.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testRentalHistory.getBatteryId()).isEqualTo(DEFAULT_BATTERY_ID);
        assertThat(testRentalHistory.getTimeStart()).isEqualTo(DEFAULT_TIME_START);
        assertThat(testRentalHistory.getTimeEnd()).isEqualTo(DEFAULT_TIME_END);
    }

    @Test
    void getAllRentalHistories() {
        // Initialize the database
        rentalHistoryRepository.save(rentalHistory).block();

        // Get all the rentalHistoryList
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
            .value(hasItem(rentalHistory.getId().intValue()))
            .jsonPath("$.[*].userId")
            .value(hasItem(DEFAULT_USER_ID.intValue()))
            .jsonPath("$.[*].batteryId")
            .value(hasItem(DEFAULT_BATTERY_ID.intValue()))
            .jsonPath("$.[*].timeStart")
            .value(hasItem(DEFAULT_TIME_START.toString()))
            .jsonPath("$.[*].timeEnd")
            .value(hasItem(DEFAULT_TIME_END.toString()));
    }

    @Test
    void getRentalHistory() {
        // Initialize the database
        rentalHistoryRepository.save(rentalHistory).block();

        // Get the rentalHistory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, rentalHistory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(rentalHistory.getId().intValue()))
            .jsonPath("$.userId")
            .value(is(DEFAULT_USER_ID.intValue()))
            .jsonPath("$.batteryId")
            .value(is(DEFAULT_BATTERY_ID.intValue()))
            .jsonPath("$.timeStart")
            .value(is(DEFAULT_TIME_START.toString()))
            .jsonPath("$.timeEnd")
            .value(is(DEFAULT_TIME_END.toString()));
    }

    @Test
    void getNonExistingRentalHistory() {
        // Get the rentalHistory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewRentalHistory() throws Exception {
        // Initialize the database
        rentalHistoryRepository.save(rentalHistory).block();

        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().collectList().block().size();

        // Update the rentalHistory
        RentalHistory updatedRentalHistory = rentalHistoryRepository.findById(rentalHistory.getId()).block();
        updatedRentalHistory.userId(UPDATED_USER_ID).batteryId(UPDATED_BATTERY_ID).timeStart(UPDATED_TIME_START).timeEnd(UPDATED_TIME_END);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedRentalHistory.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedRentalHistory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll().collectList().block();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
        RentalHistory testRentalHistory = rentalHistoryList.get(rentalHistoryList.size() - 1);
        assertThat(testRentalHistory.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRentalHistory.getBatteryId()).isEqualTo(UPDATED_BATTERY_ID);
        assertThat(testRentalHistory.getTimeStart()).isEqualTo(UPDATED_TIME_START);
        assertThat(testRentalHistory.getTimeEnd()).isEqualTo(UPDATED_TIME_END);
    }

    @Test
    void putNonExistingRentalHistory() throws Exception {
        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().collectList().block().size();
        rentalHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, rentalHistory.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rentalHistory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll().collectList().block();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRentalHistory() throws Exception {
        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().collectList().block().size();
        rentalHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rentalHistory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll().collectList().block();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRentalHistory() throws Exception {
        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().collectList().block().size();
        rentalHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rentalHistory))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll().collectList().block();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRentalHistoryWithPatch() throws Exception {
        // Initialize the database
        rentalHistoryRepository.save(rentalHistory).block();

        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().collectList().block().size();

        // Update the rentalHistory using partial update
        RentalHistory partialUpdatedRentalHistory = new RentalHistory();
        partialUpdatedRentalHistory.setId(rentalHistory.getId());

        partialUpdatedRentalHistory.batteryId(UPDATED_BATTERY_ID).timeStart(UPDATED_TIME_START);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRentalHistory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRentalHistory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll().collectList().block();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
        RentalHistory testRentalHistory = rentalHistoryList.get(rentalHistoryList.size() - 1);
        assertThat(testRentalHistory.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testRentalHistory.getBatteryId()).isEqualTo(UPDATED_BATTERY_ID);
        assertThat(testRentalHistory.getTimeStart()).isEqualTo(UPDATED_TIME_START);
        assertThat(testRentalHistory.getTimeEnd()).isEqualTo(DEFAULT_TIME_END);
    }

    @Test
    void fullUpdateRentalHistoryWithPatch() throws Exception {
        // Initialize the database
        rentalHistoryRepository.save(rentalHistory).block();

        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().collectList().block().size();

        // Update the rentalHistory using partial update
        RentalHistory partialUpdatedRentalHistory = new RentalHistory();
        partialUpdatedRentalHistory.setId(rentalHistory.getId());

        partialUpdatedRentalHistory
            .userId(UPDATED_USER_ID)
            .batteryId(UPDATED_BATTERY_ID)
            .timeStart(UPDATED_TIME_START)
            .timeEnd(UPDATED_TIME_END);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRentalHistory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRentalHistory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll().collectList().block();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
        RentalHistory testRentalHistory = rentalHistoryList.get(rentalHistoryList.size() - 1);
        assertThat(testRentalHistory.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRentalHistory.getBatteryId()).isEqualTo(UPDATED_BATTERY_ID);
        assertThat(testRentalHistory.getTimeStart()).isEqualTo(UPDATED_TIME_START);
        assertThat(testRentalHistory.getTimeEnd()).isEqualTo(UPDATED_TIME_END);
    }

    @Test
    void patchNonExistingRentalHistory() throws Exception {
        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().collectList().block().size();
        rentalHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, rentalHistory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rentalHistory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll().collectList().block();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRentalHistory() throws Exception {
        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().collectList().block().size();
        rentalHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rentalHistory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll().collectList().block();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRentalHistory() throws Exception {
        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().collectList().block().size();
        rentalHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rentalHistory))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll().collectList().block();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRentalHistory() {
        // Initialize the database
        rentalHistoryRepository.save(rentalHistory).block();

        int databaseSizeBeforeDelete = rentalHistoryRepository.findAll().collectList().block().size();

        // Delete the rentalHistory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, rentalHistory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll().collectList().block();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
