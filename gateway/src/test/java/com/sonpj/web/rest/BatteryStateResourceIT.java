package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.BatteryState;
import com.sonpj.repository.BatteryStateRepository;
import com.sonpj.repository.EntityManager;
import java.time.Duration;
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
 * Integration tests for the {@link BatteryStateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class BatteryStateResourceIT {

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_VOL = 1;
    private static final Integer UPDATED_VOL = 2;

    private static final Integer DEFAULT_CUR = 1;
    private static final Integer UPDATED_CUR = 2;

    private static final Integer DEFAULT_SOC = 1;
    private static final Integer UPDATED_SOC = 2;

    private static final Integer DEFAULT_SOH = 1;
    private static final Integer UPDATED_SOH = 2;

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String ENTITY_API_URL = "/api/battery-states";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BatteryStateRepository batteryStateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private BatteryState batteryState;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BatteryState createEntity(EntityManager em) {
        BatteryState batteryState = new BatteryState()
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .vol(DEFAULT_VOL)
            .cur(DEFAULT_CUR)
            .soc(DEFAULT_SOC)
            .soh(DEFAULT_SOH)
            .state(DEFAULT_STATE)
            .status(DEFAULT_STATUS);
        return batteryState;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BatteryState createUpdatedEntity(EntityManager em) {
        BatteryState batteryState = new BatteryState()
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .vol(UPDATED_VOL)
            .cur(UPDATED_CUR)
            .soc(UPDATED_SOC)
            .soh(UPDATED_SOH)
            .state(UPDATED_STATE)
            .status(UPDATED_STATUS);
        return batteryState;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(BatteryState.class).block();
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
        batteryState = createEntity(em);
    }

    @Test
    void createBatteryState() throws Exception {
        int databaseSizeBeforeCreate = batteryStateRepository.findAll().collectList().block().size();
        // Create the BatteryState
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeCreate + 1);
        BatteryState testBatteryState = batteryStateList.get(batteryStateList.size() - 1);
        assertThat(testBatteryState.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testBatteryState.getVol()).isEqualTo(DEFAULT_VOL);
        assertThat(testBatteryState.getCur()).isEqualTo(DEFAULT_CUR);
        assertThat(testBatteryState.getSoc()).isEqualTo(DEFAULT_SOC);
        assertThat(testBatteryState.getSoh()).isEqualTo(DEFAULT_SOH);
        assertThat(testBatteryState.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testBatteryState.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createBatteryStateWithExistingId() throws Exception {
        // Create the BatteryState with an existing ID
        batteryState.setId(1L);

        int databaseSizeBeforeCreate = batteryStateRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkSerialNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryStateRepository.findAll().collectList().block().size();
        // set the field null
        batteryState.setSerialNumber(null);

        // Create the BatteryState, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkVolIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryStateRepository.findAll().collectList().block().size();
        // set the field null
        batteryState.setVol(null);

        // Create the BatteryState, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCurIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryStateRepository.findAll().collectList().block().size();
        // set the field null
        batteryState.setCur(null);

        // Create the BatteryState, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSocIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryStateRepository.findAll().collectList().block().size();
        // set the field null
        batteryState.setSoc(null);

        // Create the BatteryState, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSohIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryStateRepository.findAll().collectList().block().size();
        // set the field null
        batteryState.setSoh(null);

        // Create the BatteryState, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryStateRepository.findAll().collectList().block().size();
        // set the field null
        batteryState.setState(null);

        // Create the BatteryState, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryStateRepository.findAll().collectList().block().size();
        // set the field null
        batteryState.setStatus(null);

        // Create the BatteryState, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllBatteryStatesAsStream() {
        // Initialize the database
        batteryStateRepository.save(batteryState).block();

        List<BatteryState> batteryStateList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(BatteryState.class)
            .getResponseBody()
            .filter(batteryState::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(batteryStateList).isNotNull();
        assertThat(batteryStateList).hasSize(1);
        BatteryState testBatteryState = batteryStateList.get(0);
        assertThat(testBatteryState.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testBatteryState.getVol()).isEqualTo(DEFAULT_VOL);
        assertThat(testBatteryState.getCur()).isEqualTo(DEFAULT_CUR);
        assertThat(testBatteryState.getSoc()).isEqualTo(DEFAULT_SOC);
        assertThat(testBatteryState.getSoh()).isEqualTo(DEFAULT_SOH);
        assertThat(testBatteryState.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testBatteryState.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void getAllBatteryStates() {
        // Initialize the database
        batteryStateRepository.save(batteryState).block();

        // Get all the batteryStateList
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
            .value(hasItem(batteryState.getId().intValue()))
            .jsonPath("$.[*].serialNumber")
            .value(hasItem(DEFAULT_SERIAL_NUMBER))
            .jsonPath("$.[*].vol")
            .value(hasItem(DEFAULT_VOL))
            .jsonPath("$.[*].cur")
            .value(hasItem(DEFAULT_CUR))
            .jsonPath("$.[*].soc")
            .value(hasItem(DEFAULT_SOC))
            .jsonPath("$.[*].soh")
            .value(hasItem(DEFAULT_SOH))
            .jsonPath("$.[*].state")
            .value(hasItem(DEFAULT_STATE))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS));
    }

    @Test
    void getBatteryState() {
        // Initialize the database
        batteryStateRepository.save(batteryState).block();

        // Get the batteryState
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, batteryState.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(batteryState.getId().intValue()))
            .jsonPath("$.serialNumber")
            .value(is(DEFAULT_SERIAL_NUMBER))
            .jsonPath("$.vol")
            .value(is(DEFAULT_VOL))
            .jsonPath("$.cur")
            .value(is(DEFAULT_CUR))
            .jsonPath("$.soc")
            .value(is(DEFAULT_SOC))
            .jsonPath("$.soh")
            .value(is(DEFAULT_SOH))
            .jsonPath("$.state")
            .value(is(DEFAULT_STATE))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS));
    }

    @Test
    void getNonExistingBatteryState() {
        // Get the batteryState
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewBatteryState() throws Exception {
        // Initialize the database
        batteryStateRepository.save(batteryState).block();

        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().collectList().block().size();

        // Update the batteryState
        BatteryState updatedBatteryState = batteryStateRepository.findById(batteryState.getId()).block();
        updatedBatteryState
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .vol(UPDATED_VOL)
            .cur(UPDATED_CUR)
            .soc(UPDATED_SOC)
            .soh(UPDATED_SOH)
            .state(UPDATED_STATE)
            .status(UPDATED_STATUS);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedBatteryState.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedBatteryState))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
        BatteryState testBatteryState = batteryStateList.get(batteryStateList.size() - 1);
        assertThat(testBatteryState.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testBatteryState.getVol()).isEqualTo(UPDATED_VOL);
        assertThat(testBatteryState.getCur()).isEqualTo(UPDATED_CUR);
        assertThat(testBatteryState.getSoc()).isEqualTo(UPDATED_SOC);
        assertThat(testBatteryState.getSoh()).isEqualTo(UPDATED_SOH);
        assertThat(testBatteryState.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testBatteryState.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingBatteryState() throws Exception {
        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().collectList().block().size();
        batteryState.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, batteryState.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBatteryState() throws Exception {
        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().collectList().block().size();
        batteryState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBatteryState() throws Exception {
        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().collectList().block().size();
        batteryState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBatteryStateWithPatch() throws Exception {
        // Initialize the database
        batteryStateRepository.save(batteryState).block();

        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().collectList().block().size();

        // Update the batteryState using partial update
        BatteryState partialUpdatedBatteryState = new BatteryState();
        partialUpdatedBatteryState.setId(batteryState.getId());

        partialUpdatedBatteryState
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .vol(UPDATED_VOL)
            .cur(UPDATED_CUR)
            .soc(UPDATED_SOC)
            .state(UPDATED_STATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBatteryState.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBatteryState))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
        BatteryState testBatteryState = batteryStateList.get(batteryStateList.size() - 1);
        assertThat(testBatteryState.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testBatteryState.getVol()).isEqualTo(UPDATED_VOL);
        assertThat(testBatteryState.getCur()).isEqualTo(UPDATED_CUR);
        assertThat(testBatteryState.getSoc()).isEqualTo(UPDATED_SOC);
        assertThat(testBatteryState.getSoh()).isEqualTo(DEFAULT_SOH);
        assertThat(testBatteryState.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testBatteryState.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void fullUpdateBatteryStateWithPatch() throws Exception {
        // Initialize the database
        batteryStateRepository.save(batteryState).block();

        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().collectList().block().size();

        // Update the batteryState using partial update
        BatteryState partialUpdatedBatteryState = new BatteryState();
        partialUpdatedBatteryState.setId(batteryState.getId());

        partialUpdatedBatteryState
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .vol(UPDATED_VOL)
            .cur(UPDATED_CUR)
            .soc(UPDATED_SOC)
            .soh(UPDATED_SOH)
            .state(UPDATED_STATE)
            .status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBatteryState.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBatteryState))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
        BatteryState testBatteryState = batteryStateList.get(batteryStateList.size() - 1);
        assertThat(testBatteryState.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testBatteryState.getVol()).isEqualTo(UPDATED_VOL);
        assertThat(testBatteryState.getCur()).isEqualTo(UPDATED_CUR);
        assertThat(testBatteryState.getSoc()).isEqualTo(UPDATED_SOC);
        assertThat(testBatteryState.getSoh()).isEqualTo(UPDATED_SOH);
        assertThat(testBatteryState.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testBatteryState.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingBatteryState() throws Exception {
        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().collectList().block().size();
        batteryState.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, batteryState.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBatteryState() throws Exception {
        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().collectList().block().size();
        batteryState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBatteryState() throws Exception {
        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().collectList().block().size();
        batteryState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(batteryState))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBatteryState() {
        // Initialize the database
        batteryStateRepository.save(batteryState).block();

        int databaseSizeBeforeDelete = batteryStateRepository.findAll().collectList().block().size();

        // Delete the batteryState
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, batteryState.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<BatteryState> batteryStateList = batteryStateRepository.findAll().collectList().block();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
