package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.Bss;
import com.sonpj.repository.BssRepository;
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
 * Integration tests for the {@link BssResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BssResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_HW_VERSION = 1;
    private static final Integer UPDATED_HW_VERSION = 2;

    private static final Integer DEFAULT_SW_VERSION = 1;
    private static final Integer UPDATED_SW_VERSION = 2;

    private static final String DEFAULT_MANUFACTURE_DATE = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURE_DATE = "BBBBBBBBBB";

    private static final Double DEFAULT_LON = 1D;
    private static final Double UPDATED_LON = 2D;

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;

    private static final Integer DEFAULT_TYPE_CODE = 1;
    private static final Integer UPDATED_TYPE_CODE = 2;

    private static final Integer DEFAULT_CAB_NUM = 1;
    private static final Integer UPDATED_CAB_NUM = 2;

    private static final Integer DEFAULT_CAB_EMPTY_NUM = 1;
    private static final Integer UPDATED_CAB_EMPTY_NUM = 2;

    private static final Integer DEFAULT_BP_READY_NUM = 1;
    private static final Integer UPDATED_BP_READY_NUM = 2;

    private static final Long DEFAULT_SWAP_BP_NO = 1L;
    private static final Long UPDATED_SWAP_BP_NO = 2L;

    private static final String ENTITY_API_URL = "/api/bsses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BssRepository bssRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBssMockMvc;

    private Bss bss;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bss createEntity(EntityManager em) {
        Bss bss = new Bss()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .hwVersion(DEFAULT_HW_VERSION)
            .swVersion(DEFAULT_SW_VERSION)
            .manufactureDate(DEFAULT_MANUFACTURE_DATE)
            .lon(DEFAULT_LON)
            .lat(DEFAULT_LAT)
            .typeCode(DEFAULT_TYPE_CODE)
            .cabNum(DEFAULT_CAB_NUM)
            .cabEmptyNum(DEFAULT_CAB_EMPTY_NUM)
            .bpReadyNum(DEFAULT_BP_READY_NUM)
            .swapBpNo(DEFAULT_SWAP_BP_NO);
        return bss;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bss createUpdatedEntity(EntityManager em) {
        Bss bss = new Bss()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .hwVersion(UPDATED_HW_VERSION)
            .swVersion(UPDATED_SW_VERSION)
            .manufactureDate(UPDATED_MANUFACTURE_DATE)
            .lon(UPDATED_LON)
            .lat(UPDATED_LAT)
            .typeCode(UPDATED_TYPE_CODE)
            .cabNum(UPDATED_CAB_NUM)
            .cabEmptyNum(UPDATED_CAB_EMPTY_NUM)
            .bpReadyNum(UPDATED_BP_READY_NUM)
            .swapBpNo(UPDATED_SWAP_BP_NO);
        return bss;
    }

    @BeforeEach
    public void initTest() {
        bss = createEntity(em);
    }

    @Test
    @Transactional
    void createBss() throws Exception {
        int databaseSizeBeforeCreate = bssRepository.findAll().size();
        // Create the Bss
        restBssMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bss)))
            .andExpect(status().isCreated());

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll();
        assertThat(bssList).hasSize(databaseSizeBeforeCreate + 1);
        Bss testBss = bssList.get(bssList.size() - 1);
        assertThat(testBss.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBss.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testBss.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testBss.getHwVersion()).isEqualTo(DEFAULT_HW_VERSION);
        assertThat(testBss.getSwVersion()).isEqualTo(DEFAULT_SW_VERSION);
        assertThat(testBss.getManufactureDate()).isEqualTo(DEFAULT_MANUFACTURE_DATE);
        assertThat(testBss.getLon()).isEqualTo(DEFAULT_LON);
        assertThat(testBss.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testBss.getTypeCode()).isEqualTo(DEFAULT_TYPE_CODE);
        assertThat(testBss.getCabNum()).isEqualTo(DEFAULT_CAB_NUM);
        assertThat(testBss.getCabEmptyNum()).isEqualTo(DEFAULT_CAB_EMPTY_NUM);
        assertThat(testBss.getBpReadyNum()).isEqualTo(DEFAULT_BP_READY_NUM);
        assertThat(testBss.getSwapBpNo()).isEqualTo(DEFAULT_SWAP_BP_NO);
    }

    @Test
    @Transactional
    void createBssWithExistingId() throws Exception {
        // Create the Bss with an existing ID
        bss.setId(1L);

        int databaseSizeBeforeCreate = bssRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBssMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bss)))
            .andExpect(status().isBadRequest());

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll();
        assertThat(bssList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBsses() throws Exception {
        // Initialize the database
        bssRepository.saveAndFlush(bss);

        // Get all the bssList
        restBssMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bss.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].hwVersion").value(hasItem(DEFAULT_HW_VERSION)))
            .andExpect(jsonPath("$.[*].swVersion").value(hasItem(DEFAULT_SW_VERSION)))
            .andExpect(jsonPath("$.[*].manufactureDate").value(hasItem(DEFAULT_MANUFACTURE_DATE)))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].typeCode").value(hasItem(DEFAULT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].cabNum").value(hasItem(DEFAULT_CAB_NUM)))
            .andExpect(jsonPath("$.[*].cabEmptyNum").value(hasItem(DEFAULT_CAB_EMPTY_NUM)))
            .andExpect(jsonPath("$.[*].bpReadyNum").value(hasItem(DEFAULT_BP_READY_NUM)))
            .andExpect(jsonPath("$.[*].swapBpNo").value(hasItem(DEFAULT_SWAP_BP_NO.intValue())));
    }

    @Test
    @Transactional
    void getBss() throws Exception {
        // Initialize the database
        bssRepository.saveAndFlush(bss);

        // Get the bss
        restBssMockMvc
            .perform(get(ENTITY_API_URL_ID, bss.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bss.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.hwVersion").value(DEFAULT_HW_VERSION))
            .andExpect(jsonPath("$.swVersion").value(DEFAULT_SW_VERSION))
            .andExpect(jsonPath("$.manufactureDate").value(DEFAULT_MANUFACTURE_DATE))
            .andExpect(jsonPath("$.lon").value(DEFAULT_LON.doubleValue()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.typeCode").value(DEFAULT_TYPE_CODE))
            .andExpect(jsonPath("$.cabNum").value(DEFAULT_CAB_NUM))
            .andExpect(jsonPath("$.cabEmptyNum").value(DEFAULT_CAB_EMPTY_NUM))
            .andExpect(jsonPath("$.bpReadyNum").value(DEFAULT_BP_READY_NUM))
            .andExpect(jsonPath("$.swapBpNo").value(DEFAULT_SWAP_BP_NO.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingBss() throws Exception {
        // Get the bss
        restBssMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBss() throws Exception {
        // Initialize the database
        bssRepository.saveAndFlush(bss);

        int databaseSizeBeforeUpdate = bssRepository.findAll().size();

        // Update the bss
        Bss updatedBss = bssRepository.findById(bss.getId()).get();
        // Disconnect from session so that the updates on updatedBss are not directly saved in db
        em.detach(updatedBss);
        updatedBss
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .hwVersion(UPDATED_HW_VERSION)
            .swVersion(UPDATED_SW_VERSION)
            .manufactureDate(UPDATED_MANUFACTURE_DATE)
            .lon(UPDATED_LON)
            .lat(UPDATED_LAT)
            .typeCode(UPDATED_TYPE_CODE)
            .cabNum(UPDATED_CAB_NUM)
            .cabEmptyNum(UPDATED_CAB_EMPTY_NUM)
            .bpReadyNum(UPDATED_BP_READY_NUM)
            .swapBpNo(UPDATED_SWAP_BP_NO);

        restBssMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBss.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBss))
            )
            .andExpect(status().isOk());

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
        Bss testBss = bssList.get(bssList.size() - 1);
        assertThat(testBss.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBss.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testBss.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testBss.getHwVersion()).isEqualTo(UPDATED_HW_VERSION);
        assertThat(testBss.getSwVersion()).isEqualTo(UPDATED_SW_VERSION);
        assertThat(testBss.getManufactureDate()).isEqualTo(UPDATED_MANUFACTURE_DATE);
        assertThat(testBss.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testBss.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testBss.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testBss.getCabNum()).isEqualTo(UPDATED_CAB_NUM);
        assertThat(testBss.getCabEmptyNum()).isEqualTo(UPDATED_CAB_EMPTY_NUM);
        assertThat(testBss.getBpReadyNum()).isEqualTo(UPDATED_BP_READY_NUM);
        assertThat(testBss.getSwapBpNo()).isEqualTo(UPDATED_SWAP_BP_NO);
    }

    @Test
    @Transactional
    void putNonExistingBss() throws Exception {
        int databaseSizeBeforeUpdate = bssRepository.findAll().size();
        bss.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBssMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bss.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bss))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBss() throws Exception {
        int databaseSizeBeforeUpdate = bssRepository.findAll().size();
        bss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBssMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bss))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBss() throws Exception {
        int databaseSizeBeforeUpdate = bssRepository.findAll().size();
        bss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBssMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bss)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBssWithPatch() throws Exception {
        // Initialize the database
        bssRepository.saveAndFlush(bss);

        int databaseSizeBeforeUpdate = bssRepository.findAll().size();

        // Update the bss using partial update
        Bss partialUpdatedBss = new Bss();
        partialUpdatedBss.setId(bss.getId());

        partialUpdatedBss
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .swVersion(UPDATED_SW_VERSION)
            .manufactureDate(UPDATED_MANUFACTURE_DATE)
            .typeCode(UPDATED_TYPE_CODE)
            .cabNum(UPDATED_CAB_NUM)
            .cabEmptyNum(UPDATED_CAB_EMPTY_NUM)
            .bpReadyNum(UPDATED_BP_READY_NUM)
            .swapBpNo(UPDATED_SWAP_BP_NO);

        restBssMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBss.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBss))
            )
            .andExpect(status().isOk());

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
        Bss testBss = bssList.get(bssList.size() - 1);
        assertThat(testBss.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBss.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testBss.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testBss.getHwVersion()).isEqualTo(DEFAULT_HW_VERSION);
        assertThat(testBss.getSwVersion()).isEqualTo(UPDATED_SW_VERSION);
        assertThat(testBss.getManufactureDate()).isEqualTo(UPDATED_MANUFACTURE_DATE);
        assertThat(testBss.getLon()).isEqualTo(DEFAULT_LON);
        assertThat(testBss.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testBss.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testBss.getCabNum()).isEqualTo(UPDATED_CAB_NUM);
        assertThat(testBss.getCabEmptyNum()).isEqualTo(UPDATED_CAB_EMPTY_NUM);
        assertThat(testBss.getBpReadyNum()).isEqualTo(UPDATED_BP_READY_NUM);
        assertThat(testBss.getSwapBpNo()).isEqualTo(UPDATED_SWAP_BP_NO);
    }

    @Test
    @Transactional
    void fullUpdateBssWithPatch() throws Exception {
        // Initialize the database
        bssRepository.saveAndFlush(bss);

        int databaseSizeBeforeUpdate = bssRepository.findAll().size();

        // Update the bss using partial update
        Bss partialUpdatedBss = new Bss();
        partialUpdatedBss.setId(bss.getId());

        partialUpdatedBss
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .hwVersion(UPDATED_HW_VERSION)
            .swVersion(UPDATED_SW_VERSION)
            .manufactureDate(UPDATED_MANUFACTURE_DATE)
            .lon(UPDATED_LON)
            .lat(UPDATED_LAT)
            .typeCode(UPDATED_TYPE_CODE)
            .cabNum(UPDATED_CAB_NUM)
            .cabEmptyNum(UPDATED_CAB_EMPTY_NUM)
            .bpReadyNum(UPDATED_BP_READY_NUM)
            .swapBpNo(UPDATED_SWAP_BP_NO);

        restBssMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBss.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBss))
            )
            .andExpect(status().isOk());

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
        Bss testBss = bssList.get(bssList.size() - 1);
        assertThat(testBss.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBss.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testBss.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testBss.getHwVersion()).isEqualTo(UPDATED_HW_VERSION);
        assertThat(testBss.getSwVersion()).isEqualTo(UPDATED_SW_VERSION);
        assertThat(testBss.getManufactureDate()).isEqualTo(UPDATED_MANUFACTURE_DATE);
        assertThat(testBss.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testBss.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testBss.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testBss.getCabNum()).isEqualTo(UPDATED_CAB_NUM);
        assertThat(testBss.getCabEmptyNum()).isEqualTo(UPDATED_CAB_EMPTY_NUM);
        assertThat(testBss.getBpReadyNum()).isEqualTo(UPDATED_BP_READY_NUM);
        assertThat(testBss.getSwapBpNo()).isEqualTo(UPDATED_SWAP_BP_NO);
    }

    @Test
    @Transactional
    void patchNonExistingBss() throws Exception {
        int databaseSizeBeforeUpdate = bssRepository.findAll().size();
        bss.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBssMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bss.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bss))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBss() throws Exception {
        int databaseSizeBeforeUpdate = bssRepository.findAll().size();
        bss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBssMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bss))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBss() throws Exception {
        int databaseSizeBeforeUpdate = bssRepository.findAll().size();
        bss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBssMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bss)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBss() throws Exception {
        // Initialize the database
        bssRepository.saveAndFlush(bss);

        int databaseSizeBeforeDelete = bssRepository.findAll().size();

        // Delete the bss
        restBssMockMvc.perform(delete(ENTITY_API_URL_ID, bss.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bss> bssList = bssRepository.findAll();
        assertThat(bssList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
