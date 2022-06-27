package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.Bss;
import com.sonpj.repository.BssRepository;
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
 * Integration tests for the {@link BssResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Bss.class).block();
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
        bss = createEntity(em);
    }

    @Test
    void createBss() throws Exception {
        int databaseSizeBeforeCreate = bssRepository.findAll().collectList().block().size();
        // Create the Bss
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bss))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll().collectList().block();
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
    void createBssWithExistingId() throws Exception {
        // Create the Bss with an existing ID
        bss.setId(1L);

        int databaseSizeBeforeCreate = bssRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bss))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll().collectList().block();
        assertThat(bssList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllBssesAsStream() {
        // Initialize the database
        bssRepository.save(bss).block();

        List<Bss> bssList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Bss.class)
            .getResponseBody()
            .filter(bss::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(bssList).isNotNull();
        assertThat(bssList).hasSize(1);
        Bss testBss = bssList.get(0);
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
    void getAllBsses() {
        // Initialize the database
        bssRepository.save(bss).block();

        // Get all the bssList
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
            .value(hasItem(bss.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].address")
            .value(hasItem(DEFAULT_ADDRESS))
            .jsonPath("$.[*].serialNumber")
            .value(hasItem(DEFAULT_SERIAL_NUMBER))
            .jsonPath("$.[*].hwVersion")
            .value(hasItem(DEFAULT_HW_VERSION))
            .jsonPath("$.[*].swVersion")
            .value(hasItem(DEFAULT_SW_VERSION))
            .jsonPath("$.[*].manufactureDate")
            .value(hasItem(DEFAULT_MANUFACTURE_DATE))
            .jsonPath("$.[*].lon")
            .value(hasItem(DEFAULT_LON.doubleValue()))
            .jsonPath("$.[*].lat")
            .value(hasItem(DEFAULT_LAT.doubleValue()))
            .jsonPath("$.[*].typeCode")
            .value(hasItem(DEFAULT_TYPE_CODE))
            .jsonPath("$.[*].cabNum")
            .value(hasItem(DEFAULT_CAB_NUM))
            .jsonPath("$.[*].cabEmptyNum")
            .value(hasItem(DEFAULT_CAB_EMPTY_NUM))
            .jsonPath("$.[*].bpReadyNum")
            .value(hasItem(DEFAULT_BP_READY_NUM))
            .jsonPath("$.[*].swapBpNo")
            .value(hasItem(DEFAULT_SWAP_BP_NO.intValue()));
    }

    @Test
    void getBss() {
        // Initialize the database
        bssRepository.save(bss).block();

        // Get the bss
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, bss.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(bss.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.address")
            .value(is(DEFAULT_ADDRESS))
            .jsonPath("$.serialNumber")
            .value(is(DEFAULT_SERIAL_NUMBER))
            .jsonPath("$.hwVersion")
            .value(is(DEFAULT_HW_VERSION))
            .jsonPath("$.swVersion")
            .value(is(DEFAULT_SW_VERSION))
            .jsonPath("$.manufactureDate")
            .value(is(DEFAULT_MANUFACTURE_DATE))
            .jsonPath("$.lon")
            .value(is(DEFAULT_LON.doubleValue()))
            .jsonPath("$.lat")
            .value(is(DEFAULT_LAT.doubleValue()))
            .jsonPath("$.typeCode")
            .value(is(DEFAULT_TYPE_CODE))
            .jsonPath("$.cabNum")
            .value(is(DEFAULT_CAB_NUM))
            .jsonPath("$.cabEmptyNum")
            .value(is(DEFAULT_CAB_EMPTY_NUM))
            .jsonPath("$.bpReadyNum")
            .value(is(DEFAULT_BP_READY_NUM))
            .jsonPath("$.swapBpNo")
            .value(is(DEFAULT_SWAP_BP_NO.intValue()));
    }

    @Test
    void getNonExistingBss() {
        // Get the bss
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewBss() throws Exception {
        // Initialize the database
        bssRepository.save(bss).block();

        int databaseSizeBeforeUpdate = bssRepository.findAll().collectList().block().size();

        // Update the bss
        Bss updatedBss = bssRepository.findById(bss.getId()).block();
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

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedBss.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedBss))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll().collectList().block();
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
    void putNonExistingBss() throws Exception {
        int databaseSizeBeforeUpdate = bssRepository.findAll().collectList().block().size();
        bss.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, bss.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bss))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll().collectList().block();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBss() throws Exception {
        int databaseSizeBeforeUpdate = bssRepository.findAll().collectList().block().size();
        bss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bss))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll().collectList().block();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBss() throws Exception {
        int databaseSizeBeforeUpdate = bssRepository.findAll().collectList().block().size();
        bss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bss))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll().collectList().block();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBssWithPatch() throws Exception {
        // Initialize the database
        bssRepository.save(bss).block();

        int databaseSizeBeforeUpdate = bssRepository.findAll().collectList().block().size();

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

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBss.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBss))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll().collectList().block();
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
    void fullUpdateBssWithPatch() throws Exception {
        // Initialize the database
        bssRepository.save(bss).block();

        int databaseSizeBeforeUpdate = bssRepository.findAll().collectList().block().size();

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

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBss.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBss))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll().collectList().block();
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
    void patchNonExistingBss() throws Exception {
        int databaseSizeBeforeUpdate = bssRepository.findAll().collectList().block().size();
        bss.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, bss.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bss))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll().collectList().block();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBss() throws Exception {
        int databaseSizeBeforeUpdate = bssRepository.findAll().collectList().block().size();
        bss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bss))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll().collectList().block();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBss() throws Exception {
        int databaseSizeBeforeUpdate = bssRepository.findAll().collectList().block().size();
        bss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bss))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Bss in the database
        List<Bss> bssList = bssRepository.findAll().collectList().block();
        assertThat(bssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBss() {
        // Initialize the database
        bssRepository.save(bss).block();

        int databaseSizeBeforeDelete = bssRepository.findAll().collectList().block().size();

        // Delete the bss
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, bss.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Bss> bssList = bssRepository.findAll().collectList().block();
        assertThat(bssList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
