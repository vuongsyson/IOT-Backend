package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.Battery;
import com.sonpj.repository.BatteryRepository;
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
 * Integration tests for the {@link BatteryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class BatteryResourceIT {

    private static final String DEFAULT_SERIAL_NO = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_HW_VERSION = 1;
    private static final Integer UPDATED_HW_VERSION = 2;

    private static final Integer DEFAULT_SW_VERSION = 1;
    private static final Integer UPDATED_SW_VERSION = 2;

    private static final String DEFAULT_MANUFACTURE_DATE = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURE_DATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;

    private static final Integer DEFAULT_MAX_CHARGE = 1;
    private static final Integer UPDATED_MAX_CHARGE = 2;

    private static final Integer DEFAULT_MAX_DISCARGE = 1;
    private static final Integer UPDATED_MAX_DISCARGE = 2;

    private static final Integer DEFAULT_MAX_VOL = 1;
    private static final Integer UPDATED_MAX_VOL = 2;

    private static final Integer DEFAULT_MIN_VOL = 1;
    private static final Integer UPDATED_MIN_VOL = 2;

    private static final Boolean DEFAULT_USED = false;
    private static final Boolean UPDATED_USED = true;

    private static final Integer DEFAULT_SOC = 1;
    private static final Integer UPDATED_SOC = 2;

    private static final Integer DEFAULT_SOH = 1;
    private static final Integer UPDATED_SOH = 2;

    private static final Integer DEFAULT_TEMP = 1;
    private static final Integer UPDATED_TEMP = 2;

    private static final Long DEFAULT_OWNER_ID = 1L;
    private static final Long UPDATED_OWNER_ID = 2L;

    private static final Long DEFAULT_RENTER_ID = 1L;
    private static final Long UPDATED_RENTER_ID = 2L;

    private static final Long DEFAULT_CYCLE_COUNT = 1L;
    private static final Long UPDATED_CYCLE_COUNT = 2L;

    private static final String ENTITY_API_URL = "/api/batteries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BatteryRepository batteryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Battery battery;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Battery createEntity(EntityManager em) {
        Battery battery = new Battery()
            .serialNo(DEFAULT_SERIAL_NO)
            .hwVersion(DEFAULT_HW_VERSION)
            .swVersion(DEFAULT_SW_VERSION)
            .manufactureDate(DEFAULT_MANUFACTURE_DATE)
            .capacity(DEFAULT_CAPACITY)
            .maxCharge(DEFAULT_MAX_CHARGE)
            .maxDiscarge(DEFAULT_MAX_DISCARGE)
            .maxVol(DEFAULT_MAX_VOL)
            .minVol(DEFAULT_MIN_VOL)
            .used(DEFAULT_USED)
            .soc(DEFAULT_SOC)
            .soh(DEFAULT_SOH)
            .temp(DEFAULT_TEMP)
            .ownerId(DEFAULT_OWNER_ID)
            .renterId(DEFAULT_RENTER_ID)
            .cycleCount(DEFAULT_CYCLE_COUNT);
        return battery;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Battery createUpdatedEntity(EntityManager em) {
        Battery battery = new Battery()
            .serialNo(UPDATED_SERIAL_NO)
            .hwVersion(UPDATED_HW_VERSION)
            .swVersion(UPDATED_SW_VERSION)
            .manufactureDate(UPDATED_MANUFACTURE_DATE)
            .capacity(UPDATED_CAPACITY)
            .maxCharge(UPDATED_MAX_CHARGE)
            .maxDiscarge(UPDATED_MAX_DISCARGE)
            .maxVol(UPDATED_MAX_VOL)
            .minVol(UPDATED_MIN_VOL)
            .used(UPDATED_USED)
            .soc(UPDATED_SOC)
            .soh(UPDATED_SOH)
            .temp(UPDATED_TEMP)
            .ownerId(UPDATED_OWNER_ID)
            .renterId(UPDATED_RENTER_ID)
            .cycleCount(UPDATED_CYCLE_COUNT);
        return battery;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Battery.class).block();
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
        battery = createEntity(em);
    }

    @Test
    void createBattery() throws Exception {
        int databaseSizeBeforeCreate = batteryRepository.findAll().collectList().block().size();
        // Create the Battery
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeCreate + 1);
        Battery testBattery = batteryList.get(batteryList.size() - 1);
        assertThat(testBattery.getSerialNo()).isEqualTo(DEFAULT_SERIAL_NO);
        assertThat(testBattery.getHwVersion()).isEqualTo(DEFAULT_HW_VERSION);
        assertThat(testBattery.getSwVersion()).isEqualTo(DEFAULT_SW_VERSION);
        assertThat(testBattery.getManufactureDate()).isEqualTo(DEFAULT_MANUFACTURE_DATE);
        assertThat(testBattery.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
        assertThat(testBattery.getMaxCharge()).isEqualTo(DEFAULT_MAX_CHARGE);
        assertThat(testBattery.getMaxDiscarge()).isEqualTo(DEFAULT_MAX_DISCARGE);
        assertThat(testBattery.getMaxVol()).isEqualTo(DEFAULT_MAX_VOL);
        assertThat(testBattery.getMinVol()).isEqualTo(DEFAULT_MIN_VOL);
        assertThat(testBattery.getUsed()).isEqualTo(DEFAULT_USED);
        assertThat(testBattery.getSoc()).isEqualTo(DEFAULT_SOC);
        assertThat(testBattery.getSoh()).isEqualTo(DEFAULT_SOH);
        assertThat(testBattery.getTemp()).isEqualTo(DEFAULT_TEMP);
        assertThat(testBattery.getOwnerId()).isEqualTo(DEFAULT_OWNER_ID);
        assertThat(testBattery.getRenterId()).isEqualTo(DEFAULT_RENTER_ID);
        assertThat(testBattery.getCycleCount()).isEqualTo(DEFAULT_CYCLE_COUNT);
    }

    @Test
    void createBatteryWithExistingId() throws Exception {
        // Create the Battery with an existing ID
        battery.setId(1L);

        int databaseSizeBeforeCreate = batteryRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkSerialNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setSerialNo(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkHwVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setHwVersion(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSwVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setSwVersion(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkManufactureDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setManufactureDate(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setCapacity(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMaxChargeIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setMaxCharge(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMaxDiscargeIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setMaxDiscarge(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMaxVolIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setMaxVol(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMinVolIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setMinVol(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUsedIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setUsed(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSocIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setSoc(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSohIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setSoh(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTempIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setTemp(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkOwnerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setOwnerId(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkRenterIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setRenterId(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCycleCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().collectList().block().size();
        // set the field null
        battery.setCycleCount(null);

        // Create the Battery, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllBatteriesAsStream() {
        // Initialize the database
        batteryRepository.save(battery).block();

        List<Battery> batteryList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Battery.class)
            .getResponseBody()
            .filter(battery::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(batteryList).isNotNull();
        assertThat(batteryList).hasSize(1);
        Battery testBattery = batteryList.get(0);
        assertThat(testBattery.getSerialNo()).isEqualTo(DEFAULT_SERIAL_NO);
        assertThat(testBattery.getHwVersion()).isEqualTo(DEFAULT_HW_VERSION);
        assertThat(testBattery.getSwVersion()).isEqualTo(DEFAULT_SW_VERSION);
        assertThat(testBattery.getManufactureDate()).isEqualTo(DEFAULT_MANUFACTURE_DATE);
        assertThat(testBattery.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
        assertThat(testBattery.getMaxCharge()).isEqualTo(DEFAULT_MAX_CHARGE);
        assertThat(testBattery.getMaxDiscarge()).isEqualTo(DEFAULT_MAX_DISCARGE);
        assertThat(testBattery.getMaxVol()).isEqualTo(DEFAULT_MAX_VOL);
        assertThat(testBattery.getMinVol()).isEqualTo(DEFAULT_MIN_VOL);
        assertThat(testBattery.getUsed()).isEqualTo(DEFAULT_USED);
        assertThat(testBattery.getSoc()).isEqualTo(DEFAULT_SOC);
        assertThat(testBattery.getSoh()).isEqualTo(DEFAULT_SOH);
        assertThat(testBattery.getTemp()).isEqualTo(DEFAULT_TEMP);
        assertThat(testBattery.getOwnerId()).isEqualTo(DEFAULT_OWNER_ID);
        assertThat(testBattery.getRenterId()).isEqualTo(DEFAULT_RENTER_ID);
        assertThat(testBattery.getCycleCount()).isEqualTo(DEFAULT_CYCLE_COUNT);
    }

    @Test
    void getAllBatteries() {
        // Initialize the database
        batteryRepository.save(battery).block();

        // Get all the batteryList
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
            .value(hasItem(battery.getId().intValue()))
            .jsonPath("$.[*].serialNo")
            .value(hasItem(DEFAULT_SERIAL_NO))
            .jsonPath("$.[*].hwVersion")
            .value(hasItem(DEFAULT_HW_VERSION))
            .jsonPath("$.[*].swVersion")
            .value(hasItem(DEFAULT_SW_VERSION))
            .jsonPath("$.[*].manufactureDate")
            .value(hasItem(DEFAULT_MANUFACTURE_DATE))
            .jsonPath("$.[*].capacity")
            .value(hasItem(DEFAULT_CAPACITY))
            .jsonPath("$.[*].maxCharge")
            .value(hasItem(DEFAULT_MAX_CHARGE))
            .jsonPath("$.[*].maxDiscarge")
            .value(hasItem(DEFAULT_MAX_DISCARGE))
            .jsonPath("$.[*].maxVol")
            .value(hasItem(DEFAULT_MAX_VOL))
            .jsonPath("$.[*].minVol")
            .value(hasItem(DEFAULT_MIN_VOL))
            .jsonPath("$.[*].used")
            .value(hasItem(DEFAULT_USED.booleanValue()))
            .jsonPath("$.[*].soc")
            .value(hasItem(DEFAULT_SOC))
            .jsonPath("$.[*].soh")
            .value(hasItem(DEFAULT_SOH))
            .jsonPath("$.[*].temp")
            .value(hasItem(DEFAULT_TEMP))
            .jsonPath("$.[*].ownerId")
            .value(hasItem(DEFAULT_OWNER_ID.intValue()))
            .jsonPath("$.[*].renterId")
            .value(hasItem(DEFAULT_RENTER_ID.intValue()))
            .jsonPath("$.[*].cycleCount")
            .value(hasItem(DEFAULT_CYCLE_COUNT.intValue()));
    }

    @Test
    void getBattery() {
        // Initialize the database
        batteryRepository.save(battery).block();

        // Get the battery
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, battery.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(battery.getId().intValue()))
            .jsonPath("$.serialNo")
            .value(is(DEFAULT_SERIAL_NO))
            .jsonPath("$.hwVersion")
            .value(is(DEFAULT_HW_VERSION))
            .jsonPath("$.swVersion")
            .value(is(DEFAULT_SW_VERSION))
            .jsonPath("$.manufactureDate")
            .value(is(DEFAULT_MANUFACTURE_DATE))
            .jsonPath("$.capacity")
            .value(is(DEFAULT_CAPACITY))
            .jsonPath("$.maxCharge")
            .value(is(DEFAULT_MAX_CHARGE))
            .jsonPath("$.maxDiscarge")
            .value(is(DEFAULT_MAX_DISCARGE))
            .jsonPath("$.maxVol")
            .value(is(DEFAULT_MAX_VOL))
            .jsonPath("$.minVol")
            .value(is(DEFAULT_MIN_VOL))
            .jsonPath("$.used")
            .value(is(DEFAULT_USED.booleanValue()))
            .jsonPath("$.soc")
            .value(is(DEFAULT_SOC))
            .jsonPath("$.soh")
            .value(is(DEFAULT_SOH))
            .jsonPath("$.temp")
            .value(is(DEFAULT_TEMP))
            .jsonPath("$.ownerId")
            .value(is(DEFAULT_OWNER_ID.intValue()))
            .jsonPath("$.renterId")
            .value(is(DEFAULT_RENTER_ID.intValue()))
            .jsonPath("$.cycleCount")
            .value(is(DEFAULT_CYCLE_COUNT.intValue()));
    }

    @Test
    void getNonExistingBattery() {
        // Get the battery
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewBattery() throws Exception {
        // Initialize the database
        batteryRepository.save(battery).block();

        int databaseSizeBeforeUpdate = batteryRepository.findAll().collectList().block().size();

        // Update the battery
        Battery updatedBattery = batteryRepository.findById(battery.getId()).block();
        updatedBattery
            .serialNo(UPDATED_SERIAL_NO)
            .hwVersion(UPDATED_HW_VERSION)
            .swVersion(UPDATED_SW_VERSION)
            .manufactureDate(UPDATED_MANUFACTURE_DATE)
            .capacity(UPDATED_CAPACITY)
            .maxCharge(UPDATED_MAX_CHARGE)
            .maxDiscarge(UPDATED_MAX_DISCARGE)
            .maxVol(UPDATED_MAX_VOL)
            .minVol(UPDATED_MIN_VOL)
            .used(UPDATED_USED)
            .soc(UPDATED_SOC)
            .soh(UPDATED_SOH)
            .temp(UPDATED_TEMP)
            .ownerId(UPDATED_OWNER_ID)
            .renterId(UPDATED_RENTER_ID)
            .cycleCount(UPDATED_CYCLE_COUNT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedBattery.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedBattery))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
        Battery testBattery = batteryList.get(batteryList.size() - 1);
        assertThat(testBattery.getSerialNo()).isEqualTo(UPDATED_SERIAL_NO);
        assertThat(testBattery.getHwVersion()).isEqualTo(UPDATED_HW_VERSION);
        assertThat(testBattery.getSwVersion()).isEqualTo(UPDATED_SW_VERSION);
        assertThat(testBattery.getManufactureDate()).isEqualTo(UPDATED_MANUFACTURE_DATE);
        assertThat(testBattery.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testBattery.getMaxCharge()).isEqualTo(UPDATED_MAX_CHARGE);
        assertThat(testBattery.getMaxDiscarge()).isEqualTo(UPDATED_MAX_DISCARGE);
        assertThat(testBattery.getMaxVol()).isEqualTo(UPDATED_MAX_VOL);
        assertThat(testBattery.getMinVol()).isEqualTo(UPDATED_MIN_VOL);
        assertThat(testBattery.getUsed()).isEqualTo(UPDATED_USED);
        assertThat(testBattery.getSoc()).isEqualTo(UPDATED_SOC);
        assertThat(testBattery.getSoh()).isEqualTo(UPDATED_SOH);
        assertThat(testBattery.getTemp()).isEqualTo(UPDATED_TEMP);
        assertThat(testBattery.getOwnerId()).isEqualTo(UPDATED_OWNER_ID);
        assertThat(testBattery.getRenterId()).isEqualTo(UPDATED_RENTER_ID);
        assertThat(testBattery.getCycleCount()).isEqualTo(UPDATED_CYCLE_COUNT);
    }

    @Test
    void putNonExistingBattery() throws Exception {
        int databaseSizeBeforeUpdate = batteryRepository.findAll().collectList().block().size();
        battery.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, battery.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBattery() throws Exception {
        int databaseSizeBeforeUpdate = batteryRepository.findAll().collectList().block().size();
        battery.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBattery() throws Exception {
        int databaseSizeBeforeUpdate = batteryRepository.findAll().collectList().block().size();
        battery.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBatteryWithPatch() throws Exception {
        // Initialize the database
        batteryRepository.save(battery).block();

        int databaseSizeBeforeUpdate = batteryRepository.findAll().collectList().block().size();

        // Update the battery using partial update
        Battery partialUpdatedBattery = new Battery();
        partialUpdatedBattery.setId(battery.getId());

        partialUpdatedBattery
            .swVersion(UPDATED_SW_VERSION)
            .manufactureDate(UPDATED_MANUFACTURE_DATE)
            .capacity(UPDATED_CAPACITY)
            .maxCharge(UPDATED_MAX_CHARGE)
            .maxDiscarge(UPDATED_MAX_DISCARGE)
            .minVol(UPDATED_MIN_VOL)
            .used(UPDATED_USED)
            .temp(UPDATED_TEMP)
            .ownerId(UPDATED_OWNER_ID)
            .renterId(UPDATED_RENTER_ID)
            .cycleCount(UPDATED_CYCLE_COUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBattery.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBattery))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
        Battery testBattery = batteryList.get(batteryList.size() - 1);
        assertThat(testBattery.getSerialNo()).isEqualTo(DEFAULT_SERIAL_NO);
        assertThat(testBattery.getHwVersion()).isEqualTo(DEFAULT_HW_VERSION);
        assertThat(testBattery.getSwVersion()).isEqualTo(UPDATED_SW_VERSION);
        assertThat(testBattery.getManufactureDate()).isEqualTo(UPDATED_MANUFACTURE_DATE);
        assertThat(testBattery.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testBattery.getMaxCharge()).isEqualTo(UPDATED_MAX_CHARGE);
        assertThat(testBattery.getMaxDiscarge()).isEqualTo(UPDATED_MAX_DISCARGE);
        assertThat(testBattery.getMaxVol()).isEqualTo(DEFAULT_MAX_VOL);
        assertThat(testBattery.getMinVol()).isEqualTo(UPDATED_MIN_VOL);
        assertThat(testBattery.getUsed()).isEqualTo(UPDATED_USED);
        assertThat(testBattery.getSoc()).isEqualTo(DEFAULT_SOC);
        assertThat(testBattery.getSoh()).isEqualTo(DEFAULT_SOH);
        assertThat(testBattery.getTemp()).isEqualTo(UPDATED_TEMP);
        assertThat(testBattery.getOwnerId()).isEqualTo(UPDATED_OWNER_ID);
        assertThat(testBattery.getRenterId()).isEqualTo(UPDATED_RENTER_ID);
        assertThat(testBattery.getCycleCount()).isEqualTo(UPDATED_CYCLE_COUNT);
    }

    @Test
    void fullUpdateBatteryWithPatch() throws Exception {
        // Initialize the database
        batteryRepository.save(battery).block();

        int databaseSizeBeforeUpdate = batteryRepository.findAll().collectList().block().size();

        // Update the battery using partial update
        Battery partialUpdatedBattery = new Battery();
        partialUpdatedBattery.setId(battery.getId());

        partialUpdatedBattery
            .serialNo(UPDATED_SERIAL_NO)
            .hwVersion(UPDATED_HW_VERSION)
            .swVersion(UPDATED_SW_VERSION)
            .manufactureDate(UPDATED_MANUFACTURE_DATE)
            .capacity(UPDATED_CAPACITY)
            .maxCharge(UPDATED_MAX_CHARGE)
            .maxDiscarge(UPDATED_MAX_DISCARGE)
            .maxVol(UPDATED_MAX_VOL)
            .minVol(UPDATED_MIN_VOL)
            .used(UPDATED_USED)
            .soc(UPDATED_SOC)
            .soh(UPDATED_SOH)
            .temp(UPDATED_TEMP)
            .ownerId(UPDATED_OWNER_ID)
            .renterId(UPDATED_RENTER_ID)
            .cycleCount(UPDATED_CYCLE_COUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBattery.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBattery))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
        Battery testBattery = batteryList.get(batteryList.size() - 1);
        assertThat(testBattery.getSerialNo()).isEqualTo(UPDATED_SERIAL_NO);
        assertThat(testBattery.getHwVersion()).isEqualTo(UPDATED_HW_VERSION);
        assertThat(testBattery.getSwVersion()).isEqualTo(UPDATED_SW_VERSION);
        assertThat(testBattery.getManufactureDate()).isEqualTo(UPDATED_MANUFACTURE_DATE);
        assertThat(testBattery.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testBattery.getMaxCharge()).isEqualTo(UPDATED_MAX_CHARGE);
        assertThat(testBattery.getMaxDiscarge()).isEqualTo(UPDATED_MAX_DISCARGE);
        assertThat(testBattery.getMaxVol()).isEqualTo(UPDATED_MAX_VOL);
        assertThat(testBattery.getMinVol()).isEqualTo(UPDATED_MIN_VOL);
        assertThat(testBattery.getUsed()).isEqualTo(UPDATED_USED);
        assertThat(testBattery.getSoc()).isEqualTo(UPDATED_SOC);
        assertThat(testBattery.getSoh()).isEqualTo(UPDATED_SOH);
        assertThat(testBattery.getTemp()).isEqualTo(UPDATED_TEMP);
        assertThat(testBattery.getOwnerId()).isEqualTo(UPDATED_OWNER_ID);
        assertThat(testBattery.getRenterId()).isEqualTo(UPDATED_RENTER_ID);
        assertThat(testBattery.getCycleCount()).isEqualTo(UPDATED_CYCLE_COUNT);
    }

    @Test
    void patchNonExistingBattery() throws Exception {
        int databaseSizeBeforeUpdate = batteryRepository.findAll().collectList().block().size();
        battery.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, battery.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBattery() throws Exception {
        int databaseSizeBeforeUpdate = batteryRepository.findAll().collectList().block().size();
        battery.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBattery() throws Exception {
        int databaseSizeBeforeUpdate = batteryRepository.findAll().collectList().block().size();
        battery.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(battery))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBattery() {
        // Initialize the database
        batteryRepository.save(battery).block();

        int databaseSizeBeforeDelete = batteryRepository.findAll().collectList().block().size();

        // Delete the battery
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, battery.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Battery> batteryList = batteryRepository.findAll().collectList().block();
        assertThat(batteryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
