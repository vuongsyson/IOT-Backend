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
 * REST controller for managing {@link com.sonpj.domain.BatteryState}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BatteryStateResource {

    private final Logger log = LoggerFactory.getLogger(BatteryStateResource.class);

    private static final String ENTITY_NAME = "batteryState";

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
    public Mono<ResponseEntity<BatteryState>> createBatteryState(@Valid @RequestBody BatteryState batteryState) throws URISyntaxException {
        log.debug("REST request to save BatteryState : {}", batteryState);
        if (batteryState.getId() != null) {
            throw new BadRequestAlertException("A new batteryState cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return batteryStateRepository
            .save(batteryState)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/battery-states/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
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
    public Mono<ResponseEntity<BatteryState>> updateBatteryState(
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

        return batteryStateRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return batteryStateRepository
                    .save(batteryState)
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
    public Mono<ResponseEntity<BatteryState>> partialUpdateBatteryState(
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

        return batteryStateRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<BatteryState> result = batteryStateRepository
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
                    .flatMap(batteryStateRepository::save);

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
     * {@code GET  /battery-states} : get all the batteryStates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of batteryStates in body.
     */
    @GetMapping("/battery-states")
    public Mono<List<BatteryState>> getAllBatteryStates() {
        log.debug("REST request to get all BatteryStates");
        return batteryStateRepository.findAll().collectList();
    }

    /**
     * {@code GET  /battery-states} : get all the batteryStates as a stream.
     * @return the {@link Flux} of batteryStates.
     */
    @GetMapping(value = "/battery-states", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<BatteryState> getAllBatteryStatesAsStream() {
        log.debug("REST request to get all BatteryStates as a stream");
        return batteryStateRepository.findAll();
    }

    /**
     * {@code GET  /battery-states/:id} : get the "id" batteryState.
     *
     * @param id the id of the batteryState to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the batteryState, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/battery-states/{id}")
    public Mono<ResponseEntity<BatteryState>> getBatteryState(@PathVariable Long id) {
        log.debug("REST request to get BatteryState : {}", id);
        Mono<BatteryState> batteryState = batteryStateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(batteryState);
    }

    /**
     * {@code DELETE  /battery-states/:id} : delete the "id" batteryState.
     *
     * @param id the id of the batteryState to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/battery-states/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBatteryState(@PathVariable Long id) {
        log.debug("REST request to delete BatteryState : {}", id);
        return batteryStateRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
