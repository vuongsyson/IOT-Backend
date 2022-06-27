package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.SosType;
import com.sonpj.repository.EntityManager;
import com.sonpj.repository.SosTypeRepository;
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
 * Integration tests for the {@link SosTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SosType.class).block();
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
        sosType = createEntity(em);
    }

    @Test
    void createSosType() throws Exception {
        int databaseSizeBeforeCreate = sosTypeRepository.findAll().collectList().block().size();
        // Create the SosType
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosType))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll().collectList().block();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SosType testSosType = sosTypeList.get(sosTypeList.size() - 1);
        assertThat(testSosType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createSosTypeWithExistingId() throws Exception {
        // Create the SosType with an existing ID
        sosType.setId(1L);

        int databaseSizeBeforeCreate = sosTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll().collectList().block();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sosTypeRepository.findAll().collectList().block().size();
        // set the field null
        sosType.setName(null);

        // Create the SosType, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SosType> sosTypeList = sosTypeRepository.findAll().collectList().block();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllSosTypesAsStream() {
        // Initialize the database
        sosTypeRepository.save(sosType).block();

        List<SosType> sosTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(SosType.class)
            .getResponseBody()
            .filter(sosType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(sosTypeList).isNotNull();
        assertThat(sosTypeList).hasSize(1);
        SosType testSosType = sosTypeList.get(0);
        assertThat(testSosType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllSosTypes() {
        // Initialize the database
        sosTypeRepository.save(sosType).block();

        // Get all the sosTypeList
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
            .value(hasItem(sosType.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getSosType() {
        // Initialize the database
        sosTypeRepository.save(sosType).block();

        // Get the sosType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sosType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sosType.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingSosType() {
        // Get the sosType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSosType() throws Exception {
        // Initialize the database
        sosTypeRepository.save(sosType).block();

        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().collectList().block().size();

        // Update the sosType
        SosType updatedSosType = sosTypeRepository.findById(sosType.getId()).block();
        updatedSosType.name(UPDATED_NAME);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSosType.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedSosType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll().collectList().block();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
        SosType testSosType = sosTypeList.get(sosTypeList.size() - 1);
        assertThat(testSosType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingSosType() throws Exception {
        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().collectList().block().size();
        sosType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sosType.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll().collectList().block();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSosType() throws Exception {
        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().collectList().block().size();
        sosType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll().collectList().block();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSosType() throws Exception {
        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().collectList().block().size();
        sosType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosType))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll().collectList().block();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSosTypeWithPatch() throws Exception {
        // Initialize the database
        sosTypeRepository.save(sosType).block();

        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().collectList().block().size();

        // Update the sosType using partial update
        SosType partialUpdatedSosType = new SosType();
        partialUpdatedSosType.setId(sosType.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSosType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSosType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll().collectList().block();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
        SosType testSosType = sosTypeList.get(sosTypeList.size() - 1);
        assertThat(testSosType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void fullUpdateSosTypeWithPatch() throws Exception {
        // Initialize the database
        sosTypeRepository.save(sosType).block();

        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().collectList().block().size();

        // Update the sosType using partial update
        SosType partialUpdatedSosType = new SosType();
        partialUpdatedSosType.setId(sosType.getId());

        partialUpdatedSosType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSosType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSosType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll().collectList().block();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
        SosType testSosType = sosTypeList.get(sosTypeList.size() - 1);
        assertThat(testSosType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingSosType() throws Exception {
        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().collectList().block().size();
        sosType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sosType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll().collectList().block();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSosType() throws Exception {
        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().collectList().block().size();
        sosType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll().collectList().block();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSosType() throws Exception {
        int databaseSizeBeforeUpdate = sosTypeRepository.findAll().collectList().block().size();
        sosType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sosType))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SosType in the database
        List<SosType> sosTypeList = sosTypeRepository.findAll().collectList().block();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSosType() {
        // Initialize the database
        sosTypeRepository.save(sosType).block();

        int databaseSizeBeforeDelete = sosTypeRepository.findAll().collectList().block().size();

        // Delete the sosType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sosType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SosType> sosTypeList = sosTypeRepository.findAll().collectList().block();
        assertThat(sosTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
