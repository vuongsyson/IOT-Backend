package com.sonpj.web.rest;

import com.sonpj.domain.SosType;
import com.sonpj.repository.SosTypeRepository;
import com.sonpj.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.sonpj.domain.SosType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SosTypeResource {

    private final Logger log = LoggerFactory.getLogger(SosTypeResource.class);

    private static final String ENTITY_NAME = "sosType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SosTypeRepository sosTypeRepository;

    public SosTypeResource(SosTypeRepository sosTypeRepository) {
        this.sosTypeRepository = sosTypeRepository;
    }

    /**
     * {@code POST  /sos-types} : Create a new sosType.
     *
     * @param sosType the sosType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sosType, or with status {@code 400 (Bad Request)} if the sosType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sos-types")
    public Mono<ResponseEntity<SosType>> createSosType(@Valid @RequestBody SosType sosType) throws URISyntaxException {
        log.debug("REST request to save SosType : {}", sosType);
        if (sosType.getId() != null) {
            throw new BadRequestAlertException("A new sosType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return sosTypeRepository
            .save(sosType)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/sos-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /sos-types/:id} : Updates an existing sosType.
     *
     * @param id the id of the sosType to save.
     * @param sosType the sosType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sosType,
     * or with status {@code 400 (Bad Request)} if the sosType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sosType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sos-types/{id}")
    public Mono<ResponseEntity<SosType>> updateSosType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SosType sosType
    ) throws URISyntaxException {
        log.debug("REST request to update SosType : {}, {}", id, sosType);
        if (sosType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sosType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sosTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return sosTypeRepository
                    .save(sosType)
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
     * {@code PATCH  /sos-types/:id} : Partial updates given fields of an existing sosType, field will ignore if it is null
     *
     * @param id the id of the sosType to save.
     * @param sosType the sosType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sosType,
     * or with status {@code 400 (Bad Request)} if the sosType is not valid,
     * or with status {@code 404 (Not Found)} if the sosType is not found,
     * or with status {@code 500 (Internal Server Error)} if the sosType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sos-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SosType>> partialUpdateSosType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SosType sosType
    ) throws URISyntaxException {
        log.debug("REST request to partial update SosType partially : {}, {}", id, sosType);
        if (sosType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sosType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sosTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SosType> result = sosTypeRepository
                    .findById(sosType.getId())
                    .map(existingSosType -> {
                        if (sosType.getName() != null) {
                            existingSosType.setName(sosType.getName());
                        }

                        return existingSosType;
                    })
                    .flatMap(sosTypeRepository::save);

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
     * {@code GET  /sos-types} : get all the sosTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sosTypes in body.
     */
    @GetMapping("/sos-types")
    public Mono<List<SosType>> getAllSosTypes() {
        log.debug("REST request to get all SosTypes");
        return sosTypeRepository.findAll().collectList();
    }

    /**
     * {@code GET  /sos-types} : get all the sosTypes as a stream.
     * @return the {@link Flux} of sosTypes.
     */
    @GetMapping(value = "/sos-types", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SosType> getAllSosTypesAsStream() {
        log.debug("REST request to get all SosTypes as a stream");
        return sosTypeRepository.findAll();
    }

    /**
     * {@code GET  /sos-types/:id} : get the "id" sosType.
     *
     * @param id the id of the sosType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sosType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sos-types/{id}")
    public Mono<ResponseEntity<SosType>> getSosType(@PathVariable Long id) {
        log.debug("REST request to get SosType : {}", id);
        Mono<SosType> sosType = sosTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sosType);
    }

    /**
     * {@code DELETE  /sos-types/:id} : delete the "id" sosType.
     *
     * @param id the id of the sosType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sos-types/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSosType(@PathVariable Long id) {
        log.debug("REST request to delete SosType : {}", id);
        return sosTypeRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
