package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.UserAuth;
import com.sonpj.repository.EntityManager;
import com.sonpj.repository.UserAuthRepository;
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
 * Integration tests for the {@link UserAuthResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserAuth.class).block();
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
        userAuth = createEntity(em);
    }

    @Test
    void createUserAuth() throws Exception {
        int databaseSizeBeforeCreate = userAuthRepository.findAll().collectList().block().size();
        // Create the UserAuth
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAuth))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll().collectList().block();
        assertThat(userAuthList).hasSize(databaseSizeBeforeCreate + 1);
        UserAuth testUserAuth = userAuthList.get(userAuthList.size() - 1);
        assertThat(testUserAuth.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserAuth.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserAuth.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUserAuth.getAuthorities()).isEqualTo(DEFAULT_AUTHORITIES);
        assertThat(testUserAuth.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    void createUserAuthWithExistingId() throws Exception {
        // Create the UserAuth with an existing ID
        userAuth.setId(1L);

        int databaseSizeBeforeCreate = userAuthRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAuth))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll().collectList().block();
        assertThat(userAuthList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllUserAuthsAsStream() {
        // Initialize the database
        userAuthRepository.save(userAuth).block();

        List<UserAuth> userAuthList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(UserAuth.class)
            .getResponseBody()
            .filter(userAuth::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(userAuthList).isNotNull();
        assertThat(userAuthList).hasSize(1);
        UserAuth testUserAuth = userAuthList.get(0);
        assertThat(testUserAuth.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserAuth.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserAuth.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUserAuth.getAuthorities()).isEqualTo(DEFAULT_AUTHORITIES);
        assertThat(testUserAuth.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    void getAllUserAuths() {
        // Initialize the database
        userAuthRepository.save(userAuth).block();

        // Get all the userAuthList
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
            .value(hasItem(userAuth.getId().intValue()))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].phone")
            .value(hasItem(DEFAULT_PHONE))
            .jsonPath("$.[*].username")
            .value(hasItem(DEFAULT_USERNAME))
            .jsonPath("$.[*].authorities")
            .value(hasItem(DEFAULT_AUTHORITIES))
            .jsonPath("$.[*].password")
            .value(hasItem(DEFAULT_PASSWORD));
    }

    @Test
    void getUserAuth() {
        // Initialize the database
        userAuthRepository.save(userAuth).block();

        // Get the userAuth
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userAuth.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userAuth.getId().intValue()))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.phone")
            .value(is(DEFAULT_PHONE))
            .jsonPath("$.username")
            .value(is(DEFAULT_USERNAME))
            .jsonPath("$.authorities")
            .value(is(DEFAULT_AUTHORITIES))
            .jsonPath("$.password")
            .value(is(DEFAULT_PASSWORD));
    }

    @Test
    void getNonExistingUserAuth() {
        // Get the userAuth
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewUserAuth() throws Exception {
        // Initialize the database
        userAuthRepository.save(userAuth).block();

        int databaseSizeBeforeUpdate = userAuthRepository.findAll().collectList().block().size();

        // Update the userAuth
        UserAuth updatedUserAuth = userAuthRepository.findById(userAuth.getId()).block();
        updatedUserAuth
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .username(UPDATED_USERNAME)
            .authorities(UPDATED_AUTHORITIES)
            .password(UPDATED_PASSWORD);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedUserAuth.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedUserAuth))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll().collectList().block();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
        UserAuth testUserAuth = userAuthList.get(userAuthList.size() - 1);
        assertThat(testUserAuth.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserAuth.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserAuth.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserAuth.getAuthorities()).isEqualTo(UPDATED_AUTHORITIES);
        assertThat(testUserAuth.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    void putNonExistingUserAuth() throws Exception {
        int databaseSizeBeforeUpdate = userAuthRepository.findAll().collectList().block().size();
        userAuth.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userAuth.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAuth))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll().collectList().block();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserAuth() throws Exception {
        int databaseSizeBeforeUpdate = userAuthRepository.findAll().collectList().block().size();
        userAuth.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAuth))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll().collectList().block();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserAuth() throws Exception {
        int databaseSizeBeforeUpdate = userAuthRepository.findAll().collectList().block().size();
        userAuth.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAuth))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll().collectList().block();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserAuthWithPatch() throws Exception {
        // Initialize the database
        userAuthRepository.save(userAuth).block();

        int databaseSizeBeforeUpdate = userAuthRepository.findAll().collectList().block().size();

        // Update the userAuth using partial update
        UserAuth partialUpdatedUserAuth = new UserAuth();
        partialUpdatedUserAuth.setId(userAuth.getId());

        partialUpdatedUserAuth.username(UPDATED_USERNAME).authorities(UPDATED_AUTHORITIES);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserAuth.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAuth))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll().collectList().block();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
        UserAuth testUserAuth = userAuthList.get(userAuthList.size() - 1);
        assertThat(testUserAuth.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserAuth.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserAuth.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserAuth.getAuthorities()).isEqualTo(UPDATED_AUTHORITIES);
        assertThat(testUserAuth.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    void fullUpdateUserAuthWithPatch() throws Exception {
        // Initialize the database
        userAuthRepository.save(userAuth).block();

        int databaseSizeBeforeUpdate = userAuthRepository.findAll().collectList().block().size();

        // Update the userAuth using partial update
        UserAuth partialUpdatedUserAuth = new UserAuth();
        partialUpdatedUserAuth.setId(userAuth.getId());

        partialUpdatedUserAuth
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .username(UPDATED_USERNAME)
            .authorities(UPDATED_AUTHORITIES)
            .password(UPDATED_PASSWORD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserAuth.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAuth))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll().collectList().block();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
        UserAuth testUserAuth = userAuthList.get(userAuthList.size() - 1);
        assertThat(testUserAuth.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserAuth.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserAuth.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserAuth.getAuthorities()).isEqualTo(UPDATED_AUTHORITIES);
        assertThat(testUserAuth.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    void patchNonExistingUserAuth() throws Exception {
        int databaseSizeBeforeUpdate = userAuthRepository.findAll().collectList().block().size();
        userAuth.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userAuth.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAuth))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll().collectList().block();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserAuth() throws Exception {
        int databaseSizeBeforeUpdate = userAuthRepository.findAll().collectList().block().size();
        userAuth.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAuth))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll().collectList().block();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserAuth() throws Exception {
        int databaseSizeBeforeUpdate = userAuthRepository.findAll().collectList().block().size();
        userAuth.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAuth))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserAuth in the database
        List<UserAuth> userAuthList = userAuthRepository.findAll().collectList().block();
        assertThat(userAuthList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserAuth() {
        // Initialize the database
        userAuthRepository.save(userAuth).block();

        int databaseSizeBeforeDelete = userAuthRepository.findAll().collectList().block().size();

        // Delete the userAuth
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userAuth.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserAuth> userAuthList = userAuthRepository.findAll().collectList().block();
        assertThat(userAuthList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
