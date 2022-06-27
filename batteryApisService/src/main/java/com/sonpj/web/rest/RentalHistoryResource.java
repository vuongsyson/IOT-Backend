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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sonpj.domain.RentalHistory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RentalHistoryResource {

    private final Logger log = LoggerFactory.getLogger(RentalHistoryResource.class);

    private static final String ENTITY_NAME = "batteryApisServiceRentalHistory";

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
    public ResponseEntity<RentalHistory> createRentalHistory(@RequestBody RentalHistory rentalHistory) throws URISyntaxException {
        log.debug("REST request to save RentalHistory : {}", rentalHistory);
        if (rentalHistory.getId() != null) {
            throw new BadRequestAlertException("A new rentalHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RentalHistory result = rentalHistoryRepository.save(rentalHistory);
        return ResponseEntity
            .created(new URI("/api/rental-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<RentalHistory> updateRentalHistory(
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

        if (!rentalHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RentalHistory result = rentalHistoryRepository.save(rentalHistory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rentalHistory.getId().toString()))
            .body(result);
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
    public ResponseEntity<RentalHistory> partialUpdateRentalHistory(
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

        if (!rentalHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RentalHistory> result = rentalHistoryRepository
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
            .map(rentalHistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rentalHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /rental-histories} : get all the rentalHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rentalHistories in body.
     */
    @GetMapping("/rental-histories")
    public List<RentalHistory> getAllRentalHistories() {
        log.debug("REST request to get all RentalHistories");
        return rentalHistoryRepository.findAll();
    }

    /**
     * {@code GET  /rental-histories/:id} : get the "id" rentalHistory.
     *
     * @param id the id of the rentalHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rentalHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rental-histories/{id}")
    public ResponseEntity<RentalHistory> getRentalHistory(@PathVariable Long id) {
        log.debug("REST request to get RentalHistory : {}", id);
        Optional<RentalHistory> rentalHistory = rentalHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rentalHistory);
    }

    /**
     * {@code DELETE  /rental-histories/:id} : delete the "id" rentalHistory.
     *
     * @param id the id of the rentalHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rental-histories/{id}")
    public ResponseEntity<Void> deleteRentalHistory(@PathVariable Long id) {
        log.debug("REST request to delete RentalHistory : {}", id);
        rentalHistoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
