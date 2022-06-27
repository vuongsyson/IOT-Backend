package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.Vehicle;
import com.sonpj.repository.VehicleRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VehicleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restVehicleMockMvc;

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

    @BeforeEach
    public void initTest() {
        vehicle = createEntity(em);
    }

    @Test
    @Transactional
    void createVehicle() throws Exception {
        int databaseSizeBeforeCreate = vehicleRepository.findAll().size();
        // Create the Vehicle
        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicle)))
            .andExpect(status().isCreated());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
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
    @Transactional
    void createVehicleWithExistingId() throws Exception {
        // Create the Vehicle with an existing ID
        vehicle.setId(1L);

        int databaseSizeBeforeCreate = vehicleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicle)))
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVehicles() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().intValue())))
            .andExpect(jsonPath("$.[*].clearance").value(hasItem(DEFAULT_CLEARANCE)))
            .andExpect(jsonPath("$.[*].maxPower").value(hasItem(DEFAULT_MAX_POWER)))
            .andExpect(jsonPath("$.[*].maxSpeed").value(hasItem(DEFAULT_MAX_SPEED)))
            .andExpect(jsonPath("$.[*].maxLoad").value(hasItem(DEFAULT_MAX_LOAD)))
            .andExpect(jsonPath("$.[*].weightTotal").value(hasItem(DEFAULT_WEIGHT_TOTAL)))
            .andExpect(jsonPath("$.[*].maxDistance").value(hasItem(DEFAULT_MAX_DISTANCE)))
            .andExpect(jsonPath("$.[*].wheelBase").value(hasItem(DEFAULT_WHEEL_BASE)))
            .andExpect(jsonPath("$.[*].hwVersion").value(hasItem(DEFAULT_HW_VERSION)))
            .andExpect(jsonPath("$.[*].swVersion").value(hasItem(DEFAULT_SW_VERSION)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].manufactureDate").value(hasItem(DEFAULT_MANUFACTURE_DATE)))
            .andExpect(jsonPath("$.[*].lotNumber").value(hasItem(DEFAULT_LOT_NUMBER)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].vehicleType").value(hasItem(DEFAULT_VEHICLE_TYPE)))
            .andExpect(jsonPath("$.[*].used").value(hasItem(DEFAULT_USED.booleanValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    void getVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get the vehicle
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicle.getId().intValue()))
            .andExpect(jsonPath("$.clearance").value(DEFAULT_CLEARANCE))
            .andExpect(jsonPath("$.maxPower").value(DEFAULT_MAX_POWER))
            .andExpect(jsonPath("$.maxSpeed").value(DEFAULT_MAX_SPEED))
            .andExpect(jsonPath("$.maxLoad").value(DEFAULT_MAX_LOAD))
            .andExpect(jsonPath("$.weightTotal").value(DEFAULT_WEIGHT_TOTAL))
            .andExpect(jsonPath("$.maxDistance").value(DEFAULT_MAX_DISTANCE))
            .andExpect(jsonPath("$.wheelBase").value(DEFAULT_WHEEL_BASE))
            .andExpect(jsonPath("$.hwVersion").value(DEFAULT_HW_VERSION))
            .andExpect(jsonPath("$.swVersion").value(DEFAULT_SW_VERSION))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.manufactureDate").value(DEFAULT_MANUFACTURE_DATE))
            .andExpect(jsonPath("$.lotNumber").value(DEFAULT_LOT_NUMBER))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.vehicleType").value(DEFAULT_VEHICLE_TYPE))
            .andExpect(jsonPath("$.used").value(DEFAULT_USED.booleanValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingVehicle() throws Exception {
        // Get the vehicle
        restVehicleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();

        // Update the vehicle
        Vehicle updatedVehicle = vehicleRepository.findById(vehicle.getId()).get();
        // Disconnect from session so that the updates on updatedVehicle are not directly saved in db
        em.detach(updatedVehicle);
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

        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVehicle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
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
    @Transactional
    void putNonExistingVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();
        vehicle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();
        vehicle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();
        vehicle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleWithPatch() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();

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

        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
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
    @Transactional
    void fullUpdateVehicleWithPatch() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();

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

        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
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
    @Transactional
    void patchNonExistingVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();
        vehicle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();
        vehicle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicle() throws Exception {
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();
        vehicle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vehicle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicle in the database
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        int databaseSizeBeforeDelete = vehicleRepository.findAll().size();

        // Delete the vehicle
        restVehicleMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        assertThat(vehicleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
