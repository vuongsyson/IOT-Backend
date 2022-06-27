package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.RentalHistory;
import com.sonpj.repository.RentalHistoryRepository;
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
 * Integration tests for the {@link RentalHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restRentalHistoryMockMvc;

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

    @BeforeEach
    public void initTest() {
        rentalHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createRentalHistory() throws Exception {
        int databaseSizeBeforeCreate = rentalHistoryRepository.findAll().size();
        // Create the RentalHistory
        restRentalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalHistory)))
            .andExpect(status().isCreated());

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        RentalHistory testRentalHistory = rentalHistoryList.get(rentalHistoryList.size() - 1);
        assertThat(testRentalHistory.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testRentalHistory.getBatteryId()).isEqualTo(DEFAULT_BATTERY_ID);
        assertThat(testRentalHistory.getTimeStart()).isEqualTo(DEFAULT_TIME_START);
        assertThat(testRentalHistory.getTimeEnd()).isEqualTo(DEFAULT_TIME_END);
    }

    @Test
    @Transactional
    void createRentalHistoryWithExistingId() throws Exception {
        // Create the RentalHistory with an existing ID
        rentalHistory.setId(1L);

        int databaseSizeBeforeCreate = rentalHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalHistory)))
            .andExpect(status().isBadRequest());

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRentalHistories() throws Exception {
        // Initialize the database
        rentalHistoryRepository.saveAndFlush(rentalHistory);

        // Get all the rentalHistoryList
        restRentalHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rentalHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].batteryId").value(hasItem(DEFAULT_BATTERY_ID.intValue())))
            .andExpect(jsonPath("$.[*].timeStart").value(hasItem(DEFAULT_TIME_START.toString())))
            .andExpect(jsonPath("$.[*].timeEnd").value(hasItem(DEFAULT_TIME_END.toString())));
    }

    @Test
    @Transactional
    void getRentalHistory() throws Exception {
        // Initialize the database
        rentalHistoryRepository.saveAndFlush(rentalHistory);

        // Get the rentalHistory
        restRentalHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, rentalHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rentalHistory.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.batteryId").value(DEFAULT_BATTERY_ID.intValue()))
            .andExpect(jsonPath("$.timeStart").value(DEFAULT_TIME_START.toString()))
            .andExpect(jsonPath("$.timeEnd").value(DEFAULT_TIME_END.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRentalHistory() throws Exception {
        // Get the rentalHistory
        restRentalHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRentalHistory() throws Exception {
        // Initialize the database
        rentalHistoryRepository.saveAndFlush(rentalHistory);

        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().size();

        // Update the rentalHistory
        RentalHistory updatedRentalHistory = rentalHistoryRepository.findById(rentalHistory.getId()).get();
        // Disconnect from session so that the updates on updatedRentalHistory are not directly saved in db
        em.detach(updatedRentalHistory);
        updatedRentalHistory.userId(UPDATED_USER_ID).batteryId(UPDATED_BATTERY_ID).timeStart(UPDATED_TIME_START).timeEnd(UPDATED_TIME_END);

        restRentalHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRentalHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRentalHistory))
            )
            .andExpect(status().isOk());

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
        RentalHistory testRentalHistory = rentalHistoryList.get(rentalHistoryList.size() - 1);
        assertThat(testRentalHistory.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRentalHistory.getBatteryId()).isEqualTo(UPDATED_BATTERY_ID);
        assertThat(testRentalHistory.getTimeStart()).isEqualTo(UPDATED_TIME_START);
        assertThat(testRentalHistory.getTimeEnd()).isEqualTo(UPDATED_TIME_END);
    }

    @Test
    @Transactional
    void putNonExistingRentalHistory() throws Exception {
        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().size();
        rentalHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rentalHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRentalHistory() throws Exception {
        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().size();
        rentalHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRentalHistory() throws Exception {
        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().size();
        rentalHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRentalHistoryWithPatch() throws Exception {
        // Initialize the database
        rentalHistoryRepository.saveAndFlush(rentalHistory);

        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().size();

        // Update the rentalHistory using partial update
        RentalHistory partialUpdatedRentalHistory = new RentalHistory();
        partialUpdatedRentalHistory.setId(rentalHistory.getId());

        partialUpdatedRentalHistory.batteryId(UPDATED_BATTERY_ID).timeStart(UPDATED_TIME_START);

        restRentalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRentalHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRentalHistory))
            )
            .andExpect(status().isOk());

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
        RentalHistory testRentalHistory = rentalHistoryList.get(rentalHistoryList.size() - 1);
        assertThat(testRentalHistory.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testRentalHistory.getBatteryId()).isEqualTo(UPDATED_BATTERY_ID);
        assertThat(testRentalHistory.getTimeStart()).isEqualTo(UPDATED_TIME_START);
        assertThat(testRentalHistory.getTimeEnd()).isEqualTo(DEFAULT_TIME_END);
    }

    @Test
    @Transactional
    void fullUpdateRentalHistoryWithPatch() throws Exception {
        // Initialize the database
        rentalHistoryRepository.saveAndFlush(rentalHistory);

        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().size();

        // Update the rentalHistory using partial update
        RentalHistory partialUpdatedRentalHistory = new RentalHistory();
        partialUpdatedRentalHistory.setId(rentalHistory.getId());

        partialUpdatedRentalHistory
            .userId(UPDATED_USER_ID)
            .batteryId(UPDATED_BATTERY_ID)
            .timeStart(UPDATED_TIME_START)
            .timeEnd(UPDATED_TIME_END);

        restRentalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRentalHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRentalHistory))
            )
            .andExpect(status().isOk());

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
        RentalHistory testRentalHistory = rentalHistoryList.get(rentalHistoryList.size() - 1);
        assertThat(testRentalHistory.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRentalHistory.getBatteryId()).isEqualTo(UPDATED_BATTERY_ID);
        assertThat(testRentalHistory.getTimeStart()).isEqualTo(UPDATED_TIME_START);
        assertThat(testRentalHistory.getTimeEnd()).isEqualTo(UPDATED_TIME_END);
    }

    @Test
    @Transactional
    void patchNonExistingRentalHistory() throws Exception {
        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().size();
        rentalHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rentalHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rentalHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRentalHistory() throws Exception {
        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().size();
        rentalHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rentalHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRentalHistory() throws Exception {
        int databaseSizeBeforeUpdate = rentalHistoryRepository.findAll().size();
        rentalHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rentalHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RentalHistory in the database
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRentalHistory() throws Exception {
        // Initialize the database
        rentalHistoryRepository.saveAndFlush(rentalHistory);

        int databaseSizeBeforeDelete = rentalHistoryRepository.findAll().size();

        // Delete the rentalHistory
        restRentalHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, rentalHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RentalHistory> rentalHistoryList = rentalHistoryRepository.findAll();
        assertThat(rentalHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
