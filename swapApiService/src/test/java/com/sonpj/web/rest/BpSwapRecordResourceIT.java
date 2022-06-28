package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.BpSwapRecord;
import com.sonpj.repository.BpSwapRecordRepository;
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
 * Integration tests for the {@link BpSwapRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BpSwapRecordResourceIT {

    private static final String DEFAULT_OLD_BAT = "AAAAAAAAAA";
    private static final String UPDATED_OLD_BAT = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_BAT = "AAAAAAAAAA";
    private static final String UPDATED_NEW_BAT = "BBBBBBBBBB";

    private static final String DEFAULT_OLD_CAB = "AAAAAAAAAA";
    private static final String UPDATED_OLD_CAB = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_CAB = "AAAAAAAAAA";
    private static final String UPDATED_NEW_CAB = "BBBBBBBBBB";

    private static final String DEFAULT_BSS = "AAAAAAAAAA";
    private static final String UPDATED_BSS = "BBBBBBBBBB";

    private static final Long DEFAULT_USER = 1L;
    private static final Long UPDATED_USER = 2L;

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final Integer DEFAULT_ERROR = 1;
    private static final Integer UPDATED_ERROR = 2;

    private static final String ENTITY_API_URL = "/api/bp-swap-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BpSwapRecordRepository bpSwapRecordRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBpSwapRecordMockMvc;

    private BpSwapRecord bpSwapRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BpSwapRecord createEntity(EntityManager em) {
        BpSwapRecord bpSwapRecord = new BpSwapRecord()
            .oldBat(DEFAULT_OLD_BAT)
            .newBat(DEFAULT_NEW_BAT)
            .oldCab(DEFAULT_OLD_CAB)
            .newCab(DEFAULT_NEW_CAB)
            .bss(DEFAULT_BSS)
            .user(DEFAULT_USER)
            .state(DEFAULT_STATE)
            .error(DEFAULT_ERROR);
        return bpSwapRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BpSwapRecord createUpdatedEntity(EntityManager em) {
        BpSwapRecord bpSwapRecord = new BpSwapRecord()
            .oldBat(UPDATED_OLD_BAT)
            .newBat(UPDATED_NEW_BAT)
            .oldCab(UPDATED_OLD_CAB)
            .newCab(UPDATED_NEW_CAB)
            .bss(UPDATED_BSS)
            .user(UPDATED_USER)
            .state(UPDATED_STATE)
            .error(UPDATED_ERROR);
        return bpSwapRecord;
    }

    @BeforeEach
    public void initTest() {
        bpSwapRecord = createEntity(em);
    }

    @Test
    @Transactional
    void createBpSwapRecord() throws Exception {
        int databaseSizeBeforeCreate = bpSwapRecordRepository.findAll().size();
        // Create the BpSwapRecord
        restBpSwapRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bpSwapRecord)))
            .andExpect(status().isCreated());

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeCreate + 1);
        BpSwapRecord testBpSwapRecord = bpSwapRecordList.get(bpSwapRecordList.size() - 1);
        assertThat(testBpSwapRecord.getOldBat()).isEqualTo(DEFAULT_OLD_BAT);
        assertThat(testBpSwapRecord.getNewBat()).isEqualTo(DEFAULT_NEW_BAT);
        assertThat(testBpSwapRecord.getOldCab()).isEqualTo(DEFAULT_OLD_CAB);
        assertThat(testBpSwapRecord.getNewCab()).isEqualTo(DEFAULT_NEW_CAB);
        assertThat(testBpSwapRecord.getBss()).isEqualTo(DEFAULT_BSS);
        assertThat(testBpSwapRecord.getUser()).isEqualTo(DEFAULT_USER);
        assertThat(testBpSwapRecord.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testBpSwapRecord.getError()).isEqualTo(DEFAULT_ERROR);
    }

    @Test
    @Transactional
    void createBpSwapRecordWithExistingId() throws Exception {
        // Create the BpSwapRecord with an existing ID
        bpSwapRecord.setId(1L);

        int databaseSizeBeforeCreate = bpSwapRecordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBpSwapRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bpSwapRecord)))
            .andExpect(status().isBadRequest());

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBpSwapRecords() throws Exception {
        // Initialize the database
        bpSwapRecordRepository.saveAndFlush(bpSwapRecord);

        // Get all the bpSwapRecordList
        restBpSwapRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bpSwapRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].oldBat").value(hasItem(DEFAULT_OLD_BAT)))
            .andExpect(jsonPath("$.[*].newBat").value(hasItem(DEFAULT_NEW_BAT)))
            .andExpect(jsonPath("$.[*].oldCab").value(hasItem(DEFAULT_OLD_CAB)))
            .andExpect(jsonPath("$.[*].newCab").value(hasItem(DEFAULT_NEW_CAB)))
            .andExpect(jsonPath("$.[*].bss").value(hasItem(DEFAULT_BSS)))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].error").value(hasItem(DEFAULT_ERROR)));
    }

    @Test
    @Transactional
    void getBpSwapRecord() throws Exception {
        // Initialize the database
        bpSwapRecordRepository.saveAndFlush(bpSwapRecord);

        // Get the bpSwapRecord
        restBpSwapRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, bpSwapRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bpSwapRecord.getId().intValue()))
            .andExpect(jsonPath("$.oldBat").value(DEFAULT_OLD_BAT))
            .andExpect(jsonPath("$.newBat").value(DEFAULT_NEW_BAT))
            .andExpect(jsonPath("$.oldCab").value(DEFAULT_OLD_CAB))
            .andExpect(jsonPath("$.newCab").value(DEFAULT_NEW_CAB))
            .andExpect(jsonPath("$.bss").value(DEFAULT_BSS))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER.intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.error").value(DEFAULT_ERROR));
    }

    @Test
    @Transactional
    void getNonExistingBpSwapRecord() throws Exception {
        // Get the bpSwapRecord
        restBpSwapRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBpSwapRecord() throws Exception {
        // Initialize the database
        bpSwapRecordRepository.saveAndFlush(bpSwapRecord);

        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().size();

        // Update the bpSwapRecord
        BpSwapRecord updatedBpSwapRecord = bpSwapRecordRepository.findById(bpSwapRecord.getId()).get();
        // Disconnect from session so that the updates on updatedBpSwapRecord are not directly saved in db
        em.detach(updatedBpSwapRecord);
        updatedBpSwapRecord
            .oldBat(UPDATED_OLD_BAT)
            .newBat(UPDATED_NEW_BAT)
            .oldCab(UPDATED_OLD_CAB)
            .newCab(UPDATED_NEW_CAB)
            .bss(UPDATED_BSS)
            .user(UPDATED_USER)
            .state(UPDATED_STATE)
            .error(UPDATED_ERROR);

        restBpSwapRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBpSwapRecord.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBpSwapRecord))
            )
            .andExpect(status().isOk());

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
        BpSwapRecord testBpSwapRecord = bpSwapRecordList.get(bpSwapRecordList.size() - 1);
        assertThat(testBpSwapRecord.getOldBat()).isEqualTo(UPDATED_OLD_BAT);
        assertThat(testBpSwapRecord.getNewBat()).isEqualTo(UPDATED_NEW_BAT);
        assertThat(testBpSwapRecord.getOldCab()).isEqualTo(UPDATED_OLD_CAB);
        assertThat(testBpSwapRecord.getNewCab()).isEqualTo(UPDATED_NEW_CAB);
        assertThat(testBpSwapRecord.getBss()).isEqualTo(UPDATED_BSS);
        assertThat(testBpSwapRecord.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testBpSwapRecord.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testBpSwapRecord.getError()).isEqualTo(UPDATED_ERROR);
    }

    @Test
    @Transactional
    void putNonExistingBpSwapRecord() throws Exception {
        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().size();
        bpSwapRecord.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBpSwapRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bpSwapRecord.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bpSwapRecord))
            )
            .andExpect(status().isBadRequest());

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBpSwapRecord() throws Exception {
        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().size();
        bpSwapRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBpSwapRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bpSwapRecord))
            )
            .andExpect(status().isBadRequest());

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBpSwapRecord() throws Exception {
        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().size();
        bpSwapRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBpSwapRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bpSwapRecord)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBpSwapRecordWithPatch() throws Exception {
        // Initialize the database
        bpSwapRecordRepository.saveAndFlush(bpSwapRecord);

        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().size();

        // Update the bpSwapRecord using partial update
        BpSwapRecord partialUpdatedBpSwapRecord = new BpSwapRecord();
        partialUpdatedBpSwapRecord.setId(bpSwapRecord.getId());

        partialUpdatedBpSwapRecord.newBat(UPDATED_NEW_BAT).user(UPDATED_USER);

        restBpSwapRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBpSwapRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBpSwapRecord))
            )
            .andExpect(status().isOk());

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
        BpSwapRecord testBpSwapRecord = bpSwapRecordList.get(bpSwapRecordList.size() - 1);
        assertThat(testBpSwapRecord.getOldBat()).isEqualTo(DEFAULT_OLD_BAT);
        assertThat(testBpSwapRecord.getNewBat()).isEqualTo(UPDATED_NEW_BAT);
        assertThat(testBpSwapRecord.getOldCab()).isEqualTo(DEFAULT_OLD_CAB);
        assertThat(testBpSwapRecord.getNewCab()).isEqualTo(DEFAULT_NEW_CAB);
        assertThat(testBpSwapRecord.getBss()).isEqualTo(DEFAULT_BSS);
        assertThat(testBpSwapRecord.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testBpSwapRecord.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testBpSwapRecord.getError()).isEqualTo(DEFAULT_ERROR);
    }

    @Test
    @Transactional
    void fullUpdateBpSwapRecordWithPatch() throws Exception {
        // Initialize the database
        bpSwapRecordRepository.saveAndFlush(bpSwapRecord);

        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().size();

        // Update the bpSwapRecord using partial update
        BpSwapRecord partialUpdatedBpSwapRecord = new BpSwapRecord();
        partialUpdatedBpSwapRecord.setId(bpSwapRecord.getId());

        partialUpdatedBpSwapRecord
            .oldBat(UPDATED_OLD_BAT)
            .newBat(UPDATED_NEW_BAT)
            .oldCab(UPDATED_OLD_CAB)
            .newCab(UPDATED_NEW_CAB)
            .bss(UPDATED_BSS)
            .user(UPDATED_USER)
            .state(UPDATED_STATE)
            .error(UPDATED_ERROR);

        restBpSwapRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBpSwapRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBpSwapRecord))
            )
            .andExpect(status().isOk());

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
        BpSwapRecord testBpSwapRecord = bpSwapRecordList.get(bpSwapRecordList.size() - 1);
        assertThat(testBpSwapRecord.getOldBat()).isEqualTo(UPDATED_OLD_BAT);
        assertThat(testBpSwapRecord.getNewBat()).isEqualTo(UPDATED_NEW_BAT);
        assertThat(testBpSwapRecord.getOldCab()).isEqualTo(UPDATED_OLD_CAB);
        assertThat(testBpSwapRecord.getNewCab()).isEqualTo(UPDATED_NEW_CAB);
        assertThat(testBpSwapRecord.getBss()).isEqualTo(UPDATED_BSS);
        assertThat(testBpSwapRecord.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testBpSwapRecord.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testBpSwapRecord.getError()).isEqualTo(UPDATED_ERROR);
    }

    @Test
    @Transactional
    void patchNonExistingBpSwapRecord() throws Exception {
        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().size();
        bpSwapRecord.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBpSwapRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bpSwapRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bpSwapRecord))
            )
            .andExpect(status().isBadRequest());

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBpSwapRecord() throws Exception {
        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().size();
        bpSwapRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBpSwapRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bpSwapRecord))
            )
            .andExpect(status().isBadRequest());

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBpSwapRecord() throws Exception {
        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().size();
        bpSwapRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBpSwapRecordMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bpSwapRecord))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBpSwapRecord() throws Exception {
        // Initialize the database
        bpSwapRecordRepository.saveAndFlush(bpSwapRecord);

        int databaseSizeBeforeDelete = bpSwapRecordRepository.findAll().size();

        // Delete the bpSwapRecord
        restBpSwapRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, bpSwapRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
