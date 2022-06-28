package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.Cabinet;
import com.sonpj.repository.CabinetRepository;
import com.sonpj.repository.EntityManager;
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
 * Integration tests for the {@link CabinetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Cabinet.class).block();
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
        cabinet = createEntity(em);
    }

    @Test
    void createCabinet() throws Exception {
        int databaseSizeBeforeCreate = cabinetRepository.findAll().collectList().block().size();
        // Create the Cabinet
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cabinet))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll().collectList().block();
        assertThat(cabinetList).hasSize(databaseSizeBeforeCreate + 1);
        Cabinet testCabinet = cabinetList.get(cabinetList.size() - 1);
        assertThat(testCabinet.getBssId()).isEqualTo(DEFAULT_BSS_ID);
        assertThat(testCabinet.getBpId()).isEqualTo(DEFAULT_BP_ID);
        assertThat(testCabinet.getBpReady()).isEqualTo(DEFAULT_BP_READY);
        assertThat(testCabinet.getSwapNo()).isEqualTo(DEFAULT_SWAP_NO);
        assertThat(testCabinet.getStateCode()).isEqualTo(DEFAULT_STATE_CODE);
    }

    @Test
    void createCabinetWithExistingId() throws Exception {
        // Create the Cabinet with an existing ID
        cabinet.setId(1L);

        int databaseSizeBeforeCreate = cabinetRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cabinet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll().collectList().block();
        assertThat(cabinetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkBssIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = cabinetRepository.findAll().collectList().block().size();
        // set the field null
        cabinet.setBssId(null);

        // Create the Cabinet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cabinet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Cabinet> cabinetList = cabinetRepository.findAll().collectList().block();
        assertThat(cabinetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCabinetsAsStream() {
        // Initialize the database
        cabinetRepository.save(cabinet).block();

        List<Cabinet> cabinetList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Cabinet.class)
            .getResponseBody()
            .filter(cabinet::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(cabinetList).isNotNull();
        assertThat(cabinetList).hasSize(1);
        Cabinet testCabinet = cabinetList.get(0);
        assertThat(testCabinet.getBssId()).isEqualTo(DEFAULT_BSS_ID);
        assertThat(testCabinet.getBpId()).isEqualTo(DEFAULT_BP_ID);
        assertThat(testCabinet.getBpReady()).isEqualTo(DEFAULT_BP_READY);
        assertThat(testCabinet.getSwapNo()).isEqualTo(DEFAULT_SWAP_NO);
        assertThat(testCabinet.getStateCode()).isEqualTo(DEFAULT_STATE_CODE);
    }

    @Test
    void getAllCabinets() {
        // Initialize the database
        cabinetRepository.save(cabinet).block();

        // Get all the cabinetList
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
            .value(hasItem(cabinet.getId().intValue()))
            .jsonPath("$.[*].bssId")
            .value(hasItem(DEFAULT_BSS_ID.intValue()))
            .jsonPath("$.[*].bpId")
            .value(hasItem(DEFAULT_BP_ID.intValue()))
            .jsonPath("$.[*].bpReady")
            .value(hasItem(DEFAULT_BP_READY.booleanValue()))
            .jsonPath("$.[*].swapNo")
            .value(hasItem(DEFAULT_SWAP_NO.intValue()))
            .jsonPath("$.[*].stateCode")
            .value(hasItem(DEFAULT_STATE_CODE));
    }

    @Test
    void getCabinet() {
        // Initialize the database
        cabinetRepository.save(cabinet).block();

        // Get the cabinet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, cabinet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(cabinet.getId().intValue()))
            .jsonPath("$.bssId")
            .value(is(DEFAULT_BSS_ID.intValue()))
            .jsonPath("$.bpId")
            .value(is(DEFAULT_BP_ID.intValue()))
            .jsonPath("$.bpReady")
            .value(is(DEFAULT_BP_READY.booleanValue()))
            .jsonPath("$.swapNo")
            .value(is(DEFAULT_SWAP_NO.intValue()))
            .jsonPath("$.stateCode")
            .value(is(DEFAULT_STATE_CODE));
    }

    @Test
    void getNonExistingCabinet() {
        // Get the cabinet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCabinet() throws Exception {
        // Initialize the database
        cabinetRepository.save(cabinet).block();

        int databaseSizeBeforeUpdate = cabinetRepository.findAll().collectList().block().size();

        // Update the cabinet
        Cabinet updatedCabinet = cabinetRepository.findById(cabinet.getId()).block();
        updatedCabinet
            .bssId(UPDATED_BSS_ID)
            .bpId(UPDATED_BP_ID)
            .bpReady(UPDATED_BP_READY)
            .swapNo(UPDATED_SWAP_NO)
            .stateCode(UPDATED_STATE_CODE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCabinet.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCabinet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll().collectList().block();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
        Cabinet testCabinet = cabinetList.get(cabinetList.size() - 1);
        assertThat(testCabinet.getBssId()).isEqualTo(UPDATED_BSS_ID);
        assertThat(testCabinet.getBpId()).isEqualTo(UPDATED_BP_ID);
        assertThat(testCabinet.getBpReady()).isEqualTo(UPDATED_BP_READY);
        assertThat(testCabinet.getSwapNo()).isEqualTo(UPDATED_SWAP_NO);
        assertThat(testCabinet.getStateCode()).isEqualTo(UPDATED_STATE_CODE);
    }

    @Test
    void putNonExistingCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().collectList().block().size();
        cabinet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cabinet.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cabinet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll().collectList().block();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().collectList().block().size();
        cabinet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cabinet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll().collectList().block();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().collectList().block().size();
        cabinet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cabinet))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll().collectList().block();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCabinetWithPatch() throws Exception {
        // Initialize the database
        cabinetRepository.save(cabinet).block();

        int databaseSizeBeforeUpdate = cabinetRepository.findAll().collectList().block().size();

        // Update the cabinet using partial update
        Cabinet partialUpdatedCabinet = new Cabinet();
        partialUpdatedCabinet.setId(cabinet.getId());

        partialUpdatedCabinet.bpReady(UPDATED_BP_READY).swapNo(UPDATED_SWAP_NO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCabinet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCabinet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll().collectList().block();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
        Cabinet testCabinet = cabinetList.get(cabinetList.size() - 1);
        assertThat(testCabinet.getBssId()).isEqualTo(DEFAULT_BSS_ID);
        assertThat(testCabinet.getBpId()).isEqualTo(DEFAULT_BP_ID);
        assertThat(testCabinet.getBpReady()).isEqualTo(UPDATED_BP_READY);
        assertThat(testCabinet.getSwapNo()).isEqualTo(UPDATED_SWAP_NO);
        assertThat(testCabinet.getStateCode()).isEqualTo(DEFAULT_STATE_CODE);
    }

    @Test
    void fullUpdateCabinetWithPatch() throws Exception {
        // Initialize the database
        cabinetRepository.save(cabinet).block();

        int databaseSizeBeforeUpdate = cabinetRepository.findAll().collectList().block().size();

        // Update the cabinet using partial update
        Cabinet partialUpdatedCabinet = new Cabinet();
        partialUpdatedCabinet.setId(cabinet.getId());

        partialUpdatedCabinet
            .bssId(UPDATED_BSS_ID)
            .bpId(UPDATED_BP_ID)
            .bpReady(UPDATED_BP_READY)
            .swapNo(UPDATED_SWAP_NO)
            .stateCode(UPDATED_STATE_CODE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCabinet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCabinet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll().collectList().block();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
        Cabinet testCabinet = cabinetList.get(cabinetList.size() - 1);
        assertThat(testCabinet.getBssId()).isEqualTo(UPDATED_BSS_ID);
        assertThat(testCabinet.getBpId()).isEqualTo(UPDATED_BP_ID);
        assertThat(testCabinet.getBpReady()).isEqualTo(UPDATED_BP_READY);
        assertThat(testCabinet.getSwapNo()).isEqualTo(UPDATED_SWAP_NO);
        assertThat(testCabinet.getStateCode()).isEqualTo(UPDATED_STATE_CODE);
    }

    @Test
    void patchNonExistingCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().collectList().block().size();
        cabinet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, cabinet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cabinet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll().collectList().block();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().collectList().block().size();
        cabinet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cabinet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll().collectList().block();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().collectList().block().size();
        cabinet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cabinet))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll().collectList().block();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCabinet() {
        // Initialize the database
        cabinetRepository.save(cabinet).block();

        int databaseSizeBeforeDelete = cabinetRepository.findAll().collectList().block().size();

        // Delete the cabinet
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, cabinet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Cabinet> cabinetList = cabinetRepository.findAll().collectList().block();
        assertThat(cabinetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
