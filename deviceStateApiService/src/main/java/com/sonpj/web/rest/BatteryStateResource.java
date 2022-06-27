package com.sonpj.web.rest;

import com.sonpj.domain.BatteryState;
import com.sonpj.repository.BatteryStateRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sonpj.domain.BatteryState}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BatteryStateResource {

    private final Logger log = LoggerFactory.getLogger(BatteryStateResource.class);

    private static final String ENTITY_NAME = "deviceStateApiServiceBatteryState";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BatteryStateRepository batteryStateRepository;

    public BatteryStateResource(BatteryStateRepository batteryStateRepository) {
        this.batteryStateRepository = batteryStateRepository;
    }

    /**
     * {@code POST  /battery-states} : Create a new batteryState.
     *
     * @param batteryState the batteryState to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new batteryState, or with status {@code 400 (Bad Request)} if the batteryState has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/battery-states")
    public ResponseEntity<BatteryState> createBatteryState(@Valid @RequestBody BatteryState batteryState) throws URISyntaxException {
        log.debug("REST request to save BatteryState : {}", batteryState);
        if (batteryState.getId() != null) {
            throw new BadRequestAlertException("A new batteryState cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BatteryState result = batteryStateRepository.save(batteryState);
        return ResponseEntity
            .created(new URI("/api/battery-states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /battery-states/:id} : Updates an existing batteryState.
     *
     * @param id the id of the batteryState to save.
     * @param batteryState the batteryState to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated batteryState,
     * or with status {@code 400 (Bad Request)} if the batteryState is not valid,
     * or with status {@code 500 (Internal Server Error)} if the batteryState couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/battery-states/{id}")
    public ResponseEntity<BatteryState> updateBatteryState(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BatteryState batteryState
    ) throws URISyntaxException {
        log.debug("REST request to update BatteryState : {}, {}", id, batteryState);
        if (batteryState.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, batteryState.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!batteryStateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BatteryState result = batteryStateRepository.save(batteryState);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, batteryState.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /battery-states/:id} : Partial updates given fields of an existing batteryState, field will ignore if it is null
     *
     * @param id the id of the batteryState to save.
     * @param batteryState the batteryState to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated batteryState,
     * or with status {@code 400 (Bad Request)} if the batteryState is not valid,
     * or with status {@code 404 (Not Found)} if the batteryState is not found,
     * or with status {@code 500 (Internal Server Error)} if the batteryState couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/battery-states/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BatteryState> partialUpdateBatteryState(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BatteryState batteryState
    ) throws URISyntaxException {
        log.debug("REST request to partial update BatteryState partially : {}, {}", id, batteryState);
        if (batteryState.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, batteryState.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!batteryStateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BatteryState> result = batteryStateRepository
            .findById(batteryState.getId())
            .map(existingBatteryState -> {
                if (batteryState.getSerialNumber() != null) {
                    existingBatteryState.setSerialNumber(batteryState.getSerialNumber());
                }
                if (batteryState.getVol() != null) {
                    existingBatteryState.setVol(batteryState.getVol());
                }
                if (batteryState.getCur() != null) {
                    existingBatteryState.setCur(batteryState.getCur());
                }
                if (batteryState.getSoc() != null) {
                    existingBatteryState.setSoc(batteryState.getSoc());
                }
                if (batteryState.getSoh() != null) {
                    existingBatteryState.setSoh(batteryState.getSoh());
                }
                if (batteryState.getState() != null) {
                    existingBatteryState.setState(batteryState.getState());
                }
                if (batteryState.getStatus() != null) {
                    existingBatteryState.setStatus(batteryState.getStatus());
                }

                return existingBatteryState;
            })
            .map(batteryStateRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, batteryState.getId().toString())
        );
    }

    /**
     * {@code GET  /battery-states} : get all the batteryStates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of batteryStates in body.
     */
    @GetMapping("/battery-states")
    public List<BatteryState> getAllBatteryStates() {
        log.debug("REST request to get all BatteryStates");
        return batteryStateRepository.findAll();
    }

    /**
     * {@code GET  /battery-states/:id} : get the "id" batteryState.
     *
     * @param id the id of the batteryState to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the batteryState, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/battery-states/{id}")
    public ResponseEntity<BatteryState> getBatteryState(@PathVariable Long id) {
        log.debug("REST request to get BatteryState : {}", id);
        Optional<BatteryState> batteryState = batteryStateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(batteryState);
    }

    /**
     * {@code DELETE  /battery-states/:id} : delete the "id" batteryState.
     *
     * @param id the id of the batteryState to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/battery-states/{id}")
    public ResponseEntity<Void> deleteBatteryState(@PathVariable Long id) {
        log.debug("REST request to delete BatteryState : {}", id);
        batteryStateRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
