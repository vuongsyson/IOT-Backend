package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.VehicleState;
import com.sonpj.repository.EntityManager;
import com.sonpj.repository.VehicleStateRepository;
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
 * Integration tests for the {@link VehicleStateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class VehicleStateResourceIT {

    private static final Integer DEFAULT_SPEED = 1;
    private static final Integer UPDATED_SPEED = 2;

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;

    private static final Double DEFAULT_LON = 1D;
    private static final Double UPDATED_LON = 2D;

    private static final Integer DEFAULT_ERROR = 1;
    private static final Integer UPDATED_ERROR = 2;

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Double DEFAULT_ODO = 1D;
    private static final Double UPDATED_ODO = 2D;

    private static final Double DEFAULT_POWER = 1D;
    private static final Double UPDATED_POWER = 2D;

    private static final Double DEFAULT_THROTTLE = 1D;
    private static final Double UPDATED_THROTTLE = 2D;

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/vehicle-states";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VehicleStateRepository vehicleStateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private VehicleState vehicleState;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleState createEntity(EntityManager em) {
        VehicleState vehicleState = new VehicleState()
            .speed(DEFAULT_SPEED)
            .lat(DEFAULT_LAT)
            .lon(DEFAULT_LON)
            .error(DEFAULT_ERROR)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .status(DEFAULT_STATUS)
            .odo(DEFAULT_ODO)
            .power(DEFAULT_POWER)
            .throttle(DEFAULT_THROTTLE)
            .time(DEFAULT_TIME);
        return vehicleState;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleState createUpdatedEntity(EntityManager em) {
        VehicleState vehicleState = new VehicleState()
            .speed(UPDATED_SPEED)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .error(UPDATED_ERROR)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .status(UPDATED_STATUS)
            .odo(UPDATED_ODO)
            .power(UPDATED_POWER)
            .throttle(UPDATED_THROTTLE)
            .time(UPDATED_TIME);
        return vehicleState;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(VehicleState.class).block();
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
        vehicleState = createEntity(em);
    }

    @Test
    void createVehicleState() throws Exception {
        int databaseSizeBeforeCreate = vehicleStateRepository.findAll().collectList().block().size();
        // Create the VehicleState
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleState))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll().collectList().block();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeCreate + 1);
        VehicleState testVehicleState = vehicleStateList.get(vehicleStateList.size() - 1);
        assertThat(testVehicleState.getSpeed()).isEqualTo(DEFAULT_SPEED);
        assertThat(testVehicleState.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testVehicleState.getLon()).isEqualTo(DEFAULT_LON);
        assertThat(testVehicleState.getError()).isEqualTo(DEFAULT_ERROR);
        assertThat(testVehicleState.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testVehicleState.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testVehicleState.getOdo()).isEqualTo(DEFAULT_ODO);
        assertThat(testVehicleState.getPower()).isEqualTo(DEFAULT_POWER);
        assertThat(testVehicleState.getThrottle()).isEqualTo(DEFAULT_THROTTLE);
        assertThat(testVehicleState.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    void createVehicleStateWithExistingId() throws Exception {
        // Create the VehicleState with an existing ID
        vehicleState.setId(1L);

        int databaseSizeBeforeCreate = vehicleStateRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll().collectList().block();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllVehicleStatesAsStream() {
        // Initialize the database
        vehicleStateRepository.save(vehicleState).block();

        List<VehicleState> vehicleStateList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(VehicleState.class)
            .getResponseBody()
            .filter(vehicleState::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(vehicleStateList).isNotNull();
        assertThat(vehicleStateList).hasSize(1);
        VehicleState testVehicleState = vehicleStateList.get(0);
        assertThat(testVehicleState.getSpeed()).isEqualTo(DEFAULT_SPEED);
        assertThat(testVehicleState.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testVehicleState.getLon()).isEqualTo(DEFAULT_LON);
        assertThat(testVehicleState.getError()).isEqualTo(DEFAULT_ERROR);
        assertThat(testVehicleState.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testVehicleState.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testVehicleState.getOdo()).isEqualTo(DEFAULT_ODO);
        assertThat(testVehicleState.getPower()).isEqualTo(DEFAULT_POWER);
        assertThat(testVehicleState.getThrottle()).isEqualTo(DEFAULT_THROTTLE);
        assertThat(testVehicleState.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    void getAllVehicleStates() {
        // Initialize the database
        vehicleStateRepository.save(vehicleState).block();

        // Get all the vehicleStateList
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
            .value(hasItem(vehicleState.getId().intValue()))
            .jsonPath("$.[*].speed")
            .value(hasItem(DEFAULT_SPEED))
            .jsonPath("$.[*].lat")
            .value(hasItem(DEFAULT_LAT.doubleValue()))
            .jsonPath("$.[*].lon")
            .value(hasItem(DEFAULT_LON.doubleValue()))
            .jsonPath("$.[*].error")
            .value(hasItem(DEFAULT_ERROR))
            .jsonPath("$.[*].serialNumber")
            .value(hasItem(DEFAULT_SERIAL_NUMBER))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS))
            .jsonPath("$.[*].odo")
            .value(hasItem(DEFAULT_ODO.doubleValue()))
            .jsonPath("$.[*].power")
            .value(hasItem(DEFAULT_POWER.doubleValue()))
            .jsonPath("$.[*].throttle")
            .value(hasItem(DEFAULT_THROTTLE.doubleValue()))
            .jsonPath("$.[*].time")
            .value(hasItem(DEFAULT_TIME.toString()));
    }

    @Test
    void getVehicleState() {
        // Initialize the database
        vehicleStateRepository.save(vehicleState).block();

        // Get the vehicleState
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, vehicleState.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(vehicleState.getId().intValue()))
            .jsonPath("$.speed")
            .value(is(DEFAULT_SPEED))
            .jsonPath("$.lat")
            .value(is(DEFAULT_LAT.doubleValue()))
            .jsonPath("$.lon")
            .value(is(DEFAULT_LON.doubleValue()))
            .jsonPath("$.error")
            .value(is(DEFAULT_ERROR))
            .jsonPath("$.serialNumber")
            .value(is(DEFAULT_SERIAL_NUMBER))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS))
            .jsonPath("$.odo")
            .value(is(DEFAULT_ODO.doubleValue()))
            .jsonPath("$.power")
            .value(is(DEFAULT_POWER.doubleValue()))
            .jsonPath("$.throttle")
            .value(is(DEFAULT_THROTTLE.doubleValue()))
            .jsonPath("$.time")
            .value(is(DEFAULT_TIME.toString()));
    }

    @Test
    void getNonExistingVehicleState() {
        // Get the vehicleState
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewVehicleState() throws Exception {
        // Initialize the database
        vehicleStateRepository.save(vehicleState).block();

        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().collectList().block().size();

        // Update the vehicleState
        VehicleState updatedVehicleState = vehicleStateRepository.findById(vehicleState.getId()).block();
        updatedVehicleState
            .speed(UPDATED_SPEED)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .error(UPDATED_ERROR)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .status(UPDATED_STATUS)
            .odo(UPDATED_ODO)
            .power(UPDATED_POWER)
            .throttle(UPDATED_THROTTLE)
            .time(UPDATED_TIME);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedVehicleState.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedVehicleState))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll().collectList().block();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
        VehicleState testVehicleState = vehicleStateList.get(vehicleStateList.size() - 1);
        assertThat(testVehicleState.getSpeed()).isEqualTo(UPDATED_SPEED);
        assertThat(testVehicleState.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testVehicleState.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testVehicleState.getError()).isEqualTo(UPDATED_ERROR);
        assertThat(testVehicleState.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testVehicleState.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVehicleState.getOdo()).isEqualTo(UPDATED_ODO);
        assertThat(testVehicleState.getPower()).isEqualTo(UPDATED_POWER);
        assertThat(testVehicleState.getThrottle()).isEqualTo(UPDATED_THROTTLE);
        assertThat(testVehicleState.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    void putNonExistingVehicleState() throws Exception {
        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().collectList().block().size();
        vehicleState.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, vehicleState.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll().collectList().block();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchVehicleState() throws Exception {
        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().collectList().block().size();
        vehicleState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll().collectList().block();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamVehicleState() throws Exception {
        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().collectList().block().size();
        vehicleState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleState))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll().collectList().block();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateVehicleStateWithPatch() throws Exception {
        // Initialize the database
        vehicleStateRepository.save(vehicleState).block();

        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().collectList().block().size();

        // Update the vehicleState using partial update
        VehicleState partialUpdatedVehicleState = new VehicleState();
        partialUpdatedVehicleState.setId(vehicleState.getId());

        partialUpdatedVehicleState
            .speed(UPDATED_SPEED)
            .lat(UPDATED_LAT)
            .status(UPDATED_STATUS)
            .throttle(UPDATED_THROTTLE)
            .time(UPDATED_TIME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVehicleState.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicleState))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll().collectList().block();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
        VehicleState testVehicleState = vehicleStateList.get(vehicleStateList.size() - 1);
        assertThat(testVehicleState.getSpeed()).isEqualTo(UPDATED_SPEED);
        assertThat(testVehicleState.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testVehicleState.getLon()).isEqualTo(DEFAULT_LON);
        assertThat(testVehicleState.getError()).isEqualTo(DEFAULT_ERROR);
        assertThat(testVehicleState.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testVehicleState.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVehicleState.getOdo()).isEqualTo(DEFAULT_ODO);
        assertThat(testVehicleState.getPower()).isEqualTo(DEFAULT_POWER);
        assertThat(testVehicleState.getThrottle()).isEqualTo(UPDATED_THROTTLE);
        assertThat(testVehicleState.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    void fullUpdateVehicleStateWithPatch() throws Exception {
        // Initialize the database
        vehicleStateRepository.save(vehicleState).block();

        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().collectList().block().size();

        // Update the vehicleState using partial update
        VehicleState partialUpdatedVehicleState = new VehicleState();
        partialUpdatedVehicleState.setId(vehicleState.getId());

        partialUpdatedVehicleState
            .speed(UPDATED_SPEED)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .error(UPDATED_ERROR)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .status(UPDATED_STATUS)
            .odo(UPDATED_ODO)
            .power(UPDATED_POWER)
            .throttle(UPDATED_THROTTLE)
            .time(UPDATED_TIME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVehicleState.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicleState))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll().collectList().block();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
        VehicleState testVehicleState = vehicleStateList.get(vehicleStateList.size() - 1);
        assertThat(testVehicleState.getSpeed()).isEqualTo(UPDATED_SPEED);
        assertThat(testVehicleState.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testVehicleState.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testVehicleState.getError()).isEqualTo(UPDATED_ERROR);
        assertThat(testVehicleState.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testVehicleState.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVehicleState.getOdo()).isEqualTo(UPDATED_ODO);
        assertThat(testVehicleState.getPower()).isEqualTo(UPDATED_POWER);
        assertThat(testVehicleState.getThrottle()).isEqualTo(UPDATED_THROTTLE);
        assertThat(testVehicleState.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    void patchNonExistingVehicleState() throws Exception {
        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().collectList().block().size();
        vehicleState.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, vehicleState.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll().collectList().block();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchVehicleState() throws Exception {
        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().collectList().block().size();
        vehicleState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleState))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll().collectList().block();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamVehicleState() throws Exception {
        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().collectList().block().size();
        vehicleState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleState))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll().collectList().block();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteVehicleState() {
        // Initialize the database
        vehicleStateRepository.save(vehicleState).block();

        int databaseSizeBeforeDelete = vehicleStateRepository.findAll().collectList().block().size();

        // Delete the vehicleState
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, vehicleState.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll().collectList().block();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
