package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.RefreshToken;
import com.sonpj.repository.EntityManager;
import com.sonpj.repository.RefreshTokenRepository;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link RefreshTokenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(RefreshToken.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        refreshToken = createEntity(em);
    }

    @Test
    void createRefreshToken() throws Exception {
        int databaseSizeBeforeCreate = refreshTokenRepository.findAll().collectList().block().size();
        // Create the RefreshToken
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(refreshToken))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll().collectList().block();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeCreate + 1);
        RefreshToken testRefreshToken = refreshTokenList.get(refreshTokenList.size() - 1);
        assertThat(testRefreshToken.getUsername()).isEqualTo(DEFAULT_USERNAME);
    }

    @Test
    void createRefreshTokenWithExistingId() throws Exception {
        // Create the RefreshToken with an existing ID
        refreshToken.setId(1L);

        int databaseSizeBeforeCreate = refreshTokenRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(refreshToken))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll().collectList().block();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRefreshTokensAsStream() {
        // Initialize the database
        refreshTokenRepository.save(refreshToken).block();

        List<RefreshToken> refreshTokenList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(RefreshToken.class)
            .getResponseBody()
            .filter(refreshToken::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(refreshTokenList).isNotNull();
        assertThat(refreshTokenList).hasSize(1);
        RefreshToken testRefreshToken = refreshTokenList.get(0);
        assertThat(testRefreshToken.getUsername()).isEqualTo(DEFAULT_USERNAME);
    }

    @Test
    void getAllRefreshTokens() {
        // Initialize the database
        refreshTokenRepository.save(refreshToken).block();

        // Get all the refreshTokenList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(refreshToken.getId().intValue()))
            .jsonPath("$.[*].username")
            .value(hasItem(DEFAULT_USERNAME));
    }

    @Test
    void getRefreshToken() {
        // Initialize the database
        refreshTokenRepository.save(refreshToken).block();

        // Get the refreshToken
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, refreshToken.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(refreshToken.getId().intValue()))
            .jsonPath("$.username")
            .value(is(DEFAULT_USERNAME));
    }

    @Test
    void getNonExistingRefreshToken() {
        // Get the refreshToken
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewRefreshToken() throws Exception {
        // Initialize the database
        refreshTokenRepository.save(refreshToken).block();

        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().collectList().block().size();

        // Update the refreshToken
        RefreshToken updatedRefreshToken = refreshTokenRepository.findById(refreshToken.getId()).block();
        updatedRefreshToken.username(UPDATED_USERNAME);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedRefreshToken.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedRefreshToken))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll().collectList().block();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
        RefreshToken testRefreshToken = refreshTokenList.get(refreshTokenList.size() - 1);
        assertThat(testRefreshToken.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    void putNonExistingRefreshToken() throws Exception {
        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().collectList().block().size();
        refreshToken.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, refreshToken.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(refreshToken))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll().collectList().block();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRefreshToken() throws Exception {
        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().collectList().block().size();
        refreshToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(refreshToken))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll().collectList().block();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRefreshToken() throws Exception {
        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().collectList().block().size();
        refreshToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(refreshToken))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll().collectList().block();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRefreshTokenWithPatch() throws Exception {
        // Initialize the database
        refreshTokenRepository.save(refreshToken).block();

        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().collectList().block().size();

        // Update the refreshToken using partial update
        RefreshToken partialUpdatedRefreshToken = new RefreshToken();
        partialUpdatedRefreshToken.setId(refreshToken.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRefreshToken.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRefreshToken))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll().collectList().block();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
        RefreshToken testRefreshToken = refreshTokenList.get(refreshTokenList.size() - 1);
        assertThat(testRefreshToken.getUsername()).isEqualTo(DEFAULT_USERNAME);
    }

    @Test
    void fullUpdateRefreshTokenWithPatch() throws Exception {
        // Initialize the database
        refreshTokenRepository.save(refreshToken).block();

        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().collectList().block().size();

        // Update the refreshToken using partial update
        RefreshToken partialUpdatedRefreshToken = new RefreshToken();
        partialUpdatedRefreshToken.setId(refreshToken.getId());

        partialUpdatedRefreshToken.username(UPDATED_USERNAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRefreshToken.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRefreshToken))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll().collectList().block();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
        RefreshToken testRefreshToken = refreshTokenList.get(refreshTokenList.size() - 1);
        assertThat(testRefreshToken.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    void patchNonExistingRefreshToken() throws Exception {
        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().collectList().block().size();
        refreshToken.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, refreshToken.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(refreshToken))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll().collectList().block();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRefreshToken() throws Exception {
        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().collectList().block().size();
        refreshToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(refreshToken))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll().collectList().block();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRefreshToken() throws Exception {
        int databaseSizeBeforeUpdate = refreshTokenRepository.findAll().collectList().block().size();
        refreshToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(refreshToken))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RefreshToken in the database
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll().collectList().block();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRefreshToken() {
        // Initialize the database
        refreshTokenRepository.save(refreshToken).block();

        int databaseSizeBeforeDelete = refreshTokenRepository.findAll().collectList().block().size();

        // Delete the refreshToken
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, refreshToken.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll().collectList().block();
        assertThat(refreshTokenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
