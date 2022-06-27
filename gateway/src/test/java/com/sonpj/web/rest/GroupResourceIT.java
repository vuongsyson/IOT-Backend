package com.sonpj.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sonpj.IntegrationTest;
import com.sonpj.domain.Group;
import com.sonpj.repository.EntityManager;
import com.sonpj.repository.GroupRepository;
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
 * Integration tests for the {@link GroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class GroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Group group;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Group createEntity(EntityManager em) {
        Group group = new Group().name(DEFAULT_NAME);
        return group;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Group createUpdatedEntity(EntityManager em) {
        Group group = new Group().name(UPDATED_NAME);
        return group;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Group.class).block();
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
        group = createEntity(em);
    }

    @Test
    void createGroup() throws Exception {
        int databaseSizeBeforeCreate = groupRepository.findAll().collectList().block().size();
        // Create the Group
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(group))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll().collectList().block();
        assertThat(groupList).hasSize(databaseSizeBeforeCreate + 1);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createGroupWithExistingId() throws Exception {
        // Create the Group with an existing ID
        group.setId(1L);

        int databaseSizeBeforeCreate = groupRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(group))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll().collectList().block();
        assertThat(groupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupRepository.findAll().collectList().block().size();
        // set the field null
        group.setName(null);

        // Create the Group, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(group))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Group> groupList = groupRepository.findAll().collectList().block();
        assertThat(groupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllGroupsAsStream() {
        // Initialize the database
        groupRepository.save(group).block();

        List<Group> groupList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Group.class)
            .getResponseBody()
            .filter(group::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(groupList).isNotNull();
        assertThat(groupList).hasSize(1);
        Group testGroup = groupList.get(0);
        assertThat(testGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllGroups() {
        // Initialize the database
        groupRepository.save(group).block();

        // Get all the groupList
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
            .value(hasItem(group.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getGroup() {
        // Initialize the database
        groupRepository.save(group).block();

        // Get the group
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, group.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(group.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingGroup() {
        // Get the group
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewGroup() throws Exception {
        // Initialize the database
        groupRepository.save(group).block();

        int databaseSizeBeforeUpdate = groupRepository.findAll().collectList().block().size();

        // Update the group
        Group updatedGroup = groupRepository.findById(group.getId()).block();
        updatedGroup.name(UPDATED_NAME);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedGroup.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedGroup))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll().collectList().block();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().collectList().block().size();
        group.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, group.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(group))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll().collectList().block();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().collectList().block().size();
        group.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(group))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll().collectList().block();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().collectList().block().size();
        group.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(group))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll().collectList().block();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateGroupWithPatch() throws Exception {
        // Initialize the database
        groupRepository.save(group).block();

        int databaseSizeBeforeUpdate = groupRepository.findAll().collectList().block().size();

        // Update the group using partial update
        Group partialUpdatedGroup = new Group();
        partialUpdatedGroup.setId(group.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGroup.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGroup))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll().collectList().block();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void fullUpdateGroupWithPatch() throws Exception {
        // Initialize the database
        groupRepository.save(group).block();

        int databaseSizeBeforeUpdate = groupRepository.findAll().collectList().block().size();

        // Update the group using partial update
        Group partialUpdatedGroup = new Group();
        partialUpdatedGroup.setId(group.getId());

        partialUpdatedGroup.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGroup.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGroup))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll().collectList().block();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().collectList().block().size();
        group.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, group.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(group))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll().collectList().block();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().collectList().block().size();
        group.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(group))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll().collectList().block();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().collectList().block().size();
        group.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(group))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll().collectList().block();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteGroup() {
        // Initialize the database
        groupRepository.save(group).block();

        int databaseSizeBeforeDelete = groupRepository.findAll().collectList().block().size();

        // Delete the group
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, group.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Group> groupList = groupRepository.findAll().collectList().block();
        assertThat(groupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
