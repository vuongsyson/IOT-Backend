package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.BatteryState;
import com.sonpj.repository.BatteryStateRepository;
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
 * Integration tests for the {@link BatteryStateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restBatteryStateMockMvc;

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

    @BeforeEach
    public void initTest() {
        batteryState = createEntity(em);
    }

    @Test
    @Transactional
    void createBatteryState() throws Exception {
        int databaseSizeBeforeCreate = batteryStateRepository.findAll().size();
        // Create the BatteryState
        restBatteryStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batteryState)))
            .andExpect(status().isCreated());

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
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
    @Transactional
    void createBatteryStateWithExistingId() throws Exception {
        // Create the BatteryState with an existing ID
        batteryState.setId(1L);

        int databaseSizeBeforeCreate = batteryStateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBatteryStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batteryState)))
            .andExpect(status().isBadRequest());

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSerialNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryStateRepository.findAll().size();
        // set the field null
        batteryState.setSerialNumber(null);

        // Create the BatteryState, which fails.

        restBatteryStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batteryState)))
            .andExpect(status().isBadRequest());

        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVolIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryStateRepository.findAll().size();
        // set the field null
        batteryState.setVol(null);

        // Create the BatteryState, which fails.

        restBatteryStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batteryState)))
            .andExpect(status().isBadRequest());

        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryStateRepository.findAll().size();
        // set the field null
        batteryState.setCur(null);

        // Create the BatteryState, which fails.

        restBatteryStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batteryState)))
            .andExpect(status().isBadRequest());

        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSocIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryStateRepository.findAll().size();
        // set the field null
        batteryState.setSoc(null);

        // Create the BatteryState, which fails.

        restBatteryStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batteryState)))
            .andExpect(status().isBadRequest());

        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSohIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryStateRepository.findAll().size();
        // set the field null
        batteryState.setSoh(null);

        // Create the BatteryState, which fails.

        restBatteryStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batteryState)))
            .andExpect(status().isBadRequest());

        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryStateRepository.findAll().size();
        // set the field null
        batteryState.setState(null);

        // Create the BatteryState, which fails.

        restBatteryStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batteryState)))
            .andExpect(status().isBadRequest());

        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryStateRepository.findAll().size();
        // set the field null
        batteryState.setStatus(null);

        // Create the BatteryState, which fails.

        restBatteryStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batteryState)))
            .andExpect(status().isBadRequest());

        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBatteryStates() throws Exception {
        // Initialize the database
        batteryStateRepository.saveAndFlush(batteryState);

        // Get all the batteryStateList
        restBatteryStateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batteryState.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].vol").value(hasItem(DEFAULT_VOL)))
            .andExpect(jsonPath("$.[*].cur").value(hasItem(DEFAULT_CUR)))
            .andExpect(jsonPath("$.[*].soc").value(hasItem(DEFAULT_SOC)))
            .andExpect(jsonPath("$.[*].soh").value(hasItem(DEFAULT_SOH)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getBatteryState() throws Exception {
        // Initialize the database
        batteryStateRepository.saveAndFlush(batteryState);

        // Get the batteryState
        restBatteryStateMockMvc
            .perform(get(ENTITY_API_URL_ID, batteryState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(batteryState.getId().intValue()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.vol").value(DEFAULT_VOL))
            .andExpect(jsonPath("$.cur").value(DEFAULT_CUR))
            .andExpect(jsonPath("$.soc").value(DEFAULT_SOC))
            .andExpect(jsonPath("$.soh").value(DEFAULT_SOH))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingBatteryState() throws Exception {
        // Get the batteryState
        restBatteryStateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBatteryState() throws Exception {
        // Initialize the database
        batteryStateRepository.saveAndFlush(batteryState);

        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().size();

        // Update the batteryState
        BatteryState updatedBatteryState = batteryStateRepository.findById(batteryState.getId()).get();
        // Disconnect from session so that the updates on updatedBatteryState are not directly saved in db
        em.detach(updatedBatteryState);
        updatedBatteryState
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .vol(UPDATED_VOL)
            .cur(UPDATED_CUR)
            .soc(UPDATED_SOC)
            .soh(UPDATED_SOH)
            .state(UPDATED_STATE)
            .status(UPDATED_STATUS);

        restBatteryStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBatteryState.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBatteryState))
            )
            .andExpect(status().isOk());

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
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
    @Transactional
    void putNonExistingBatteryState() throws Exception {
        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().size();
        batteryState.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatteryStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, batteryState.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batteryState))
            )
            .andExpect(status().isBadRequest());

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBatteryState() throws Exception {
        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().size();
        batteryState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatteryStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batteryState))
            )
            .andExpect(status().isBadRequest());

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBatteryState() throws Exception {
        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().size();
        batteryState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatteryStateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batteryState)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBatteryStateWithPatch() throws Exception {
        // Initialize the database
        batteryStateRepository.saveAndFlush(batteryState);

        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().size();

        // Update the batteryState using partial update
        BatteryState partialUpdatedBatteryState = new BatteryState();
        partialUpdatedBatteryState.setId(batteryState.getId());

        partialUpdatedBatteryState
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .vol(UPDATED_VOL)
            .cur(UPDATED_CUR)
            .soc(UPDATED_SOC)
            .state(UPDATED_STATE);

        restBatteryStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatteryState.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBatteryState))
            )
            .andExpect(status().isOk());

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
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
    @Transactional
    void fullUpdateBatteryStateWithPatch() throws Exception {
        // Initialize the database
        batteryStateRepository.saveAndFlush(batteryState);

        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().size();

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

        restBatteryStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatteryState.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBatteryState))
            )
            .andExpect(status().isOk());

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
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
    @Transactional
    void patchNonExistingBatteryState() throws Exception {
        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().size();
        batteryState.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatteryStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, batteryState.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(batteryState))
            )
            .andExpect(status().isBadRequest());

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBatteryState() throws Exception {
        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().size();
        batteryState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatteryStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(batteryState))
            )
            .andExpect(status().isBadRequest());

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBatteryState() throws Exception {
        int databaseSizeBeforeUpdate = batteryStateRepository.findAll().size();
        batteryState.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatteryStateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(batteryState))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BatteryState in the database
        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBatteryState() throws Exception {
        // Initialize the database
        batteryStateRepository.saveAndFlush(batteryState);

        int databaseSizeBeforeDelete = batteryStateRepository.findAll().size();

        // Delete the batteryState
        restBatteryStateMockMvc
            .perform(delete(ENTITY_API_URL_ID, batteryState.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BatteryState> batteryStateList = batteryStateRepository.findAll();
        assertThat(batteryStateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
