package com.sonpj.web.rest;

import com.sonpj.domain.VehicleState;
import com.sonpj.repository.VehicleStateRepository;
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
 * REST controller for managing {@link com.sonpj.domain.VehicleState}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VehicleStateResource {

    private final Logger log = LoggerFactory.getLogger(VehicleStateResource.class);

    private static final String ENTITY_NAME = "vehicleState";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleStateRepository vehicleStateRepository;

    public VehicleStateResource(VehicleStateRepository vehicleStateRepository) {
        this.vehicleStateRepository = vehicleStateRepository;
    }

    /**
     * {@code POST  /vehicle-states} : Create a new vehicleState.
     *
     * @param vehicleState the vehicleState to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleState, or with status {@code 400 (Bad Request)} if the vehicleState has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vehicle-states")
    public Mono<ResponseEntity<VehicleState>> createVehicleState(@RequestBody VehicleState vehicleState) throws URISyntaxException {
        log.debug("REST request to save VehicleState : {}", vehicleState);
        if (vehicleState.getId() != null) {
            throw new BadRequestAlertException("A new vehicleState cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return vehicleStateRepository
            .save(vehicleState)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/vehicle-states/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /vehicle-states/:id} : Updates an existing vehicleState.
     *
     * @param id the id of the vehicleState to save.
     * @param vehicleState the vehicleState to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleState,
     * or with status {@code 400 (Bad Request)} if the vehicleState is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleState couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vehicle-states/{id}")
    public Mono<ResponseEntity<VehicleState>> updateVehicleState(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VehicleState vehicleState
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleState : {}, {}", id, vehicleState);
        if (vehicleState.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleState.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return vehicleStateRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return vehicleStateRepository
                    .save(vehicleState)
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
     * {@code PATCH  /vehicle-states/:id} : Partial updates given fields of an existing vehicleState, field will ignore if it is null
     *
     * @param id the id of the vehicleState to save.
     * @param vehicleState the vehicleState to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleState,
     * or with status {@code 400 (Bad Request)} if the vehicleState is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleState is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleState couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vehicle-states/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<VehicleState>> partialUpdateVehicleState(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VehicleState vehicleState
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleState partially : {}, {}", id, vehicleState);
        if (vehicleState.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleState.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return vehicleStateRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<VehicleState> result = vehicleStateRepository
                    .findById(vehicleState.getId())
                    .map(existingVehicleState -> {
                        if (vehicleState.getSpeed() != null) {
                            existingVehicleState.setSpeed(vehicleState.getSpeed());
                        }
                        if (vehicleState.getLat() != null) {
                            existingVehicleState.setLat(vehicleState.getLat());
                        }
                        if (vehicleState.getLon() != null) {
                            existingVehicleState.setLon(vehicleState.getLon());
                        }
                        if (vehicleState.getError() != null) {
                            existingVehicleState.setError(vehicleState.getError());
                        }
                        if (vehicleState.getSerialNumber() != null) {
                            existingVehicleState.setSerialNumber(vehicleState.getSerialNumber());
                        }
                        if (vehicleState.getStatus() != null) {
                            existingVehicleState.setStatus(vehicleState.getStatus());
                        }
                        if (vehicleState.getOdo() != null) {
                            existingVehicleState.setOdo(vehicleState.getOdo());
                        }
                        if (vehicleState.getPower() != null) {
                            existingVehicleState.setPower(vehicleState.getPower());
                        }
                        if (vehicleState.getThrottle() != null) {
                            existingVehicleState.setThrottle(vehicleState.getThrottle());
                        }
                        if (vehicleState.getTime() != null) {
                            existingVehicleState.setTime(vehicleState.getTime());
                        }

                        return existingVehicleState;
                    })
                    .flatMap(vehicleStateRepository::save);

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
     * {@code GET  /vehicle-states} : get all the vehicleStates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleStates in body.
     */
    @GetMapping("/vehicle-states")
    public Mono<List<VehicleState>> getAllVehicleStates() {
        log.debug("REST request to get all VehicleStates");
        return vehicleStateRepository.findAll().collectList();
    }

    /**
     * {@code GET  /vehicle-states} : get all the vehicleStates as a stream.
     * @return the {@link Flux} of vehicleStates.
     */
    @GetMapping(value = "/vehicle-states", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<VehicleState> getAllVehicleStatesAsStream() {
        log.debug("REST request to get all VehicleStates as a stream");
        return vehicleStateRepository.findAll();
    }

    /**
     * {@code GET  /vehicle-states/:id} : get the "id" vehicleState.
     *
     * @param id the id of the vehicleState to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleState, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vehicle-states/{id}")
    public Mono<ResponseEntity<VehicleState>> getVehicleState(@PathVariable Long id) {
        log.debug("REST request to get VehicleState : {}", id);
        Mono<VehicleState> vehicleState = vehicleStateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vehicleState);
    }

    /**
     * {@code DELETE  /vehicle-states/:id} : delete the "id" vehicleState.
     *
     * @param id the id of the vehicleState to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vehicle-states/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteVehicleState(@PathVariable Long id) {
        log.debug("REST request to delete VehicleState : {}", id);
        return vehicleStateRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
