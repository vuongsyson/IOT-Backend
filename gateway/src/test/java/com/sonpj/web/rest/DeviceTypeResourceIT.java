package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.DeviceType;
import com.sonpj.repository.DeviceTypeRepository;
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
 * Integration tests for the {@link DeviceTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(DeviceType.class).block();
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
        deviceType = createEntity(em);
    }

    @Test
    void createDeviceType() throws Exception {
        int databaseSizeBeforeCreate = deviceTypeRepository.findAll().collectList().block().size();
        // Create the DeviceType
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceType))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll().collectList().block();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createDeviceTypeWithExistingId() throws Exception {
        // Create the DeviceType with an existing ID
        deviceType.setId(1L);

        int databaseSizeBeforeCreate = deviceTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll().collectList().block();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceTypeRepository.findAll().collectList().block().size();
        // set the field null
        deviceType.setName(null);

        // Create the DeviceType, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll().collectList().block();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDeviceTypesAsStream() {
        // Initialize the database
        deviceTypeRepository.save(deviceType).block();

        List<DeviceType> deviceTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DeviceType.class)
            .getResponseBody()
            .filter(deviceType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(deviceTypeList).isNotNull();
        assertThat(deviceTypeList).hasSize(1);
        DeviceType testDeviceType = deviceTypeList.get(0);
        assertThat(testDeviceType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllDeviceTypes() {
        // Initialize the database
        deviceTypeRepository.save(deviceType).block();

        // Get all the deviceTypeList
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
            .value(hasItem(deviceType.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getDeviceType() {
        // Initialize the database
        deviceTypeRepository.save(deviceType).block();

        // Get the deviceType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, deviceType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(deviceType.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingDeviceType() {
        // Get the deviceType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDeviceType() throws Exception {
        // Initialize the database
        deviceTypeRepository.save(deviceType).block();

        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().collectList().block().size();

        // Update the deviceType
        DeviceType updatedDeviceType = deviceTypeRepository.findById(deviceType.getId()).block();
        updatedDeviceType.name(UPDATED_NAME);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDeviceType.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedDeviceType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll().collectList().block();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().collectList().block().size();
        deviceType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, deviceType.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll().collectList().block();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().collectList().block().size();
        deviceType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll().collectList().block();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().collectList().block().size();
        deviceType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceType))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll().collectList().block();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDeviceTypeWithPatch() throws Exception {
        // Initialize the database
        deviceTypeRepository.save(deviceType).block();

        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().collectList().block().size();

        // Update the deviceType using partial update
        DeviceType partialUpdatedDeviceType = new DeviceType();
        partialUpdatedDeviceType.setId(deviceType.getId());

        partialUpdatedDeviceType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDeviceType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll().collectList().block();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void fullUpdateDeviceTypeWithPatch() throws Exception {
        // Initialize the database
        deviceTypeRepository.save(deviceType).block();

        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().collectList().block().size();

        // Update the deviceType using partial update
        DeviceType partialUpdatedDeviceType = new DeviceType();
        partialUpdatedDeviceType.setId(deviceType.getId());

        partialUpdatedDeviceType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDeviceType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll().collectList().block();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().collectList().block().size();
        deviceType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, deviceType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll().collectList().block();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().collectList().block().size();
        deviceType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll().collectList().block();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().collectList().block().size();
        deviceType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceType))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll().collectList().block();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDeviceType() {
        // Initialize the database
        deviceTypeRepository.save(deviceType).block();

        int databaseSizeBeforeDelete = deviceTypeRepository.findAll().collectList().block().size();

        // Delete the deviceType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, deviceType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll().collectList().block();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
