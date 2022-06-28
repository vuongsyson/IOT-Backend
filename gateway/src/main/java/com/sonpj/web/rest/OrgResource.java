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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.sonpj.domain.Org}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrgResource {

    private final Logger log = LoggerFactory.getLogger(OrgResource.class);

    private static final String ENTITY_NAME = "org";

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
    public Mono<ResponseEntity<Org>> createOrg(@RequestBody Org org) throws URISyntaxException {
        log.debug("REST request to save Org : {}", org);
        if (org.getId() != null) {
            throw new BadRequestAlertException("A new org cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return orgRepository
            .save(org)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/orgs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
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
    public Mono<ResponseEntity<Org>> updateOrg(@PathVariable(value = "id", required = false) final Long id, @RequestBody Org org)
        throws URISyntaxException {
        log.debug("REST request to update Org : {}, {}", id, org);
        if (org.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, org.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return orgRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return orgRepository
                    .save(org)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
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
    public Mono<ResponseEntity<Org>> partialUpdateOrg(@PathVariable(value = "id", required = false) final Long id, @RequestBody Org org)
        throws URISyntaxException {
        log.debug("REST request to partial update Org partially : {}, {}", id, org);
        if (org.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, org.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return orgRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Org> result = orgRepository
                    .findById(org.getId())
                    .map(existingOrg -> {
                        if (org.getOrgId() != null) {
                            existingOrg.setOrgId(org.getOrgId());
                        }

                        return existingOrg;
                    })
                    .flatMap(orgRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /orgs} : get all the orgs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orgs in body.
     */
    @GetMapping("/orgs")
    public Mono<List<Org>> getAllOrgs() {
        log.debug("REST request to get all Orgs");
        return orgRepository.findAll().collectList();
    }

    /**
     * {@code GET  /orgs} : get all the orgs as a stream.
     * @return the {@link Flux} of orgs.
     */
    @GetMapping(value = "/orgs", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Org> getAllOrgsAsStream() {
        log.debug("REST request to get all Orgs as a stream");
        return orgRepository.findAll();
    }

    /**
     * {@code GET  /orgs/:id} : get the "id" org.
     *
     * @param id the id of the org to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the org, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/orgs/{id}")
    public Mono<ResponseEntity<Org>> getOrg(@PathVariable Long id) {
        log.debug("REST request to get Org : {}", id);
        Mono<Org> org = orgRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(org);
    }

    /**
     * {@code DELETE  /orgs/:id} : delete the "id" org.
     *
     * @param id the id of the org to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/orgs/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteOrg(@PathVariable Long id) {
        log.debug("REST request to delete Org : {}", id);
        return orgRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
