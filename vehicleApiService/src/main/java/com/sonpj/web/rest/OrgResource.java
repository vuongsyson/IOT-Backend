package com.sonpj.web.rest;

import com.sonpj.domain.Org;
import com.sonpj.repository.OrgRepository;
import com.sonpj.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sonpj.domain.Org}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrgResource {

    private final Logger log = LoggerFactory.getLogger(OrgResource.class);

    private static final String ENTITY_NAME = "vehicleApiServiceOrg";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrgRepository orgRepository;

    public OrgResource(OrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    /**
     * {@code POST  /orgs} : Create a new org.
     *
     * @param org the org to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new org, or with status {@code 400 (Bad Request)} if the org has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/orgs")
    public ResponseEntity<Org> createOrg(@RequestBody Org org) throws URISyntaxException {
        log.debug("REST request to save Org : {}", org);
        if (org.getId() != null) {
            throw new BadRequestAlertException("A new org cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Org result = orgRepository.save(org);
        return ResponseEntity
            .created(new URI("/api/orgs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /orgs/:id} : Updates an existing org.
     *
     * @param id the id of the org to save.
     * @param org the org to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated org,
     * or with status {@code 400 (Bad Request)} if the org is not valid,
     * or with status {@code 500 (Internal Server Error)} if the org couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/orgs/{id}")
    public ResponseEntity<Org> updateOrg(@PathVariable(value = "id", required = false) final Long id, @RequestBody Org org)
        throws URISyntaxException {
        log.debug("REST request to update Org : {}, {}", id, org);
        if (org.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, org.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Org result = orgRepository.save(org);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, org.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /orgs/:id} : Partial updates given fields of an existing org, field will ignore if it is null
     *
     * @param id the id of the org to save.
     * @param org the org to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated org,
     * or with status {@code 400 (Bad Request)} if the org is not valid,
     * or with status {@code 404 (Not Found)} if the org is not found,
     * or with status {@code 500 (Internal Server Error)} if the org couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/orgs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Org> partialUpdateOrg(@PathVariable(value = "id", required = false) final Long id, @RequestBody Org org)
        throws URISyntaxException {
        log.debug("REST request to partial update Org partially : {}, {}", id, org);
        if (org.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, org.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Org> result = orgRepository
            .findById(org.getId())
            .map(existingOrg -> {
                if (org.getOrgId() != null) {
                    existingOrg.setOrgId(org.getOrgId());
                }

                return existingOrg;
            })
            .map(orgRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, org.getId().toString())
        );
    }

    /**
     * {@code GET  /orgs} : get all the orgs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orgs in body.
     */
    @GetMapping("/orgs")
    public List<Org> getAllOrgs() {
        log.debug("REST request to get all Orgs");
        return orgRepository.findAll();
    }

    /**
     * {@code GET  /orgs/:id} : get the "id" org.
     *
     * @param id the id of the org to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the org, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/orgs/{id}")
    public ResponseEntity<Org> getOrg(@PathVariable Long id) {
        log.debug("REST request to get Org : {}", id);
        Optional<Org> org = orgRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(org);
    }

    /**
     * {@code DELETE  /orgs/:id} : delete the "id" org.
     *
     * @param id the id of the org to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/orgs/{id}")
    public ResponseEntity<Void> deleteOrg(@PathVariable Long id) {
        log.debug("REST request to delete Org : {}", id);
        orgRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
