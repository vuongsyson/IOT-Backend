package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.DeviceType;
import com.sonpj.repository.DeviceTypeRepository;
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
 * Integration tests for the {@link DeviceTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/device-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeviceTypeRepository deviceTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceTypeMockMvc;

    private DeviceType deviceType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceType createEntity(EntityManager em) {
        DeviceType deviceType = new DeviceType().name(DEFAULT_NAME);
        return deviceType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceType createUpdatedEntity(EntityManager em) {
        DeviceType deviceType = new DeviceType().name(UPDATED_NAME);
        return deviceType;
    }

    @BeforeEach
    public void initTest() {
        deviceType = createEntity(em);
    }

    @Test
    @Transactional
    void createDeviceType() throws Exception {
        int databaseSizeBeforeCreate = deviceTypeRepository.findAll().size();
        // Create the DeviceType
        restDeviceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceType)))
            .andExpect(status().isCreated());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createDeviceTypeWithExistingId() throws Exception {
        // Create the DeviceType with an existing ID
        deviceType.setId(1L);

        int databaseSizeBeforeCreate = deviceTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceType)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceTypeRepository.findAll().size();
        // set the field null
        deviceType.setName(null);

        // Create the DeviceType, which fails.

        restDeviceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceType)))
            .andExpect(status().isBadRequest());

        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeviceTypes() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        // Get all the deviceTypeList
        restDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getDeviceType() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        // Get the deviceType
        restDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, deviceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deviceType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingDeviceType() throws Exception {
        // Get the deviceType
        restDeviceTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeviceType() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();

        // Update the deviceType
        DeviceType updatedDeviceType = deviceTypeRepository.findById(deviceType.getId()).get();
        // Disconnect from session so that the updates on updatedDeviceType are not directly saved in db
        em.detach(updatedDeviceType);
        updatedDeviceType.name(UPDATED_NAME);

        restDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeviceType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeviceType))
            )
            .andExpect(status().isOk());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceTypeWithPatch() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();

        // Update the deviceType using partial update
        DeviceType partialUpdatedDeviceType = new DeviceType();
        partialUpdatedDeviceType.setId(deviceType.getId());

        partialUpdatedDeviceType.name(UPDATED_NAME);

        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceType))
            )
            .andExpect(status().isOk());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDeviceTypeWithPatch() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();

        // Update the deviceType using partial update
        DeviceType partialUpdatedDeviceType = new DeviceType();
        partialUpdatedDeviceType.setId(deviceType.getId());

        partialUpdatedDeviceType.name(UPDATED_NAME);

        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceType))
            )
            .andExpect(status().isOk());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deviceType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeviceType() throws Exception {
        // Initialize the database
        deviceTypeRepository.saveAndFlush(deviceType);

        int databaseSizeBeforeDelete = deviceTypeRepository.findAll().size();

        // Delete the deviceType
        restDeviceTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, deviceType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
