package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.Org;
import com.sonpj.repository.OrgRepository;
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
 * Integration tests for the {@link OrgResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrgResourceIT {

    private static final Long DEFAULT_ORG_ID = 1L;
    private static final Long UPDATED_ORG_ID = 2L;

    private static final String ENTITY_API_URL = "/api/orgs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrgMockMvc;

    private Org org;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Org createEntity(EntityManager em) {
        Org org = new Org().orgId(DEFAULT_ORG_ID);
        return org;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Org createUpdatedEntity(EntityManager em) {
        Org org = new Org().orgId(UPDATED_ORG_ID);
        return org;
    }

    @BeforeEach
    public void initTest() {
        org = createEntity(em);
    }

    @Test
    @Transactional
    void createOrg() throws Exception {
        int databaseSizeBeforeCreate = orgRepository.findAll().size();
        // Create the Org
        restOrgMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(org)))
            .andExpect(status().isCreated());

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll();
        assertThat(orgList).hasSize(databaseSizeBeforeCreate + 1);
        Org testOrg = orgList.get(orgList.size() - 1);
        assertThat(testOrg.getOrgId()).isEqualTo(DEFAULT_ORG_ID);
    }

    @Test
    @Transactional
    void createOrgWithExistingId() throws Exception {
        // Create the Org with an existing ID
        org.setId(1L);

        int databaseSizeBeforeCreate = orgRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(org)))
            .andExpect(status().isBadRequest());

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll();
        assertThat(orgList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrgs() throws Exception {
        // Initialize the database
        orgRepository.saveAndFlush(org);

        // Get all the orgList
        restOrgMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(org.getId().intValue())))
            .andExpect(jsonPath("$.[*].orgId").value(hasItem(DEFAULT_ORG_ID.intValue())));
    }

    @Test
    @Transactional
    void getOrg() throws Exception {
        // Initialize the database
        orgRepository.saveAndFlush(org);

        // Get the org
        restOrgMockMvc
            .perform(get(ENTITY_API_URL_ID, org.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(org.getId().intValue()))
            .andExpect(jsonPath("$.orgId").value(DEFAULT_ORG_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrg() throws Exception {
        // Get the org
        restOrgMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrg() throws Exception {
        // Initialize the database
        orgRepository.saveAndFlush(org);

        int databaseSizeBeforeUpdate = orgRepository.findAll().size();

        // Update the org
        Org updatedOrg = orgRepository.findById(org.getId()).get();
        // Disconnect from session so that the updates on updatedOrg are not directly saved in db
        em.detach(updatedOrg);
        updatedOrg.orgId(UPDATED_ORG_ID);

        restOrgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrg.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrg))
            )
            .andExpect(status().isOk());

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
        Org testOrg = orgList.get(orgList.size() - 1);
        assertThat(testOrg.getOrgId()).isEqualTo(UPDATED_ORG_ID);
    }

    @Test
    @Transactional
    void putNonExistingOrg() throws Exception {
        int databaseSizeBeforeUpdate = orgRepository.findAll().size();
        org.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, org.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(org))
            )
            .andExpect(status().isBadRequest());

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrg() throws Exception {
        int databaseSizeBeforeUpdate = orgRepository.findAll().size();
        org.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(org))
            )
            .andExpect(status().isBadRequest());

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrg() throws Exception {
        int databaseSizeBeforeUpdate = orgRepository.findAll().size();
        org.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(org)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrgWithPatch() throws Exception {
        // Initialize the database
        orgRepository.saveAndFlush(org);

        int databaseSizeBeforeUpdate = orgRepository.findAll().size();

        // Update the org using partial update
        Org partialUpdatedOrg = new Org();
        partialUpdatedOrg.setId(org.getId());

        partialUpdatedOrg.orgId(UPDATED_ORG_ID);

        restOrgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrg.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrg))
            )
            .andExpect(status().isOk());

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
        Org testOrg = orgList.get(orgList.size() - 1);
        assertThat(testOrg.getOrgId()).isEqualTo(UPDATED_ORG_ID);
    }

    @Test
    @Transactional
    void fullUpdateOrgWithPatch() throws Exception {
        // Initialize the database
        orgRepository.saveAndFlush(org);

        int databaseSizeBeforeUpdate = orgRepository.findAll().size();

        // Update the org using partial update
        Org partialUpdatedOrg = new Org();
        partialUpdatedOrg.setId(org.getId());

        partialUpdatedOrg.orgId(UPDATED_ORG_ID);

        restOrgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrg.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrg))
            )
            .andExpect(status().isOk());

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
        Org testOrg = orgList.get(orgList.size() - 1);
        assertThat(testOrg.getOrgId()).isEqualTo(UPDATED_ORG_ID);
    }

    @Test
    @Transactional
    void patchNonExistingOrg() throws Exception {
        int databaseSizeBeforeUpdate = orgRepository.findAll().size();
        org.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, org.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(org))
            )
            .andExpect(status().isBadRequest());

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrg() throws Exception {
        int databaseSizeBeforeUpdate = orgRepository.findAll().size();
        org.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(org))
            )
            .andExpect(status().isBadRequest());

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrg() throws Exception {
        int databaseSizeBeforeUpdate = orgRepository.findAll().size();
        org.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(org)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrg() throws Exception {
        // Initialize the database
        orgRepository.saveAndFlush(org);

        int databaseSizeBeforeDelete = orgRepository.findAll().size();

        // Delete the org
        restOrgMockMvc.perform(delete(ENTITY_API_URL_ID, org.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Org> orgList = orgRepository.findAll();
        assertThat(orgList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
