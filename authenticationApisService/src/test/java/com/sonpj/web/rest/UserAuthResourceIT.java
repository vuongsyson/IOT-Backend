package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.UserAuth;
import com.sonpj.repository.UserAuthRepository;
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
 * Integration tests for the {@link UserAuthResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAuthResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHORITIES = "AAAAAAAAAA";
    private static final String UPDATED_AUTHORITIES = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-auths";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAuthMockMvc;

    private UserAuth userAuth;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAuth createEntity(EntityManager em) {
        UserAuth userAuth = new UserAuth()
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .username(DEFAULT_USERNAME)
            .authorities(DEFAULT_AUTHORITIES)
            .password(DEFAULT_PASSWORD);
        return userAuth;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAuth createUpdatedEntity(EntityManager em) {
        UserAuth userAuth = new UserAuth()
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .username(UPDATED_USERNAME)
            .authorities(UPDATED_AUTHORITIES)
            .password(UPDATED_PASSWORD);
        return userAuth;
    }

    @BeforeEach
    public void initTest() {
        userAuth = createEntity(em);
    }

    @Test
    @Transactional
    void createUserAuth() throws Exception {
        int databaseSizeBeforeCreate = userAuthRepository.findAll().size();
        // Create the UserAuth
        restUserAuthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAuth)))
            .andExpect(status().isCreated());

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll();
        assertThat(userAuthList).hasSize(databaseSizeBeforeCreate + 1);
        UserAuth testUserAuth = userAuthList.get(userAuthList.size() - 1);
        assertThat(testUserAuth.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserAuth.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserAuth.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUserAuth.getAuthorities()).isEqualTo(DEFAULT_AUTHORITIES);
        assertThat(testUserAuth.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createUserAuthWithExistingId() throws Exception {
        // Create the UserAuth with an existing ID
        userAuth.setId(1L);

        int databaseSizeBeforeCreate = userAuthRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAuthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAuth)))
            .andExpect(status().isBadRequest());

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll();
        assertThat(userAuthList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserAuths() throws Exception {
        // Initialize the database
        userAuthRepository.saveAndFlush(userAuth);

        // Get all the userAuthList
        restUserAuthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAuth.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].authorities").value(hasItem(DEFAULT_AUTHORITIES)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getUserAuth() throws Exception {
        // Initialize the database
        userAuthRepository.saveAndFlush(userAuth);

        // Get the userAuth
        restUserAuthMockMvc
            .perform(get(ENTITY_API_URL_ID, userAuth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAuth.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.authorities").value(DEFAULT_AUTHORITIES))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getNonExistingUserAuth() throws Exception {
        // Get the userAuth
        restUserAuthMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserAuth() throws Exception {
        // Initialize the database
        userAuthRepository.saveAndFlush(userAuth);

        int databaseSizeBeforeUpdate = userAuthRepository.findAll().size();

        // Update the userAuth
        UserAuth updatedUserAuth = userAuthRepository.findById(userAuth.getId()).get();
        // Disconnect from session so that the updates on updatedUserAuth are not directly saved in db
        em.detach(updatedUserAuth);
        updatedUserAuth
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .username(UPDATED_USERNAME)
            .authorities(UPDATED_AUTHORITIES)
            .password(UPDATED_PASSWORD);

        restUserAuthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserAuth.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserAuth))
            )
            .andExpect(status().isOk());

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
        UserAuth testUserAuth = userAuthList.get(userAuthList.size() - 1);
        assertThat(testUserAuth.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserAuth.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserAuth.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserAuth.getAuthorities()).isEqualTo(UPDATED_AUTHORITIES);
        assertThat(testUserAuth.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingUserAuth() throws Exception {
        int databaseSizeBeforeUpdate = userAuthRepository.findAll().size();
        userAuth.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAuthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAuth.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAuth))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAuth() throws Exception {
        int databaseSizeBeforeUpdate = userAuthRepository.findAll().size();
        userAuth.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAuthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAuth))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAuth() throws Exception {
        int databaseSizeBeforeUpdate = userAuthRepository.findAll().size();
        userAuth.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAuthMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAuth)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAuthWithPatch() throws Exception {
        // Initialize the database
        userAuthRepository.saveAndFlush(userAuth);

        int databaseSizeBeforeUpdate = userAuthRepository.findAll().size();

        // Update the userAuth using partial update
        UserAuth partialUpdatedUserAuth = new UserAuth();
        partialUpdatedUserAuth.setId(userAuth.getId());

        partialUpdatedUserAuth.username(UPDATED_USERNAME).authorities(UPDATED_AUTHORITIES);

        restUserAuthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAuth.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAuth))
            )
            .andExpect(status().isOk());

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
        UserAuth testUserAuth = userAuthList.get(userAuthList.size() - 1);
        assertThat(testUserAuth.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserAuth.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserAuth.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserAuth.getAuthorities()).isEqualTo(UPDATED_AUTHORITIES);
        assertThat(testUserAuth.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateUserAuthWithPatch() throws Exception {
        // Initialize the database
        userAuthRepository.saveAndFlush(userAuth);

        int databaseSizeBeforeUpdate = userAuthRepository.findAll().size();

        // Update the userAuth using partial update
        UserAuth partialUpdatedUserAuth = new UserAuth();
        partialUpdatedUserAuth.setId(userAuth.getId());

        partialUpdatedUserAuth
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .username(UPDATED_USERNAME)
            .authorities(UPDATED_AUTHORITIES)
            .password(UPDATED_PASSWORD);

        restUserAuthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAuth.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAuth))
            )
            .andExpect(status().isOk());

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
        UserAuth testUserAuth = userAuthList.get(userAuthList.size() - 1);
        assertThat(testUserAuth.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserAuth.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserAuth.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserAuth.getAuthorities()).isEqualTo(UPDATED_AUTHORITIES);
        assertThat(testUserAuth.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingUserAuth() throws Exception {
        int databaseSizeBeforeUpdate = userAuthRepository.findAll().size();
        userAuth.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAuthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAuth.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAuth))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAuth() throws Exception {
        int databaseSizeBeforeUpdate = userAuthRepository.findAll().size();
        userAuth.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAuthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAuth))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAuth() throws Exception {
        int databaseSizeBeforeUpdate = userAuthRepository.findAll().size();
        userAuth.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAuthMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userAuth)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAuth() throws Exception {
        // Initialize the database
        userAuthRepository.saveAndFlush(userAuth);

        int databaseSizeBeforeDelete = userAuthRepository.findAll().size();

        // Delete the userAuth
        restUserAuthMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAuth.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserAuth> userAuthList = userAuthRepository.findAll();
        assertThat(userAuthList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
