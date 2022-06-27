package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.RefreshToken;
import com.sonpj.repository.RefreshTokenRepository;
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
 * Integration tests for the {@link RefreshTokenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RefreshTokenResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/refresh-tokens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRefreshTokenMockMvc;

    private RefreshToken refreshToken;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RefreshToken createEntity(EntityManager em) {
        RefreshToken refreshToken = new RefreshToken().username(DEFAULT_USERNAME);
        return refreshToken;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RefreshToken createUpdatedEntity(EntityManager em) {
        RefreshToken refreshToken = new RefreshToken().username(UPDATED_USERNAME);
        return refreshToken;
    }

    @BeforeEach
    public void initTest() {
        refreshToken = createEntity(em);
    }

    @Test
    @Transactional
    void createRefreshToken() throws Exception {
        int databaseSizeBeforeCreate = refreshTokenRepository.findAll().size();
        // Create the RefreshToken
        restRefreshTokenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refreshToken)))
            .andExpect(status().isCreated());

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeCreate + 1);
        RefreshToken testRefreshToken = refreshTokenList.get(refreshTokenList.size() - 1);
        assertThat(testRefreshToken.getUsername()).isEqualTo(DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void createRefreshTokenWithExistingId() throws Exception {
        // Create the RefreshToken with an existing ID
        refreshToken.setId(1L);

        int databaseSizeBeforeCreate = refreshTokenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRefreshTokenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refreshToken)))
            .andExpect(status().isBadRequest());

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRefreshTokens() throws Exception {
        // Initialize the database
        refreshTokenRepository.saveAndFlush(refreshToken);

        // Get all the refreshTokenList
        restRefreshTokenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(refreshToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)));
    }

    @Test
    @Transactional
    void getRefreshToken() throws Exception {
        // Initialize the database
        refreshTokenRepository.saveAndFlush(refreshToken);

        // Get the refreshToken
        restRefreshTokenMockMvc
            .perform(get(ENTITY_API_URL_ID, refreshToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(refreshToken.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME));
    }

    @Test
    @Transactional
    void getNonExistingRefreshToken() throws Exception {
        // Get the refreshToken
        restRefreshTokenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRefreshToken() throws Exception {
        // Initialize the database
        refreshTokenRepository.saveAndFlush(refreshToken);

        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().size();

        // Update the refreshToken
        RefreshToken updatedRefreshToken = refreshTokenRepository.findById(refreshToken.getId()).get();
        // Disconnect from session so that the updates on updatedRefreshToken are not directly saved in db
        em.detach(updatedRefreshToken);
        updatedRefreshToken.username(UPDATED_USERNAME);

        restRefreshTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRefreshToken.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRefreshToken))
            )
            .andExpect(status().isOk());

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
        RefreshToken testRefreshToken = refreshTokenList.get(refreshTokenList.size() - 1);
        assertThat(testRefreshToken.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void putNonExistingRefreshToken() throws Exception {
        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().size();
        refreshToken.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefreshTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, refreshToken.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refreshToken))
            )
            .andExpect(status().isBadRequest());

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRefreshToken() throws Exception {
        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().size();
        refreshToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefreshTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refreshToken))
            )
            .andExpect(status().isBadRequest());

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRefreshToken() throws Exception {
        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().size();
        refreshToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefreshTokenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refreshToken)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRefreshTokenWithPatch() throws Exception {
        // Initialize the database
        refreshTokenRepository.saveAndFlush(refreshToken);

        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().size();

        // Update the refreshToken using partial update
        RefreshToken partialUpdatedRefreshToken = new RefreshToken();
        partialUpdatedRefreshToken.setId(refreshToken.getId());

        restRefreshTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRefreshToken.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRefreshToken))
            )
            .andExpect(status().isOk());

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
        RefreshToken testRefreshToken = refreshTokenList.get(refreshTokenList.size() - 1);
        assertThat(testRefreshToken.getUsername()).isEqualTo(DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void fullUpdateRefreshTokenWithPatch() throws Exception {
        // Initialize the database
        refreshTokenRepository.saveAndFlush(refreshToken);

        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().size();

        // Update the refreshToken using partial update
        RefreshToken partialUpdatedRefreshToken = new RefreshToken();
        partialUpdatedRefreshToken.setId(refreshToken.getId());

        partialUpdatedRefreshToken.username(UPDATED_USERNAME);

        restRefreshTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRefreshToken.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRefreshToken))
            )
            .andExpect(status().isOk());

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
        RefreshToken testRefreshToken = refreshTokenList.get(refreshTokenList.size() - 1);
        assertThat(testRefreshToken.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void patchNonExistingRefreshToken() throws Exception {
        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().size();
        refreshToken.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefreshTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, refreshToken.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(refreshToken))
            )
            .andExpect(status().isBadRequest());

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRefreshToken() throws Exception {
        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().size();
        refreshToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefreshTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(refreshToken))
            )
            .andExpect(status().isBadRequest());

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRefreshToken() throws Exception {
        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().size();
        refreshToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefreshTokenMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(refreshToken))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRefreshToken() throws Exception {
        // Initialize the database
        refreshTokenRepository.saveAndFlush(refreshToken);

        int databaseSizeBeforeDelete = refreshTokenRepository.findAll().size();

        // Delete the refreshToken
        restRefreshTokenMockMvc
            .perform(delete(ENTITY_API_URL_ID, refreshToken.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
