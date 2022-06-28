package com.sonpj.web.rest;

import com.sonpj.domain.BpSwapRecord;
import com.sonpj.repository.BpSwapRecordRepository;
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
 * REST controller for managing {@link com.sonpj.domain.BpSwapRecord}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BpSwapRecordResource {

    private final Logger log = LoggerFactory.getLogger(BpSwapRecordResource.class);

    private static final String ENTITY_NAME = "bpSwapRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BpSwapRecordRepository bpSwapRecordRepository;

    public BpSwapRecordResource(BpSwapRecordRepository bpSwapRecordRepository) {
        this.bpSwapRecordRepository = bpSwapRecordRepository;
    }

    /**
     * {@code POST  /bp-swap-records} : Create a new bpSwapRecord.
     *
     * @param bpSwapRecord the bpSwapRecord to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bpSwapRecord, or with status {@code 400 (Bad Request)} if the bpSwapRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bp-swap-records")
    public Mono<ResponseEntity<BpSwapRecord>> createBpSwapRecord(@RequestBody BpSwapRecord bpSwapRecord) throws URISyntaxException {
        log.debug("REST request to save BpSwapRecord : {}", bpSwapRecord);
        if (bpSwapRecord.getId() != null) {
            throw new BadRequestAlertException("A new bpSwapRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return bpSwapRecordRepository
            .save(bpSwapRecord)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/bp-swap-records/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /bp-swap-records/:id} : Updates an existing bpSwapRecord.
     *
     * @param id the id of the bpSwapRecord to save.
     * @param bpSwapRecord the bpSwapRecord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bpSwapRecord,
     * or with status {@code 400 (Bad Request)} if the bpSwapRecord is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bpSwapRecord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bp-swap-records/{id}")
    public Mono<ResponseEntity<BpSwapRecord>> updateBpSwapRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BpSwapRecord bpSwapRecord
    ) throws URISyntaxException {
        log.debug("REST request to update BpSwapRecord : {}, {}", id, bpSwapRecord);
        if (bpSwapRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bpSwapRecord.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bpSwapRecordRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return bpSwapRecordRepository
                    .save(bpSwapRecord)
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
     * {@code PATCH  /bp-swap-records/:id} : Partial updates given fields of an existing bpSwapRecord, field will ignore if it is null
     *
     * @param id the id of the bpSwapRecord to save.
     * @param bpSwapRecord the bpSwapRecord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bpSwapRecord,
     * or with status {@code 400 (Bad Request)} if the bpSwapRecord is not valid,
     * or with status {@code 404 (Not Found)} if the bpSwapRecord is not found,
     * or with status {@code 500 (Internal Server Error)} if the bpSwapRecord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bp-swap-records/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<BpSwapRecord>> partialUpdateBpSwapRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BpSwapRecord bpSwapRecord
    ) throws URISyntaxException {
        log.debug("REST request to partial update BpSwapRecord partially : {}, {}", id, bpSwapRecord);
        if (bpSwapRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bpSwapRecord.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bpSwapRecordRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<BpSwapRecord> result = bpSwapRecordRepository
                    .findById(bpSwapRecord.getId())
                    .map(existingBpSwapRecord -> {
                        if (bpSwapRecord.getOldBat() != null) {
                            existingBpSwapRecord.setOldBat(bpSwapRecord.getOldBat());
                        }
                        if (bpSwapRecord.getNewBat() != null) {
                            existingBpSwapRecord.setNewBat(bpSwapRecord.getNewBat());
                        }
                        if (bpSwapRecord.getOldCab() != null) {
                            existingBpSwapRecord.setOldCab(bpSwapRecord.getOldCab());
                        }
                        if (bpSwapRecord.getNewCab() != null) {
                            existingBpSwapRecord.setNewCab(bpSwapRecord.getNewCab());
                        }
                        if (bpSwapRecord.getBss() != null) {
                            existingBpSwapRecord.setBss(bpSwapRecord.getBss());
                        }
                        if (bpSwapRecord.getUser() != null) {
                            existingBpSwapRecord.setUser(bpSwapRecord.getUser());
                        }
                        if (bpSwapRecord.getState() != null) {
                            existingBpSwapRecord.setState(bpSwapRecord.getState());
                        }
                        if (bpSwapRecord.getError() != null) {
                            existingBpSwapRecord.setError(bpSwapRecord.getError());
                        }

                        return existingBpSwapRecord;
                    })
                    .flatMap(bpSwapRecordRepository::save);

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
     * {@code GET  /bp-swap-records} : get all the bpSwapRecords.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bpSwapRecords in body.
     */
    @GetMapping("/bp-swap-records")
    public Mono<List<BpSwapRecord>> getAllBpSwapRecords() {
        log.debug("REST request to get all BpSwapRecords");
        return bpSwapRecordRepository.findAll().collectList();
    }

    /**
     * {@code GET  /bp-swap-records} : get all the bpSwapRecords as a stream.
     * @return the {@link Flux} of bpSwapRecords.
     */
    @GetMapping(value = "/bp-swap-records", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<BpSwapRecord> getAllBpSwapRecordsAsStream() {
        log.debug("REST request to get all BpSwapRecords as a stream");
        return bpSwapRecordRepository.findAll();
    }

    /**
     * {@code GET  /bp-swap-records/:id} : get the "id" bpSwapRecord.
     *
     * @param id the id of the bpSwapRecord to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bpSwapRecord, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bp-swap-records/{id}")
    public Mono<ResponseEntity<BpSwapRecord>> getBpSwapRecord(@PathVariable Long id) {
        log.debug("REST request to get BpSwapRecord : {}", id);
        Mono<BpSwapRecord> bpSwapRecord = bpSwapRecordRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bpSwapRecord);
    }

    /**
     * {@code DELETE  /bp-swap-records/:id} : delete the "id" bpSwapRecord.
     *
     * @param id the id of the bpSwapRecord to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bp-swap-records/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBpSwapRecord(@PathVariable Long id) {
        log.debug("REST request to delete BpSwapRecord : {}", id);
        return bpSwapRecordRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
