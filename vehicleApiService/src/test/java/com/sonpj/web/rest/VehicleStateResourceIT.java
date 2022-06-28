package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.VehicleState;
import com.sonpj.repository.VehicleStateRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link VehicleStateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restVehicleStateMockMvc;

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

    @BeforeEach
    public void initTest() {
        vehicleState = createEntity(em);
    }

    @Test
    @Transactional
    void createVehicleState() throws Exception {
        int databaseSizeBeforeCreate = vehicleStateRepository.findAll().size();
        // Create the VehicleState
        restVehicleStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicleState)))
            .andExpect(status().isCreated());

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll();
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
    @Transactional
    void createVehicleStateWithExistingId() throws Exception {
        // Create the VehicleState with an existing ID
        vehicleState.setId(1L);

        int databaseSizeBeforeCreate = vehicleStateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicleState)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVehicleStates() throws Exception {
        // Initialize the database
        vehicleStateRepository.saveAndFlush(vehicleState);

        // Get all the vehicleStateList
        restVehicleStateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleState.getId().intValue())))
            .andExpect(jsonPath("$.[*].speed").value(hasItem(DEFAULT_SPEED)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())))
            .andExpect(jsonPath("$.[*].error").value(hasItem(DEFAULT_ERROR)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].odo").value(hasItem(DEFAULT_ODO.doubleValue())))
            .andExpect(jsonPath("$.[*].power").value(hasItem(DEFAULT_POWER.doubleValue())))
            .andExpect(jsonPath("$.[*].throttle").value(hasItem(DEFAULT_THROTTLE.doubleValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())));
    }

    @Test
    @Transactional
    void getVehicleState() throws Exception {
        // Initialize the database
        vehicleStateRepository.saveAndFlush(vehicleState);

        // Get the vehicleState
        restVehicleStateMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleState.getId().intValue()))
            .andExpect(jsonPath("$.speed").value(DEFAULT_SPEED))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lon").value(DEFAULT_LON.doubleValue()))
            .andExpect(jsonPath("$.error").value(DEFAULT_ERROR))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.odo").value(DEFAULT_ODO.doubleValue()))
            .andExpect(jsonPath("$.power").value(DEFAULT_POWER.doubleValue()))
            .andExpect(jsonPath("$.throttle").value(DEFAULT_THROTTLE.doubleValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVehicleState() throws Exception {
        // Get the vehicleState
        restVehicleStateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVehicleState() throws Exception {
        // Initialize the database
        vehicleStateRepository.saveAndFlush(vehicleState);

        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().size();

        // Update the vehicleState
        VehicleState updatedVehicleState = vehicleStateRepository.findById(vehicleState.getId()).get();
        // Disconnect from session so that the updates on updatedVehicleState are not directly saved in db
        em.detach(updatedVehicleState);
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

        restVehicleStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVehicleState.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVehicleState))
            )
            .andExpect(status().isOk());

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll();
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
    @Transactional
    void putNonExistingVehicleState() throws Exception {
        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().size();
        vehicleState.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleState.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleState))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleState() throws Exception {
        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().size();
        vehicleState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleState))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleState() throws Exception {
        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().size();
        vehicleState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleStateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicleState)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleStateWithPatch() throws Exception {
        // Initialize the database
        vehicleStateRepository.saveAndFlush(vehicleState);

        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().size();

        // Update the vehicleState using partial update
        VehicleState partialUpdatedVehicleState = new VehicleState();
        partialUpdatedVehicleState.setId(vehicleState.getId());

        partialUpdatedVehicleState
            .speed(UPDATED_SPEED)
            .lat(UPDATED_LAT)
            .status(UPDATED_STATUS)
            .throttle(UPDATED_THROTTLE)
            .time(UPDATED_TIME);

        restVehicleStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleState.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicleState))
            )
            .andExpect(status().isOk());

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll();
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
    @Transactional
    void fullUpdateVehicleStateWithPatch() throws Exception {
        // Initialize the database
        vehicleStateRepository.saveAndFlush(vehicleState);

        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().size();

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

        restVehicleStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleState.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicleState))
            )
            .andExpect(status().isOk());

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll();
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
    @Transactional
    void patchNonExistingVehicleState() throws Exception {
        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().size();
        vehicleState.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleState.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicleState))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleState() throws Exception {
        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().size();
        vehicleState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicleState))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleState() throws Exception {
        int databaseSizeBeforeUpdate = vehicleStateRepository.findAll().size();
        vehicleState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleStateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vehicleState))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleState in the database
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleState() throws Exception {
        // Initialize the database
        vehicleStateRepository.saveAndFlush(vehicleState);

        int databaseSizeBeforeDelete = vehicleStateRepository.findAll().size();

        // Delete the vehicleState
        restVehicleStateMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleState.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VehicleState> vehicleStateList = vehicleStateRepository.findAll();
        assertThat(vehicleStateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
