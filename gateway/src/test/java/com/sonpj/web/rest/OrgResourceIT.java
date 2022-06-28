package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.Org;
import com.sonpj.repository.EntityManager;
import com.sonpj.repository.OrgRepository;
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
 * Integration tests for the {@link OrgResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class OrgResourceIT {

    private static final Long DEFAULT_ORG_ID = 1L;
    private static final Long UPDATED_ORG_ID = 2L;

    private static final String ENTITY_API_URL = "/api/orgs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Org org;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Org createEntity(EntityManager em) {
        Org org = new Org().orgId(DEFAULT_ORG_ID);
        return org;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Org createUpdatedEntity(EntityManager em) {
        Org org = new Org().orgId(UPDATED_ORG_ID);
        return org;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Org.class).block();
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
        org = createEntity(em);
    }

    @Test
    void createOrg() throws Exception {
        int databaseSizeBeforeCreate = orgRepository.findAll().collectList().block().size();
        // Create the Org
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(org))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll().collectList().block();
        assertThat(orgList).hasSize(databaseSizeBeforeCreate + 1);
        Org testOrg = orgList.get(orgList.size() - 1);
        assertThat(testOrg.getOrgId()).isEqualTo(DEFAULT_ORG_ID);
    }

    @Test
    void createOrgWithExistingId() throws Exception {
        // Create the Org with an existing ID
        org.setId(1L);

        int databaseSizeBeforeCreate = orgRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(org))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll().collectList().block();
        assertThat(orgList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllOrgsAsStream() {
        // Initialize the database
        orgRepository.save(org).block();

        List<Org> orgList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Org.class)
            .getResponseBody()
            .filter(org::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(orgList).isNotNull();
        assertThat(orgList).hasSize(1);
        Org testOrg = orgList.get(0);
        assertThat(testOrg.getOrgId()).isEqualTo(DEFAULT_ORG_ID);
    }

    @Test
    void getAllOrgs() {
        // Initialize the database
        orgRepository.save(org).block();

        // Get all the orgList
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
            .value(hasItem(org.getId().intValue()))
            .jsonPath("$.[*].orgId")
            .value(hasItem(DEFAULT_ORG_ID.intValue()));
    }

    @Test
    void getOrg() {
        // Initialize the database
        orgRepository.save(org).block();

        // Get the org
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, org.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(org.getId().intValue()))
            .jsonPath("$.orgId")
            .value(is(DEFAULT_ORG_ID.intValue()));
    }

    @Test
    void getNonExistingOrg() {
        // Get the org
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewOrg() throws Exception {
        // Initialize the database
        orgRepository.save(org).block();

        int databaseSizeBeforeUpdate = orgRepository.findAll().collectList().block().size();

        // Update the org
        Org updatedOrg = orgRepository.findById(org.getId()).block();
        updatedOrg.orgId(UPDATED_ORG_ID);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedOrg.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedOrg))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll().collectList().block();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
        Org testOrg = orgList.get(orgList.size() - 1);
        assertThat(testOrg.getOrgId()).isEqualTo(UPDATED_ORG_ID);
    }

    @Test
    void putNonExistingOrg() throws Exception {
        int databaseSizeBeforeUpdate = orgRepository.findAll().collectList().block().size();
        org.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, org.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(org))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll().collectList().block();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOrg() throws Exception {
        int databaseSizeBeforeUpdate = orgRepository.findAll().collectList().block().size();
        org.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(org))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll().collectList().block();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOrg() throws Exception {
        int databaseSizeBeforeUpdate = orgRepository.findAll().collectList().block().size();
        org.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(org))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll().collectList().block();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOrgWithPatch() throws Exception {
        // Initialize the database
        orgRepository.save(org).block();

        int databaseSizeBeforeUpdate = orgRepository.findAll().collectList().block().size();

        // Update the org using partial update
        Org partialUpdatedOrg = new Org();
        partialUpdatedOrg.setId(org.getId());

        partialUpdatedOrg.orgId(UPDATED_ORG_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrg.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrg))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll().collectList().block();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
        Org testOrg = orgList.get(orgList.size() - 1);
        assertThat(testOrg.getOrgId()).isEqualTo(UPDATED_ORG_ID);
    }

    @Test
    void fullUpdateOrgWithPatch() throws Exception {
        // Initialize the database
        orgRepository.save(org).block();

        int databaseSizeBeforeUpdate = orgRepository.findAll().collectList().block().size();

        // Update the org using partial update
        Org partialUpdatedOrg = new Org();
        partialUpdatedOrg.setId(org.getId());

        partialUpdatedOrg.orgId(UPDATED_ORG_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrg.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrg))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll().collectList().block();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
        Org testOrg = orgList.get(orgList.size() - 1);
        assertThat(testOrg.getOrgId()).isEqualTo(UPDATED_ORG_ID);
    }

    @Test
    void patchNonExistingOrg() throws Exception {
        int databaseSizeBeforeUpdate = orgRepository.findAll().collectList().block().size();
        org.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, org.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(org))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll().collectList().block();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOrg() throws Exception {
        int databaseSizeBeforeUpdate = orgRepository.findAll().collectList().block().size();
        org.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(org))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll().collectList().block();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOrg() throws Exception {
        int databaseSizeBeforeUpdate = orgRepository.findAll().collectList().block().size();
        org.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(org))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Org in the database
        List<Org> orgList = orgRepository.findAll().collectList().block();
        assertThat(orgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOrg() {
        // Initialize the database
        orgRepository.save(org).block();

        int databaseSizeBeforeDelete = orgRepository.findAll().collectList().block().size();

        // Delete the org
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, org.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Org> orgList = orgRepository.findAll().collectList().block();
        assertThat(orgList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
