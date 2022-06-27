package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.Vehicle;
import com.sonpj.repository.EntityManager;
import com.sonpj.repository.VehicleRepository;
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
 * Integration tests for the {@link VehicleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class VehicleResourceIT {

    private static final Integer DEFAULT_CLEARANCE = 1;
    private static final Integer UPDATED_CLEARANCE = 2;

    private static final Integer DEFAULT_MAX_POWER = 1;
    private static final Integer UPDATED_MAX_POWER = 2;

    private static final Integer DEFAULT_MAX_SPEED = 1;
    private static final Integer UPDATED_MAX_SPEED = 2;

    private static final Integer DEFAULT_MAX_LOAD = 1;
    private static final Integer UPDATED_MAX_LOAD = 2;

    private static final Integer DEFAULT_WEIGHT_TOTAL = 1;
    private static final Integer UPDATED_WEIGHT_TOTAL = 2;

    private static final Integer DEFAULT_MAX_DISTANCE = 1;
    private static final Integer UPDATED_MAX_DISTANCE = 2;

    private static final Integer DEFAULT_WHEEL_BASE = 1;
    private static final Integer UPDATED_WHEEL_BASE = 2;

    private static final Integer DEFAULT_HW_VERSION = 1;
    private static final Integer UPDATED_HW_VERSION = 2;

    private static final Integer DEFAULT_SW_VERSION = 1;
    private static final Integer UPDATED_SW_VERSION = 2;

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MANUFACTURE_DATE = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURE_DATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_LOT_NUMBER = 1;
    private static final Integer UPDATED_LOT_NUMBER = 2;

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_VEHICLE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_USED = false;
    private static final Boolean UPDATED_USED = true;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/vehicles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Vehicle vehicle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicle createEntity(EntityManager em) {
        Vehicle vehicle = new Vehicle()
            .clearance(DEFAULT_CLEARANCE)
            .maxPower(DEFAULT_MAX_POWER)
            .maxSpeed(DEFAULT_MAX_SPEED)
            .maxLoad(DEFAULT_MAX_LOAD)
            .weightTotal(DEFAULT_WEIGHT_TOTAL)
            .maxDistance(DEFAULT_MAX_DISTANCE)
            .wheelBase(DEFAULT_WHEEL_BASE)
            .hwVersion(DEFAULT_HW_VERSION)
            .swVersion(DEFAULT_SW_VERSION)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .manufactureDate(DEFAULT_MANUFACTURE_DATE)
            .lotNumber(DEFAULT_LOT_NUMBER)
            .color(DEFAULT_COLOR)
            .vehicleType(DEFAULT_VEHICLE_TYPE)
            .used(DEFAULT_USED)
            .userId(DEFAULT_USER_ID);
        return vehicle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicle createUpdatedEntity(EntityManager em) {
        Vehicle vehicle = new Vehicle()
            .clearance(UPDATED_CLEARANCE)
            .maxPower(UPDATED_MAX_POWER)
            .maxSpeed(UPDATED_MAX_SPEED)
            .maxLoad(UPDATED_MAX_LOAD)
            .weightTotal(UPDATED_WEIGHT_TOTAL)
            .maxDistance(UPDATED_MAX_DISTANCE)
            .wheelBase(UPDATED_WHEEL_BASE)
            .hwVersion(UPDATED_HW_VERSION)
            .swVersion(UPDATED_SW_VERSION)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .manufactureDate(UPDATED_MANUFACTURE_DATE)
            .lotNumber(UPDATED_LOT_NUMBER)
            .color(UPDATED_COLOR)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .used(UPDATED_USED)
            .userId(UPDATED_USER_ID);
        return vehicle;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Vehicle.class).block();
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
        vehicle = createEntity(em);
    }

    @Test
    void createVehicle() throws Exception {
        int databaseSizeBeforeCreate = vehicleRepository.findAll().collectList().block().size();
        // Create the Vehicle
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicle))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll().collectList().block();
        assertThat(vehicleList).hasSize(databaseSizeBeforeCreate + 1);
        Vehicle testVehicle = vehicleList.get(vehicleList.size() - 1);
        assertThat(testVehicle.getClearance()).isEqualTo(DEFAULT_CLEARANCE);
        assertThat(testVehicle.getMaxPower()).isEqualTo(DEFAULT_MAX_POWER);
        assertThat(testVehicle.getMaxSpeed()).isEqualTo(DEFAULT_MAX_SPEED);
        assertThat(testVehicle.getMaxLoad()).isEqualTo(DEFAULT_MAX_LOAD);
        assertThat(testVehicle.getWeightTotal()).isEqualTo(DEFAULT_WEIGHT_TOTAL);
        assertThat(testVehicle.getMaxDistance()).isEqualTo(DEFAULT_MAX_DISTANCE);
        assertThat(testVehicle.getWheelBase()).isEqualTo(DEFAULT_WHEEL_BASE);
        assertThat(testVehicle.getHwVersion()).isEqualTo(DEFAULT_HW_VERSION);
        assertThat(testVehicle.getSwVersion()).isEqualTo(DEFAULT_SW_VERSION);
        assertThat(testVehicle.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testVehicle.getManufactureDate()).isEqualTo(DEFAULT_MANUFACTURE_DATE);
        assertThat(testVehicle.getLotNumber()).isEqualTo(DEFAULT_LOT_NUMBER);
        assertThat(testVehicle.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testVehicle.getVehicleType()).isEqualTo(DEFAULT_VEHICLE_TYPE);
        assertThat(testVehicle.getUsed()).isEqualTo(DEFAULT_USED);
        assertThat(testVehicle.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    void createVehicleWithExistingId() throws Exception {
        // Create the Vehicle with an existing ID
        vehicle.setId(1L);

        int databaseSizeBeforeCreate = vehicleRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicle))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll().collectList().block();
        assertThat(vehicleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllVehiclesAsStream() {
        // Initialize the database
        vehicleRepository.save(vehicle).block();

        List<Vehicle> vehicleList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Vehicle.class)
            .getResponseBody()
            .filter(vehicle::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(vehicleList).isNotNull();
        assertThat(vehicleList).hasSize(1);
        Vehicle testVehicle = vehicleList.get(0);
        assertThat(testVehicle.getClearance()).isEqualTo(DEFAULT_CLEARANCE);
        assertThat(testVehicle.getMaxPower()).isEqualTo(DEFAULT_MAX_POWER);
        assertThat(testVehicle.getMaxSpeed()).isEqualTo(DEFAULT_MAX_SPEED);
        assertThat(testVehicle.getMaxLoad()).isEqualTo(DEFAULT_MAX_LOAD);
        assertThat(testVehicle.getWeightTotal()).isEqualTo(DEFAULT_WEIGHT_TOTAL);
        assertThat(testVehicle.getMaxDistance()).isEqualTo(DEFAULT_MAX_DISTANCE);
        assertThat(testVehicle.getWheelBase()).isEqualTo(DEFAULT_WHEEL_BASE);
        assertThat(testVehicle.getHwVersion()).isEqualTo(DEFAULT_HW_VERSION);
        assertThat(testVehicle.getSwVersion()).isEqualTo(DEFAULT_SW_VERSION);
        assertThat(testVehicle.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testVehicle.getManufactureDate()).isEqualTo(DEFAULT_MANUFACTURE_DATE);
        assertThat(testVehicle.getLotNumber()).isEqualTo(DEFAULT_LOT_NUMBER);
        assertThat(testVehicle.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testVehicle.getVehicleType()).isEqualTo(DEFAULT_VEHICLE_TYPE);
        assertThat(testVehicle.getUsed()).isEqualTo(DEFAULT_USED);
        assertThat(testVehicle.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    void getAllVehicles() {
        // Initialize the database
        vehicleRepository.save(vehicle).block();

        // Get all the vehicleList
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
            .value(hasItem(vehicle.getId().intValue()))
            .jsonPath("$.[*].clearance")
            .value(hasItem(DEFAULT_CLEARANCE))
            .jsonPath("$.[*].maxPower")
            .value(hasItem(DEFAULT_MAX_POWER))
            .jsonPath("$.[*].maxSpeed")
            .value(hasItem(DEFAULT_MAX_SPEED))
            .jsonPath("$.[*].maxLoad")
            .value(hasItem(DEFAULT_MAX_LOAD))
            .jsonPath("$.[*].weightTotal")
            .value(hasItem(DEFAULT_WEIGHT_TOTAL))
            .jsonPath("$.[*].maxDistance")
            .value(hasItem(DEFAULT_MAX_DISTANCE))
            .jsonPath("$.[*].wheelBase")
            .value(hasItem(DEFAULT_WHEEL_BASE))
            .jsonPath("$.[*].hwVersion")
            .value(hasItem(DEFAULT_HW_VERSION))
            .jsonPath("$.[*].swVersion")
            .value(hasItem(DEFAULT_SW_VERSION))
            .jsonPath("$.[*].serialNumber")
            .value(hasItem(DEFAULT_SERIAL_NUMBER))
            .jsonPath("$.[*].manufactureDate")
            .value(hasItem(DEFAULT_MANUFACTURE_DATE))
            .jsonPath("$.[*].lotNumber")
            .value(hasItem(DEFAULT_LOT_NUMBER))
            .jsonPath("$.[*].color")
            .value(hasItem(DEFAULT_COLOR))
            .jsonPath("$.[*].vehicleType")
            .value(hasItem(DEFAULT_VEHICLE_TYPE))
            .jsonPath("$.[*].used")
            .value(hasItem(DEFAULT_USED.booleanValue()))
            .jsonPath("$.[*].userId")
            .value(hasItem(DEFAULT_USER_ID.intValue()));
    }

    @Test
    void getVehicle() {
        // Initialize the database
        vehicleRepository.save(vehicle).block();

        // Get the vehicle
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, vehicle.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(vehicle.getId().intValue()))
            .jsonPath("$.clearance")
            .value(is(DEFAULT_CLEARANCE))
            .jsonPath("$.maxPower")
            .value(is(DEFAULT_MAX_POWER))
            .jsonPath("$.maxSpeed")
            .value(is(DEFAULT_MAX_SPEED))
            .jsonPath("$.maxLoad")
            .value(is(DEFAULT_MAX_LOAD))
            .jsonPath("$.weightTotal")
            .value(is(DEFAULT_WEIGHT_TOTAL))
            .jsonPath("$.maxDistance")
            .value(is(DEFAULT_MAX_DISTANCE))
            .jsonPath("$.wheelBase")
            .value(is(DEFAULT_WHEEL_BASE))
            .jsonPath("$.hwVersion")
            .value(is(DEFAULT_HW_VERSION))
            .jsonPath("$.swVersion")
            .value(is(DEFAULT_SW_VERSION))
            .jsonPath("$.serialNumber")
            .value(is(DEFAULT_SERIAL_NUMBER))
            .jsonPath("$.manufactureDate")
            .value(is(DEFAULT_MANUFACTURE_DATE))
            .jsonPath("$.lotNumber")
            .value(is(DEFAULT_LOT_NUMBER))
            .jsonPath("$.color")
            .value(is(DEFAULT_COLOR))
            .jsonPath("$.vehicleType")
            .value(is(DEFAULT_VEHICLE_TYPE))
            .jsonPath("$.used")
            .value(is(DEFAULT_USED.booleanValue()))
            .jsonPath("$.userId")
            .value(is(DEFAULT_USER_ID.intValue()));
    }

    @Test
    void getNonExistingVehicle() {
        // Get the vehicle
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.save(vehicle).block();

        int databaseSizeBeforeUpdate = vehicleRepository.findAll().collectList().block().size();

        // Update the vehicle
        Vehicle updatedVehicle = vehicleRepository.findById(vehicle.getId()).block();
        updatedVehicle
            .clearance(UPDATED_CLEARANCE)
            .maxPower(UPDATED_MAX_POWER)
            .maxSpeed(UPDATED_MAX_SPEED)
            .maxLoad(UPDATED_MAX_LOAD)
            .weightTotal(UPDATED_WEIGHT_TOTAL)
            .maxDistance(UPDATED_MAX_DISTANCE)
            .wheelBase(UPDATED_WHEEL_BASE)
            .hwVersion(UPDATED_HW_VERSION)
            .swVersion(UPDATED_SW_VERSION)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .manufactureDate(UPDATED_MANUFACTURE_DATE)
            .lotNumber(UPDATED_LOT_NUMBER)
            .color(UPDATED_COLOR)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .used(UPDATED_USED)
            .userId(UPDATED_USER_ID);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedVehicle.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedVehicle))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll().collectList().block();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
        Vehicle testVehicle = vehicleList.get(vehicleList.size() - 1);
        assertThat(testVehicle.getClearance()).isEqualTo(UPDATED_CLEARANCE);
        assertThat(testVehicle.getMaxPower()).isEqualTo(UPDATED_MAX_POWER);
        assertThat(testVehicle.getMaxSpeed()).isEqualTo(UPDATED_MAX_SPEED);
        assertThat(testVehicle.getMaxLoad()).isEqualTo(UPDATED_MAX_LOAD);
        assertThat(testVehicle.getWeightTotal()).isEqualTo(UPDATED_WEIGHT_TOTAL);
        assertThat(testVehicle.getMaxDistance()).isEqualTo(UPDATED_MAX_DISTANCE);
        assertThat(testVehicle.getWheelBase()).isEqualTo(UPDATED_WHEEL_BASE);
        assertThat(testVehicle.getHwVersion()).isEqualTo(UPDATED_HW_VERSION);
        assertThat(testVehicle.getSwVersion()).isEqualTo(UPDATED_SW_VERSION);
        assertThat(testVehicle.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testVehicle.getManufactureDate()).isEqualTo(UPDATED_MANUFACTURE_DATE);
        assertThat(testVehicle.getLotNumber()).isEqualTo(UPDATED_LOT_NUMBER);
        assertThat(testVehicle.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testVehicle.getVehicleType()).isEqualTo(UPDATED_VEHICLE_TYPE);
        assertThat(testVehicle.getUsed()).isEqualTo(UPDATED_USED);
        assertThat(testVehicle.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    void putNonExistingVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().collectList().block().size();
        vehicle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, vehicle.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicle))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll().collectList().block();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().collectList().block().size();
        vehicle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicle))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll().collectList().block();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().collectList().block().size();
        vehicle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicle))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll().collectList().block();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateVehicleWithPatch() throws Exception {
        // Initialize the database
        vehicleRepository.save(vehicle).block();

        int databaseSizeBeforeUpdate = vehicleRepository.findAll().collectList().block().size();

        // Update the vehicle using partial update
        Vehicle partialUpdatedVehicle = new Vehicle();
        partialUpdatedVehicle.setId(vehicle.getId());

        partialUpdatedVehicle
            .maxLoad(UPDATED_MAX_LOAD)
            .weightTotal(UPDATED_WEIGHT_TOTAL)
            .wheelBase(UPDATED_WHEEL_BASE)
            .hwVersion(UPDATED_HW_VERSION)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .lotNumber(UPDATED_LOT_NUMBER)
            .color(UPDATED_COLOR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVehicle.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicle))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll().collectList().block();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
        Vehicle testVehicle = vehicleList.get(vehicleList.size() - 1);
        assertThat(testVehicle.getClearance()).isEqualTo(DEFAULT_CLEARANCE);
        assertThat(testVehicle.getMaxPower()).isEqualTo(DEFAULT_MAX_POWER);
        assertThat(testVehicle.getMaxSpeed()).isEqualTo(DEFAULT_MAX_SPEED);
        assertThat(testVehicle.getMaxLoad()).isEqualTo(UPDATED_MAX_LOAD);
        assertThat(testVehicle.getWeightTotal()).isEqualTo(UPDATED_WEIGHT_TOTAL);
        assertThat(testVehicle.getMaxDistance()).isEqualTo(DEFAULT_MAX_DISTANCE);
        assertThat(testVehicle.getWheelBase()).isEqualTo(UPDATED_WHEEL_BASE);
        assertThat(testVehicle.getHwVersion()).isEqualTo(UPDATED_HW_VERSION);
        assertThat(testVehicle.getSwVersion()).isEqualTo(DEFAULT_SW_VERSION);
        assertThat(testVehicle.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testVehicle.getManufactureDate()).isEqualTo(DEFAULT_MANUFACTURE_DATE);
        assertThat(testVehicle.getLotNumber()).isEqualTo(UPDATED_LOT_NUMBER);
        assertThat(testVehicle.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testVehicle.getVehicleType()).isEqualTo(DEFAULT_VEHICLE_TYPE);
        assertThat(testVehicle.getUsed()).isEqualTo(DEFAULT_USED);
        assertThat(testVehicle.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    void fullUpdateVehicleWithPatch() throws Exception {
        // Initialize the database
        vehicleRepository.save(vehicle).block();

        int databaseSizeBeforeUpdate = vehicleRepository.findAll().collectList().block().size();

        // Update the vehicle using partial update
        Vehicle partialUpdatedVehicle = new Vehicle();
        partialUpdatedVehicle.setId(vehicle.getId());

        partialUpdatedVehicle
            .clearance(UPDATED_CLEARANCE)
            .maxPower(UPDATED_MAX_POWER)
            .maxSpeed(UPDATED_MAX_SPEED)
            .maxLoad(UPDATED_MAX_LOAD)
            .weightTotal(UPDATED_WEIGHT_TOTAL)
            .maxDistance(UPDATED_MAX_DISTANCE)
            .wheelBase(UPDATED_WHEEL_BASE)
            .hwVersion(UPDATED_HW_VERSION)
            .swVersion(UPDATED_SW_VERSION)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .manufactureDate(UPDATED_MANUFACTURE_DATE)
            .lotNumber(UPDATED_LOT_NUMBER)
            .color(UPDATED_COLOR)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .used(UPDATED_USED)
            .userId(UPDATED_USER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVehicle.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicle))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll().collectList().block();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
        Vehicle testVehicle = vehicleList.get(vehicleList.size() - 1);
        assertThat(testVehicle.getClearance()).isEqualTo(UPDATED_CLEARANCE);
        assertThat(testVehicle.getMaxPower()).isEqualTo(UPDATED_MAX_POWER);
        assertThat(testVehicle.getMaxSpeed()).isEqualTo(UPDATED_MAX_SPEED);
        assertThat(testVehicle.getMaxLoad()).isEqualTo(UPDATED_MAX_LOAD);
        assertThat(testVehicle.getWeightTotal()).isEqualTo(UPDATED_WEIGHT_TOTAL);
        assertThat(testVehicle.getMaxDistance()).isEqualTo(UPDATED_MAX_DISTANCE);
        assertThat(testVehicle.getWheelBase()).isEqualTo(UPDATED_WHEEL_BASE);
        assertThat(testVehicle.getHwVersion()).isEqualTo(UPDATED_HW_VERSION);
        assertThat(testVehicle.getSwVersion()).isEqualTo(UPDATED_SW_VERSION);
        assertThat(testVehicle.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testVehicle.getManufactureDate()).isEqualTo(UPDATED_MANUFACTURE_DATE);
        assertThat(testVehicle.getLotNumber()).isEqualTo(UPDATED_LOT_NUMBER);
        assertThat(testVehicle.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testVehicle.getVehicleType()).isEqualTo(UPDATED_VEHICLE_TYPE);
        assertThat(testVehicle.getUsed()).isEqualTo(UPDATED_USED);
        assertThat(testVehicle.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    void patchNonExistingVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().collectList().block().size();
        vehicle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, vehicle.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicle))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll().collectList().block();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().collectList().block().size();
        vehicle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicle))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll().collectList().block();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().collectList().block().size();
        vehicle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicle))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll().collectList().block();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteVehicle() {
        // Initialize the database
        vehicleRepository.save(vehicle).block();

        int databaseSizeBeforeDelete = vehicleRepository.findAll().collectList().block().size();

        // Delete the vehicle
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, vehicle.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Vehicle> vehicleList = vehicleRepository.findAll().collectList().block();
        assertThat(vehicleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
