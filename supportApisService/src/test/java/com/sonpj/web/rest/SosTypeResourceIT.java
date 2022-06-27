package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.SosType;
import com.sonpj.repository.SosTypeRepository;
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
 * Integration tests for the {@link SosTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SosTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sos-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SosTypeRepository sosTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSosTypeMockMvc;

    private SosType sosType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SosType createEntity(EntityManager em) {
        SosType sosType = new SosType().name(DEFAULT_NAME);
        return sosType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SosType createUpdatedEntity(EntityManager em) {
        SosType sosType = new SosType().name(UPDATED_NAME);
        return sosType;
    }

    @BeforeEach
    public void initTest() {
        sosType = createEntity(em);
    }

    @Test
    @Transactional
    void createSosType() throws Exception {
        int databaseSizeBeforeCreate = sosTypeRepository.findAll().size();
        // Create the SosType
        restSosTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sosType)))
            .andExpect(status().isCreated());

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SosType testSosType = sosTypeList.get(sosTypeList.size() - 1);
        assertThat(testSosType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSosTypeWithExistingId() throws Exception {
        // Create the SosType with an existing ID
        sosType.setId(1L);

        int databaseSizeBeforeCreate = sosTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSosTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sosType)))
            .andExpect(status().isBadRequest());

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sosTypeRepository.findAll().size();
        // set the field null
        sosType.setName(null);

        // Create the SosType, which fails.

        restSosTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sosType)))
            .andExpect(status().isBadRequest());

        List<SosType> sosTypeList = sosTypeRepository.findAll();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSosTypes() throws Exception {
        // Initialize the database
        sosTypeRepository.saveAndFlush(sosType);

        // Get all the sosTypeList
        restSosTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sosType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSosType() throws Exception {
        // Initialize the database
        sosTypeRepository.saveAndFlush(sosType);

        // Get the sosType
        restSosTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, sosType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sosType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSosType() throws Exception {
        // Get the sosType
        restSosTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSosType() throws Exception {
        // Initialize the database
        sosTypeRepository.saveAndFlush(sosType);

        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().size();

        // Update the sosType
        SosType updatedSosType = sosTypeRepository.findById(sosType.getId()).get();
        // Disconnect from session so that the updates on updatedSosType are not directly saved in db
        em.detach(updatedSosType);
        updatedSosType.name(UPDATED_NAME);

        restSosTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSosType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSosType))
            )
            .andExpect(status().isOk());

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
        SosType testSosType = sosTypeList.get(sosTypeList.size() - 1);
        assertThat(testSosType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSosType() throws Exception {
        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().size();
        sosType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSosTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sosType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sosType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSosType() throws Exception {
        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().size();
        sosType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSosTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sosType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSosType() throws Exception {
        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().size();
        sosType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSosTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sosType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSosTypeWithPatch() throws Exception {
        // Initialize the database
        sosTypeRepository.saveAndFlush(sosType);

        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().size();

        // Update the sosType using partial update
        SosType partialUpdatedSosType = new SosType();
        partialUpdatedSosType.setId(sosType.getId());

        restSosTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSosType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSosType))
            )
            .andExpect(status().isOk());

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
        SosType testSosType = sosTypeList.get(sosTypeList.size() - 1);
        assertThat(testSosType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSosTypeWithPatch() throws Exception {
        // Initialize the database
        sosTypeRepository.saveAndFlush(sosType);

        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().size();

        // Update the sosType using partial update
        SosType partialUpdatedSosType = new SosType();
        partialUpdatedSosType.setId(sosType.getId());

        partialUpdatedSosType.name(UPDATED_NAME);

        restSosTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSosType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSosType))
            )
            .andExpect(status().isOk());

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
        SosType testSosType = sosTypeList.get(sosTypeList.size() - 1);
        assertThat(testSosType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSosType() throws Exception {
        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().size();
        sosType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSosTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sosType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sosType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSosType() throws Exception {
        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().size();
        sosType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSosTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sosType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSosType() throws Exception {
        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().size();
        sosType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSosTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sosType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSosType() throws Exception {
        // Initialize the database
        sosTypeRepository.saveAndFlush(sosType);

        int databaseSizeBeforeDelete = sosTypeRepository.findAll().size();

        // Delete the sosType
        restSosTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, sosType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SosType> sosTypeList = sosTypeRepository.findAll();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
