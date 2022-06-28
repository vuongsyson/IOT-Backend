package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.BpSwapRecord;
import com.sonpj.repository.BpSwapRecordRepository;
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
 * Integration tests for the {@link BpSwapRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(BpSwapRecord.class).block();
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
        bpSwapRecord = createEntity(em);
    }

    @Test
    void createBpSwapRecord() throws Exception {
        int databaseSizeBeforeCreate = bpSwapRecordRepository.findAll().collectList().block().size();
        // Create the BpSwapRecord
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bpSwapRecord))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll().collectList().block();
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
    void createBpSwapRecordWithExistingId() throws Exception {
        // Create the BpSwapRecord with an existing ID
        bpSwapRecord.setId(1L);

        int databaseSizeBeforeCreate = bpSwapRecordRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bpSwapRecord))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll().collectList().block();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllBpSwapRecordsAsStream() {
        // Initialize the database
        bpSwapRecordRepository.save(bpSwapRecord).block();

        List<BpSwapRecord> bpSwapRecordList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(BpSwapRecord.class)
            .getResponseBody()
            .filter(bpSwapRecord::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(bpSwapRecordList).isNotNull();
        assertThat(bpSwapRecordList).hasSize(1);
        BpSwapRecord testBpSwapRecord = bpSwapRecordList.get(0);
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
    void getAllBpSwapRecords() {
        // Initialize the database
        bpSwapRecordRepository.save(bpSwapRecord).block();

        // Get all the bpSwapRecordList
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
            .value(hasItem(bpSwapRecord.getId().intValue()))
            .jsonPath("$.[*].oldBat")
            .value(hasItem(DEFAULT_OLD_BAT))
            .jsonPath("$.[*].newBat")
            .value(hasItem(DEFAULT_NEW_BAT))
            .jsonPath("$.[*].oldCab")
            .value(hasItem(DEFAULT_OLD_CAB))
            .jsonPath("$.[*].newCab")
            .value(hasItem(DEFAULT_NEW_CAB))
            .jsonPath("$.[*].bss")
            .value(hasItem(DEFAULT_BSS))
            .jsonPath("$.[*].user")
            .value(hasItem(DEFAULT_USER.intValue()))
            .jsonPath("$.[*].state")
            .value(hasItem(DEFAULT_STATE))
            .jsonPath("$.[*].error")
            .value(hasItem(DEFAULT_ERROR));
    }

    @Test
    void getBpSwapRecord() {
        // Initialize the database
        bpSwapRecordRepository.save(bpSwapRecord).block();

        // Get the bpSwapRecord
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, bpSwapRecord.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(bpSwapRecord.getId().intValue()))
            .jsonPath("$.oldBat")
            .value(is(DEFAULT_OLD_BAT))
            .jsonPath("$.newBat")
            .value(is(DEFAULT_NEW_BAT))
            .jsonPath("$.oldCab")
            .value(is(DEFAULT_OLD_CAB))
            .jsonPath("$.newCab")
            .value(is(DEFAULT_NEW_CAB))
            .jsonPath("$.bss")
            .value(is(DEFAULT_BSS))
            .jsonPath("$.user")
            .value(is(DEFAULT_USER.intValue()))
            .jsonPath("$.state")
            .value(is(DEFAULT_STATE))
            .jsonPath("$.error")
            .value(is(DEFAULT_ERROR));
    }

    @Test
    void getNonExistingBpSwapRecord() {
        // Get the bpSwapRecord
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewBpSwapRecord() throws Exception {
        // Initialize the database
        bpSwapRecordRepository.save(bpSwapRecord).block();

        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().collectList().block().size();

        // Update the bpSwapRecord
        BpSwapRecord updatedBpSwapRecord = bpSwapRecordRepository.findById(bpSwapRecord.getId()).block();
        updatedBpSwapRecord
            .oldBat(UPDATED_OLD_BAT)
            .newBat(UPDATED_NEW_BAT)
            .oldCab(UPDATED_OLD_CAB)
            .newCab(UPDATED_NEW_CAB)
            .bss(UPDATED_BSS)
            .user(UPDATED_USER)
            .state(UPDATED_STATE)
            .error(UPDATED_ERROR);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedBpSwapRecord.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedBpSwapRecord))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll().collectList().block();
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
    void putNonExistingBpSwapRecord() throws Exception {
        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().collectList().block().size();
        bpSwapRecord.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, bpSwapRecord.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bpSwapRecord))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll().collectList().block();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBpSwapRecord() throws Exception {
        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().collectList().block().size();
        bpSwapRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bpSwapRecord))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll().collectList().block();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBpSwapRecord() throws Exception {
        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().collectList().block().size();
        bpSwapRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bpSwapRecord))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll().collectList().block();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBpSwapRecordWithPatch() throws Exception {
        // Initialize the database
        bpSwapRecordRepository.save(bpSwapRecord).block();

        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().collectList().block().size();

        // Update the bpSwapRecord using partial update
        BpSwapRecord partialUpdatedBpSwapRecord = new BpSwapRecord();
        partialUpdatedBpSwapRecord.setId(bpSwapRecord.getId());

        partialUpdatedBpSwapRecord.newBat(UPDATED_NEW_BAT).user(UPDATED_USER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBpSwapRecord.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBpSwapRecord))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll().collectList().block();
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
    void fullUpdateBpSwapRecordWithPatch() throws Exception {
        // Initialize the database
        bpSwapRecordRepository.save(bpSwapRecord).block();

        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().collectList().block().size();

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

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBpSwapRecord.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBpSwapRecord))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll().collectList().block();
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
    void patchNonExistingBpSwapRecord() throws Exception {
        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().collectList().block().size();
        bpSwapRecord.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, bpSwapRecord.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bpSwapRecord))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll().collectList().block();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBpSwapRecord() throws Exception {
        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().collectList().block().size();
        bpSwapRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bpSwapRecord))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll().collectList().block();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBpSwapRecord() throws Exception {
        int databaseSizeBeforeUpdate = bpSwapRecordRepository.findAll().collectList().block().size();
        bpSwapRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bpSwapRecord))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BpSwapRecord in the database
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll().collectList().block();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBpSwapRecord() {
        // Initialize the database
        bpSwapRecordRepository.save(bpSwapRecord).block();

        int databaseSizeBeforeDelete = bpSwapRecordRepository.findAll().collectList().block().size();

        // Delete the bpSwapRecord
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, bpSwapRecord.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<BpSwapRecord> bpSwapRecordList = bpSwapRecordRepository.findAll().collectList().block();
        assertThat(bpSwapRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
