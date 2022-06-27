package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.SosRequest;
import com.sonpj.domain.enumeration.SosState;
import com.sonpj.repository.SosRequestRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link SosRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SosRequestResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final SosState DEFAULT_STATE = SosState.SEND_REQUEST;
    private static final SosState UPDATED_STATE = SosState.REQUEST_RECEIVED;

    private static final Integer DEFAULT_RATING = 0;
    private static final Integer UPDATED_RATING = 1;

    private static final Boolean DEFAULT_DONE = false;
    private static final Boolean UPDATED_DONE = true;

    private static final Instant DEFAULT_DONE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DONE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sos-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SosRequestRepository sosRequestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSosRequestMockMvc;

    private SosRequest sosRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SosRequest createEntity(EntityManager em) {
        SosRequest sosRequest = new SosRequest()
            .userId(DEFAULT_USER_ID)
            .phone(DEFAULT_PHONE)
            .deviceSerialNumber(DEFAULT_DEVICE_SERIAL_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .image(DEFAULT_IMAGE)
            .state(DEFAULT_STATE)
            .rating(DEFAULT_RATING)
            .done(DEFAULT_DONE)
            .doneTime(DEFAULT_DONE_TIME);
        return sosRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SosRequest createUpdatedEntity(EntityManager em) {
        SosRequest sosRequest = new SosRequest()
            .userId(UPDATED_USER_ID)
            .phone(UPDATED_PHONE)
            .deviceSerialNumber(UPDATED_DEVICE_SERIAL_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .state(UPDATED_STATE)
            .rating(UPDATED_RATING)
            .done(UPDATED_DONE)
            .doneTime(UPDATED_DONE_TIME);
        return sosRequest;
    }

    @BeforeEach
    public void initTest() {
        sosRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createSosRequest() throws Exception {
        int databaseSizeBeforeCreate = sosRequestRepository.findAll().size();
        // Create the SosRequest
        restSosRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sosRequest)))
            .andExpect(status().isCreated());

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeCreate + 1);
        SosRequest testSosRequest = sosRequestList.get(sosRequestList.size() - 1);
        assertThat(testSosRequest.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testSosRequest.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSosRequest.getDeviceSerialNumber()).isEqualTo(DEFAULT_DEVICE_SERIAL_NUMBER);
        assertThat(testSosRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSosRequest.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testSosRequest.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testSosRequest.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testSosRequest.getDone()).isEqualTo(DEFAULT_DONE);
        assertThat(testSosRequest.getDoneTime()).isEqualTo(DEFAULT_DONE_TIME);
    }

    @Test
    @Transactional
    void createSosRequestWithExistingId() throws Exception {
        // Create the SosRequest with an existing ID
        sosRequest.setId(1L);

        int databaseSizeBeforeCreate = sosRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSosRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sosRequest)))
            .andExpect(status().isBadRequest());

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = sosRequestRepository.findAll().size();
        // set the field null
        sosRequest.setUserId(null);

        // Create the SosRequest, which fails.

        restSosRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sosRequest)))
            .andExpect(status().isBadRequest());

        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = sosRequestRepository.findAll().size();
        // set the field null
        sosRequest.setPhone(null);

        // Create the SosRequest, which fails.

        restSosRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sosRequest)))
            .andExpect(status().isBadRequest());

        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeviceSerialNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = sosRequestRepository.findAll().size();
        // set the field null
        sosRequest.setDeviceSerialNumber(null);

        // Create the SosRequest, which fails.

        restSosRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sosRequest)))
            .andExpect(status().isBadRequest());

        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSosRequests() throws Exception {
        // Initialize the database
        sosRequestRepository.saveAndFlush(sosRequest);

        // Get all the sosRequestList
        restSosRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sosRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].deviceSerialNumber").value(hasItem(DEFAULT_DEVICE_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].done").value(hasItem(DEFAULT_DONE.booleanValue())))
            .andExpect(jsonPath("$.[*].doneTime").value(hasItem(DEFAULT_DONE_TIME.toString())));
    }

    @Test
    @Transactional
    void getSosRequest() throws Exception {
        // Initialize the database
        sosRequestRepository.saveAndFlush(sosRequest);

        // Get the sosRequest
        restSosRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, sosRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sosRequest.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.deviceSerialNumber").value(DEFAULT_DEVICE_SERIAL_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.done").value(DEFAULT_DONE.booleanValue()))
            .andExpect(jsonPath("$.doneTime").value(DEFAULT_DONE_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSosRequest() throws Exception {
        // Get the sosRequest
        restSosRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSosRequest() throws Exception {
        // Initialize the database
        sosRequestRepository.saveAndFlush(sosRequest);

        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().size();

        // Update the sosRequest
        SosRequest updatedSosRequest = sosRequestRepository.findById(sosRequest.getId()).get();
        // Disconnect from session so that the updates on updatedSosRequest are not directly saved in db
        em.detach(updatedSosRequest);
        updatedSosRequest
            .userId(UPDATED_USER_ID)
            .phone(UPDATED_PHONE)
            .deviceSerialNumber(UPDATED_DEVICE_SERIAL_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .state(UPDATED_STATE)
            .rating(UPDATED_RATING)
            .done(UPDATED_DONE)
            .doneTime(UPDATED_DONE_TIME);

        restSosRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSosRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSosRequest))
            )
            .andExpect(status().isOk());

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
        SosRequest testSosRequest = sosRequestList.get(sosRequestList.size() - 1);
        assertThat(testSosRequest.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testSosRequest.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSosRequest.getDeviceSerialNumber()).isEqualTo(UPDATED_DEVICE_SERIAL_NUMBER);
        assertThat(testSosRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSosRequest.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSosRequest.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSosRequest.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testSosRequest.getDone()).isEqualTo(UPDATED_DONE);
        assertThat(testSosRequest.getDoneTime()).isEqualTo(UPDATED_DONE_TIME);
    }

    @Test
    @Transactional
    void putNonExistingSosRequest() throws Exception {
        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().size();
        sosRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSosRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sosRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sosRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSosRequest() throws Exception {
        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().size();
        sosRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSosRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sosRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSosRequest() throws Exception {
        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().size();
        sosRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSosRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sosRequest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSosRequestWithPatch() throws Exception {
        // Initialize the database
        sosRequestRepository.saveAndFlush(sosRequest);

        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().size();

        // Update the sosRequest using partial update
        SosRequest partialUpdatedSosRequest = new SosRequest();
        partialUpdatedSosRequest.setId(sosRequest.getId());

        partialUpdatedSosRequest
            .userId(UPDATED_USER_ID)
            .deviceSerialNumber(UPDATED_DEVICE_SERIAL_NUMBER)
            .image(UPDATED_IMAGE)
            .state(UPDATED_STATE);

        restSosRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSosRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSosRequest))
            )
            .andExpect(status().isOk());

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
        SosRequest testSosRequest = sosRequestList.get(sosRequestList.size() - 1);
        assertThat(testSosRequest.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testSosRequest.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSosRequest.getDeviceSerialNumber()).isEqualTo(UPDATED_DEVICE_SERIAL_NUMBER);
        assertThat(testSosRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSosRequest.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSosRequest.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSosRequest.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testSosRequest.getDone()).isEqualTo(DEFAULT_DONE);
        assertThat(testSosRequest.getDoneTime()).isEqualTo(DEFAULT_DONE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateSosRequestWithPatch() throws Exception {
        // Initialize the database
        sosRequestRepository.saveAndFlush(sosRequest);

        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().size();

        // Update the sosRequest using partial update
        SosRequest partialUpdatedSosRequest = new SosRequest();
        partialUpdatedSosRequest.setId(sosRequest.getId());

        partialUpdatedSosRequest
            .userId(UPDATED_USER_ID)
            .phone(UPDATED_PHONE)
            .deviceSerialNumber(UPDATED_DEVICE_SERIAL_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .state(UPDATED_STATE)
            .rating(UPDATED_RATING)
            .done(UPDATED_DONE)
            .doneTime(UPDATED_DONE_TIME);

        restSosRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSosRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSosRequest))
            )
            .andExpect(status().isOk());

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
        SosRequest testSosRequest = sosRequestList.get(sosRequestList.size() - 1);
        assertThat(testSosRequest.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testSosRequest.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSosRequest.getDeviceSerialNumber()).isEqualTo(UPDATED_DEVICE_SERIAL_NUMBER);
        assertThat(testSosRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSosRequest.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSosRequest.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSosRequest.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testSosRequest.getDone()).isEqualTo(UPDATED_DONE);
        assertThat(testSosRequest.getDoneTime()).isEqualTo(UPDATED_DONE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingSosRequest() throws Exception {
        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().size();
        sosRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSosRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sosRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sosRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSosRequest() throws Exception {
        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().size();
        sosRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSosRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sosRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSosRequest() throws Exception {
        int databaseSizeBeforeUpdate = sosRequestRepository.findAll().size();
        sosRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSosRequestMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sosRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SosRequest in the database
        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSosRequest() throws Exception {
        // Initialize the database
        sosRequestRepository.saveAndFlush(sosRequest);

        int databaseSizeBeforeDelete = sosRequestRepository.findAll().size();

        // Delete the sosRequest
        restSosRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, sosRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SosRequest> sosRequestList = sosRequestRepository.findAll();
        assertThat(sosRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
