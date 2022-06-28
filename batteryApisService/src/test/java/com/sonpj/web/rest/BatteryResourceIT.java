package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.Battery;
import com.sonpj.repository.BatteryRepository;
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
 * Integration tests for the {@link BatteryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restBatteryMockMvc;

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

    @BeforeEach
    public void initTest() {
        battery = createEntity(em);
    }

    @Test
    @Transactional
    void createBattery() throws Exception {
        int databaseSizeBeforeCreate = batteryRepository.findAll().size();
        // Create the Battery
        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isCreated());

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll();
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
    @Transactional
    void createBatteryWithExistingId() throws Exception {
        // Create the Battery with an existing ID
        battery.setId(1L);

        int databaseSizeBeforeCreate = batteryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSerialNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setSerialNo(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHwVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setHwVersion(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSwVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setSwVersion(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkManufactureDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setManufactureDate(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setCapacity(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxChargeIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setMaxCharge(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxDiscargeIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setMaxDiscarge(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxVolIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setMaxVol(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinVolIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setMinVol(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUsedIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setUsed(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSocIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setSoc(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSohIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setSoh(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTempIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setTemp(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOwnerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setOwnerId(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRenterIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setRenterId(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCycleCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = batteryRepository.findAll().size();
        // set the field null
        battery.setCycleCount(null);

        // Create the Battery, which fails.

        restBatteryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isBadRequest());

        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBatteries() throws Exception {
        // Initialize the database
        batteryRepository.saveAndFlush(battery);

        // Get all the batteryList
        restBatteryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(battery.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNo").value(hasItem(DEFAULT_SERIAL_NO)))
            .andExpect(jsonPath("$.[*].hwVersion").value(hasItem(DEFAULT_HW_VERSION)))
            .andExpect(jsonPath("$.[*].swVersion").value(hasItem(DEFAULT_SW_VERSION)))
            .andExpect(jsonPath("$.[*].manufactureDate").value(hasItem(DEFAULT_MANUFACTURE_DATE)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].maxCharge").value(hasItem(DEFAULT_MAX_CHARGE)))
            .andExpect(jsonPath("$.[*].maxDiscarge").value(hasItem(DEFAULT_MAX_DISCARGE)))
            .andExpect(jsonPath("$.[*].maxVol").value(hasItem(DEFAULT_MAX_VOL)))
            .andExpect(jsonPath("$.[*].minVol").value(hasItem(DEFAULT_MIN_VOL)))
            .andExpect(jsonPath("$.[*].used").value(hasItem(DEFAULT_USED.booleanValue())))
            .andExpect(jsonPath("$.[*].soc").value(hasItem(DEFAULT_SOC)))
            .andExpect(jsonPath("$.[*].soh").value(hasItem(DEFAULT_SOH)))
            .andExpect(jsonPath("$.[*].temp").value(hasItem(DEFAULT_TEMP)))
            .andExpect(jsonPath("$.[*].ownerId").value(hasItem(DEFAULT_OWNER_ID.intValue())))
            .andExpect(jsonPath("$.[*].renterId").value(hasItem(DEFAULT_RENTER_ID.intValue())))
            .andExpect(jsonPath("$.[*].cycleCount").value(hasItem(DEFAULT_CYCLE_COUNT.intValue())));
    }

    @Test
    @Transactional
    void getBattery() throws Exception {
        // Initialize the database
        batteryRepository.saveAndFlush(battery);

        // Get the battery
        restBatteryMockMvc
            .perform(get(ENTITY_API_URL_ID, battery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(battery.getId().intValue()))
            .andExpect(jsonPath("$.serialNo").value(DEFAULT_SERIAL_NO))
            .andExpect(jsonPath("$.hwVersion").value(DEFAULT_HW_VERSION))
            .andExpect(jsonPath("$.swVersion").value(DEFAULT_SW_VERSION))
            .andExpect(jsonPath("$.manufactureDate").value(DEFAULT_MANUFACTURE_DATE))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY))
            .andExpect(jsonPath("$.maxCharge").value(DEFAULT_MAX_CHARGE))
            .andExpect(jsonPath("$.maxDiscarge").value(DEFAULT_MAX_DISCARGE))
            .andExpect(jsonPath("$.maxVol").value(DEFAULT_MAX_VOL))
            .andExpect(jsonPath("$.minVol").value(DEFAULT_MIN_VOL))
            .andExpect(jsonPath("$.used").value(DEFAULT_USED.booleanValue()))
            .andExpect(jsonPath("$.soc").value(DEFAULT_SOC))
            .andExpect(jsonPath("$.soh").value(DEFAULT_SOH))
            .andExpect(jsonPath("$.temp").value(DEFAULT_TEMP))
            .andExpect(jsonPath("$.ownerId").value(DEFAULT_OWNER_ID.intValue()))
            .andExpect(jsonPath("$.renterId").value(DEFAULT_RENTER_ID.intValue()))
            .andExpect(jsonPath("$.cycleCount").value(DEFAULT_CYCLE_COUNT.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingBattery() throws Exception {
        // Get the battery
        restBatteryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBattery() throws Exception {
        // Initialize the database
        batteryRepository.saveAndFlush(battery);

        int databaseSizeBeforeUpdate = batteryRepository.findAll().size();

        // Update the battery
        Battery updatedBattery = batteryRepository.findById(battery.getId()).get();
        // Disconnect from session so that the updates on updatedBattery are not directly saved in db
        em.detach(updatedBattery);
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

        restBatteryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBattery.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBattery))
            )
            .andExpect(status().isOk());

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll();
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
    @Transactional
    void putNonExistingBattery() throws Exception {
        int databaseSizeBeforeUpdate = batteryRepository.findAll().size();
        battery.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatteryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, battery.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(battery))
            )
            .andExpect(status().isBadRequest());

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBattery() throws Exception {
        int databaseSizeBeforeUpdate = batteryRepository.findAll().size();
        battery.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatteryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(battery))
            )
            .andExpect(status().isBadRequest());

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBattery() throws Exception {
        int databaseSizeBeforeUpdate = batteryRepository.findAll().size();
        battery.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatteryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBatteryWithPatch() throws Exception {
        // Initialize the database
        batteryRepository.saveAndFlush(battery);

        int databaseSizeBeforeUpdate = batteryRepository.findAll().size();

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

        restBatteryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBattery.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBattery))
            )
            .andExpect(status().isOk());

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll();
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
    @Transactional
    void fullUpdateBatteryWithPatch() throws Exception {
        // Initialize the database
        batteryRepository.saveAndFlush(battery);

        int databaseSizeBeforeUpdate = batteryRepository.findAll().size();

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

        restBatteryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBattery.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBattery))
            )
            .andExpect(status().isOk());

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll();
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
    @Transactional
    void patchNonExistingBattery() throws Exception {
        int databaseSizeBeforeUpdate = batteryRepository.findAll().size();
        battery.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatteryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, battery.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(battery))
            )
            .andExpect(status().isBadRequest());

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBattery() throws Exception {
        int databaseSizeBeforeUpdate = batteryRepository.findAll().size();
        battery.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatteryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(battery))
            )
            .andExpect(status().isBadRequest());

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBattery() throws Exception {
        int databaseSizeBeforeUpdate = batteryRepository.findAll().size();
        battery.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatteryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(battery)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Battery in the database
        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBattery() throws Exception {
        // Initialize the database
        batteryRepository.saveAndFlush(battery);

        int databaseSizeBeforeDelete = batteryRepository.findAll().size();

        // Delete the battery
        restBatteryMockMvc
            .perform(delete(ENTITY_API_URL_ID, battery.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Battery> batteryList = batteryRepository.findAll();
        assertThat(batteryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
