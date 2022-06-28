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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sonpj.domain.BpSwapRecord}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BpSwapRecordResource {

    private final Logger log = LoggerFactory.getLogger(BpSwapRecordResource.class);

    private static final String ENTITY_NAME = "swapApiServiceBpSwapRecord";

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
    public ResponseEntity<BpSwapRecord> createBpSwapRecord(@RequestBody BpSwapRecord bpSwapRecord) throws URISyntaxException {
        log.debug("REST request to save BpSwapRecord : {}", bpSwapRecord);
        if (bpSwapRecord.getId() != null) {
            throw new BadRequestAlertException("A new bpSwapRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BpSwapRecord result = bpSwapRecordRepository.save(bpSwapRecord);
        return ResponseEntity
            .created(new URI("/api/bp-swap-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<BpSwapRecord> updateBpSwapRecord(
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

        if (!bpSwapRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BpSwapRecord result = bpSwapRecordRepository.save(bpSwapRecord);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bpSwapRecord.getId().toString()))
            .body(result);
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
    public ResponseEntity<BpSwapRecord> partialUpdateBpSwapRecord(
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

        if (!bpSwapRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BpSwapRecord> result = bpSwapRecordRepository
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
            .map(bpSwapRecordRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bpSwapRecord.getId().toString())
        );
    }

    /**
     * {@code GET  /bp-swap-records} : get all the bpSwapRecords.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bpSwapRecords in body.
     */
    @GetMapping("/bp-swap-records")
    public List<BpSwapRecord> getAllBpSwapRecords() {
        log.debug("REST request to get all BpSwapRecords");
        return bpSwapRecordRepository.findAll();
    }

    /**
     * {@code GET  /bp-swap-records/:id} : get the "id" bpSwapRecord.
     *
     * @param id the id of the bpSwapRecord to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bpSwapRecord, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bp-swap-records/{id}")
    public ResponseEntity<BpSwapRecord> getBpSwapRecord(@PathVariable Long id) {
        log.debug("REST request to get BpSwapRecord : {}", id);
        Optional<BpSwapRecord> bpSwapRecord = bpSwapRecordRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bpSwapRecord);
    }

    /**
     * {@code DELETE  /bp-swap-records/:id} : delete the "id" bpSwapRecord.
     *
     * @param id the id of the bpSwapRecord to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bp-swap-records/{id}")
    public ResponseEntity<Void> deleteBpSwapRecord(@PathVariable Long id) {
        log.debug("REST request to delete BpSwapRecord : {}", id);
        bpSwapRecordRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
