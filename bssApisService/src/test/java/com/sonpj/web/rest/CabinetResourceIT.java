package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.Cabinet;
import com.sonpj.repository.CabinetRepository;
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
 * Integration tests for the {@link CabinetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CabinetResourceIT {

    private static final Long DEFAULT_BSS_ID = 1L;
    private static final Long UPDATED_BSS_ID = 2L;

    private static final Long DEFAULT_BP_ID = 1L;
    private static final Long UPDATED_BP_ID = 2L;

    private static final Boolean DEFAULT_BP_READY = false;
    private static final Boolean UPDATED_BP_READY = true;

    private static final Long DEFAULT_SWAP_NO = 1L;
    private static final Long UPDATED_SWAP_NO = 2L;

    private static final Integer DEFAULT_STATE_CODE = 1;
    private static final Integer UPDATED_STATE_CODE = 2;

    private static final String ENTITY_API_URL = "/api/cabinets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CabinetRepository cabinetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCabinetMockMvc;

    private Cabinet cabinet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cabinet createEntity(EntityManager em) {
        Cabinet cabinet = new Cabinet()
            .bssId(DEFAULT_BSS_ID)
            .bpId(DEFAULT_BP_ID)
            .bpReady(DEFAULT_BP_READY)
            .swapNo(DEFAULT_SWAP_NO)
            .stateCode(DEFAULT_STATE_CODE);
        return cabinet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cabinet createUpdatedEntity(EntityManager em) {
        Cabinet cabinet = new Cabinet()
            .bssId(UPDATED_BSS_ID)
            .bpId(UPDATED_BP_ID)
            .bpReady(UPDATED_BP_READY)
            .swapNo(UPDATED_SWAP_NO)
            .stateCode(UPDATED_STATE_CODE);
        return cabinet;
    }

    @BeforeEach
    public void initTest() {
        cabinet = createEntity(em);
    }

    @Test
    @Transactional
    void createCabinet() throws Exception {
        int databaseSizeBeforeCreate = cabinetRepository.findAll().size();
        // Create the Cabinet
        restCabinetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cabinet)))
            .andExpect(status().isCreated());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeCreate + 1);
        Cabinet testCabinet = cabinetList.get(cabinetList.size() - 1);
        assertThat(testCabinet.getBssId()).isEqualTo(DEFAULT_BSS_ID);
        assertThat(testCabinet.getBpId()).isEqualTo(DEFAULT_BP_ID);
        assertThat(testCabinet.getBpReady()).isEqualTo(DEFAULT_BP_READY);
        assertThat(testCabinet.getSwapNo()).isEqualTo(DEFAULT_SWAP_NO);
        assertThat(testCabinet.getStateCode()).isEqualTo(DEFAULT_STATE_CODE);
    }

    @Test
    @Transactional
    void createCabinetWithExistingId() throws Exception {
        // Create the Cabinet with an existing ID
        cabinet.setId(1L);

        int databaseSizeBeforeCreate = cabinetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCabinetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cabinet)))
            .andExpect(status().isBadRequest());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBssIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = cabinetRepository.findAll().size();
        // set the field null
        cabinet.setBssId(null);

        // Create the Cabinet, which fails.

        restCabinetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cabinet)))
            .andExpect(status().isBadRequest());

        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCabinets() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);

        // Get all the cabinetList
        restCabinetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cabinet.getId().intValue())))
            .andExpect(jsonPath("$.[*].bssId").value(hasItem(DEFAULT_BSS_ID.intValue())))
            .andExpect(jsonPath("$.[*].bpId").value(hasItem(DEFAULT_BP_ID.intValue())))
            .andExpect(jsonPath("$.[*].bpReady").value(hasItem(DEFAULT_BP_READY.booleanValue())))
            .andExpect(jsonPath("$.[*].swapNo").value(hasItem(DEFAULT_SWAP_NO.intValue())))
            .andExpect(jsonPath("$.[*].stateCode").value(hasItem(DEFAULT_STATE_CODE)));
    }

    @Test
    @Transactional
    void getCabinet() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);

        // Get the cabinet
        restCabinetMockMvc
            .perform(get(ENTITY_API_URL_ID, cabinet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cabinet.getId().intValue()))
            .andExpect(jsonPath("$.bssId").value(DEFAULT_BSS_ID.intValue()))
            .andExpect(jsonPath("$.bpId").value(DEFAULT_BP_ID.intValue()))
            .andExpect(jsonPath("$.bpReady").value(DEFAULT_BP_READY.booleanValue()))
            .andExpect(jsonPath("$.swapNo").value(DEFAULT_SWAP_NO.intValue()))
            .andExpect(jsonPath("$.stateCode").value(DEFAULT_STATE_CODE));
    }

    @Test
    @Transactional
    void getNonExistingCabinet() throws Exception {
        // Get the cabinet
        restCabinetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCabinet() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);

        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();

        // Update the cabinet
        Cabinet updatedCabinet = cabinetRepository.findById(cabinet.getId()).get();
        // Disconnect from session so that the updates on updatedCabinet are not directly saved in db
        em.detach(updatedCabinet);
        updatedCabinet
            .bssId(UPDATED_BSS_ID)
            .bpId(UPDATED_BP_ID)
            .bpReady(UPDATED_BP_READY)
            .swapNo(UPDATED_SWAP_NO)
            .stateCode(UPDATED_STATE_CODE);

        restCabinetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCabinet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCabinet))
            )
            .andExpect(status().isOk());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
        Cabinet testCabinet = cabinetList.get(cabinetList.size() - 1);
        assertThat(testCabinet.getBssId()).isEqualTo(UPDATED_BSS_ID);
        assertThat(testCabinet.getBpId()).isEqualTo(UPDATED_BP_ID);
        assertThat(testCabinet.getBpReady()).isEqualTo(UPDATED_BP_READY);
        assertThat(testCabinet.getSwapNo()).isEqualTo(UPDATED_SWAP_NO);
        assertThat(testCabinet.getStateCode()).isEqualTo(UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    void putNonExistingCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();
        cabinet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCabinetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cabinet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cabinet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();
        cabinet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCabinetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cabinet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();
        cabinet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCabinetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cabinet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCabinetWithPatch() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);

        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();

        // Update the cabinet using partial update
        Cabinet partialUpdatedCabinet = new Cabinet();
        partialUpdatedCabinet.setId(cabinet.getId());

        partialUpdatedCabinet.bpReady(UPDATED_BP_READY).swapNo(UPDATED_SWAP_NO);

        restCabinetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCabinet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCabinet))
            )
            .andExpect(status().isOk());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
        Cabinet testCabinet = cabinetList.get(cabinetList.size() - 1);
        assertThat(testCabinet.getBssId()).isEqualTo(DEFAULT_BSS_ID);
        assertThat(testCabinet.getBpId()).isEqualTo(DEFAULT_BP_ID);
        assertThat(testCabinet.getBpReady()).isEqualTo(UPDATED_BP_READY);
        assertThat(testCabinet.getSwapNo()).isEqualTo(UPDATED_SWAP_NO);
        assertThat(testCabinet.getStateCode()).isEqualTo(DEFAULT_STATE_CODE);
    }

    @Test
    @Transactional
    void fullUpdateCabinetWithPatch() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);

        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();

        // Update the cabinet using partial update
        Cabinet partialUpdatedCabinet = new Cabinet();
        partialUpdatedCabinet.setId(cabinet.getId());

        partialUpdatedCabinet
            .bssId(UPDATED_BSS_ID)
            .bpId(UPDATED_BP_ID)
            .bpReady(UPDATED_BP_READY)
            .swapNo(UPDATED_SWAP_NO)
            .stateCode(UPDATED_STATE_CODE);

        restCabinetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCabinet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCabinet))
            )
            .andExpect(status().isOk());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
        Cabinet testCabinet = cabinetList.get(cabinetList.size() - 1);
        assertThat(testCabinet.getBssId()).isEqualTo(UPDATED_BSS_ID);
        assertThat(testCabinet.getBpId()).isEqualTo(UPDATED_BP_ID);
        assertThat(testCabinet.getBpReady()).isEqualTo(UPDATED_BP_READY);
        assertThat(testCabinet.getSwapNo()).isEqualTo(UPDATED_SWAP_NO);
        assertThat(testCabinet.getStateCode()).isEqualTo(UPDATED_STATE_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();
        cabinet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCabinetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cabinet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cabinet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();
        cabinet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCabinetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cabinet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();
        cabinet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCabinetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cabinet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCabinet() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);

        int databaseSizeBeforeDelete = cabinetRepository.findAll().size();

        // Delete the cabinet
        restCabinetMockMvc
            .perform(delete(ENTITY_API_URL_ID, cabinet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
