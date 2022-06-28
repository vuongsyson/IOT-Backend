package com.sonpj.web.rest;

import com.sonpj.domain.RentalHistory;
import com.sonpj.repository.RentalHistoryRepository;
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
 * REST controller for managing {@link com.sonpj.domain.RentalHistory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RentalHistoryResource {

    private final Logger log = LoggerFactory.getLogger(RentalHistoryResource.class);

    private static final String ENTITY_NAME = "rentalHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RentalHistoryRepository rentalHistoryRepository;

    public RentalHistoryResource(RentalHistoryRepository rentalHistoryRepository) {
        this.rentalHistoryRepository = rentalHistoryRepository;
    }

    /**
     * {@code POST  /rental-histories} : Create a new rentalHistory.
     *
     * @param rentalHistory the rentalHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rentalHistory, or with status {@code 400 (Bad Request)} if the rentalHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rental-histories")
    public Mono<ResponseEntity<RentalHistory>> createRentalHistory(@RequestBody RentalHistory rentalHistory) throws URISyntaxException {
        log.debug("REST request to save RentalHistory : {}", rentalHistory);
        if (rentalHistory.getId() != null) {
            throw new BadRequestAlertException("A new rentalHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return rentalHistoryRepository
            .save(rentalHistory)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/rental-histories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /rental-histories/:id} : Updates an existing rentalHistory.
     *
     * @param id the id of the rentalHistory to save.
     * @param rentalHistory the rentalHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rentalHistory,
     * or with status {@code 400 (Bad Request)} if the rentalHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rentalHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rental-histories/{id}")
    public Mono<ResponseEntity<RentalHistory>> updateRentalHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RentalHistory rentalHistory
    ) throws URISyntaxException {
        log.debug("REST request to update RentalHistory : {}, {}", id, rentalHistory);
        if (rentalHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rentalHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return rentalHistoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return rentalHistoryRepository
                    .save(rentalHistory)
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
     * {@code PATCH  /rental-histories/:id} : Partial updates given fields of an existing rentalHistory, field will ignore if it is null
     *
     * @param id the id of the rentalHistory to save.
     * @param rentalHistory the rentalHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rentalHistory,
     * or with status {@code 400 (Bad Request)} if the rentalHistory is not valid,
     * or with status {@code 404 (Not Found)} if the rentalHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the rentalHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rental-histories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RentalHistory>> partialUpdateRentalHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RentalHistory rentalHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update RentalHistory partially : {}, {}", id, rentalHistory);
        if (rentalHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rentalHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return rentalHistoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RentalHistory> result = rentalHistoryRepository
                    .findById(rentalHistory.getId())
                    .map(existingRentalHistory -> {
                        if (rentalHistory.getUserId() != null) {
                            existingRentalHistory.setUserId(rentalHistory.getUserId());
                        }
                        if (rentalHistory.getBatteryId() != null) {
                            existingRentalHistory.setBatteryId(rentalHistory.getBatteryId());
                        }
                        if (rentalHistory.getTimeStart() != null) {
                            existingRentalHistory.setTimeStart(rentalHistory.getTimeStart());
                        }
                        if (rentalHistory.getTimeEnd() != null) {
                            existingRentalHistory.setTimeEnd(rentalHistory.getTimeEnd());
                        }

                        return existingRentalHistory;
                    })
                    .flatMap(rentalHistoryRepository::save);

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
     * {@code GET  /rental-histories} : get all the rentalHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rentalHistories in body.
     */
    @GetMapping("/rental-histories")
    public Mono<List<RentalHistory>> getAllRentalHistories() {
        log.debug("REST request to get all RentalHistories");
        return rentalHistoryRepository.findAll().collectList();
    }

    /**
     * {@code GET  /rental-histories} : get all the rentalHistories as a stream.
     * @return the {@link Flux} of rentalHistories.
     */
    @GetMapping(value = "/rental-histories", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<RentalHistory> getAllRentalHistoriesAsStream() {
        log.debug("REST request to get all RentalHistories as a stream");
        return rentalHistoryRepository.findAll();
    }

    /**
     * {@code GET  /rental-histories/:id} : get the "id" rentalHistory.
     *
     * @param id the id of the rentalHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rentalHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rental-histories/{id}")
    public Mono<ResponseEntity<RentalHistory>> getRentalHistory(@PathVariable Long id) {
        log.debug("REST request to get RentalHistory : {}", id);
        Mono<RentalHistory> rentalHistory = rentalHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rentalHistory);
    }

    /**
     * {@code DELETE  /rental-histories/:id} : delete the "id" rentalHistory.
     *
     * @param id the id of the rentalHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rental-histories/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteRentalHistory(@PathVariable Long id) {
        log.debug("REST request to delete RentalHistory : {}", id);
        return rentalHistoryRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
