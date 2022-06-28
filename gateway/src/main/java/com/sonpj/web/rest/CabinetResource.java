package com.sonpj.web.rest;

import com.sonpj.domain.Cabinet;
import com.sonpj.repository.CabinetRepository;
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
 * REST controller for managing {@link com.sonpj.domain.Cabinet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CabinetResource {

    private final Logger log = LoggerFactory.getLogger(CabinetResource.class);

    private static final String ENTITY_NAME = "cabinet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CabinetRepository cabinetRepository;

    public CabinetResource(CabinetRepository cabinetRepository) {
        this.cabinetRepository = cabinetRepository;
    }

    /**
     * {@code POST  /cabinets} : Create a new cabinet.
     *
     * @param cabinet the cabinet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cabinet, or with status {@code 400 (Bad Request)} if the cabinet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cabinets")
    public Mono<ResponseEntity<Cabinet>> createCabinet(@Valid @RequestBody Cabinet cabinet) throws URISyntaxException {
        log.debug("REST request to save Cabinet : {}", cabinet);
        if (cabinet.getId() != null) {
            throw new BadRequestAlertException("A new cabinet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return cabinetRepository
            .save(cabinet)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/cabinets/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /cabinets/:id} : Updates an existing cabinet.
     *
     * @param id the id of the cabinet to save.
     * @param cabinet the cabinet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cabinet,
     * or with status {@code 400 (Bad Request)} if the cabinet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cabinet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cabinets/{id}")
    public Mono<ResponseEntity<Cabinet>> updateCabinet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Cabinet cabinet
    ) throws URISyntaxException {
        log.debug("REST request to update Cabinet : {}, {}", id, cabinet);
        if (cabinet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cabinet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cabinetRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return cabinetRepository
                    .save(cabinet)
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
     * {@code PATCH  /cabinets/:id} : Partial updates given fields of an existing cabinet, field will ignore if it is null
     *
     * @param id the id of the cabinet to save.
     * @param cabinet the cabinet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cabinet,
     * or with status {@code 400 (Bad Request)} if the cabinet is not valid,
     * or with status {@code 404 (Not Found)} if the cabinet is not found,
     * or with status {@code 500 (Internal Server Error)} if the cabinet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cabinets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Cabinet>> partialUpdateCabinet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Cabinet cabinet
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cabinet partially : {}, {}", id, cabinet);
        if (cabinet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cabinet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cabinetRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Cabinet> result = cabinetRepository
                    .findById(cabinet.getId())
                    .map(existingCabinet -> {
                        if (cabinet.getBssId() != null) {
                            existingCabinet.setBssId(cabinet.getBssId());
                        }
                        if (cabinet.getBpId() != null) {
                            existingCabinet.setBpId(cabinet.getBpId());
                        }
                        if (cabinet.getBpReady() != null) {
                            existingCabinet.setBpReady(cabinet.getBpReady());
                        }
                        if (cabinet.getSwapNo() != null) {
                            existingCabinet.setSwapNo(cabinet.getSwapNo());
                        }
                        if (cabinet.getStateCode() != null) {
                            existingCabinet.setStateCode(cabinet.getStateCode());
                        }

                        return existingCabinet;
                    })
                    .flatMap(cabinetRepository::save);

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
     * {@code GET  /cabinets} : get all the cabinets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cabinets in body.
     */
    @GetMapping("/cabinets")
    public Mono<List<Cabinet>> getAllCabinets() {
        log.debug("REST request to get all Cabinets");
        return cabinetRepository.findAll().collectList();
    }

    /**
     * {@code GET  /cabinets} : get all the cabinets as a stream.
     * @return the {@link Flux} of cabinets.
     */
    @GetMapping(value = "/cabinets", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Cabinet> getAllCabinetsAsStream() {
        log.debug("REST request to get all Cabinets as a stream");
        return cabinetRepository.findAll();
    }

    /**
     * {@code GET  /cabinets/:id} : get the "id" cabinet.
     *
     * @param id the id of the cabinet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cabinet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cabinets/{id}")
    public Mono<ResponseEntity<Cabinet>> getCabinet(@PathVariable Long id) {
        log.debug("REST request to get Cabinet : {}", id);
        Mono<Cabinet> cabinet = cabinetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cabinet);
    }

    /**
     * {@code DELETE  /cabinets/:id} : delete the "id" cabinet.
     *
     * @param id the id of the cabinet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cabinets/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCabinet(@PathVariable Long id) {
        log.debug("REST request to delete Cabinet : {}", id);
        return cabinetRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
