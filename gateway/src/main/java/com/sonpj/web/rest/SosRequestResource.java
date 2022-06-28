package com.sonpj.web.rest;

import com.sonpj.domain.SosRequest;
import com.sonpj.repository.SosRequestRepository;
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
 * REST controller for managing {@link com.sonpj.domain.SosRequest}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SosRequestResource {

    private final Logger log = LoggerFactory.getLogger(SosRequestResource.class);

    private static final String ENTITY_NAME = "sosRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SosRequestRepository sosRequestRepository;

    public SosRequestResource(SosRequestRepository sosRequestRepository) {
        this.sosRequestRepository = sosRequestRepository;
    }

    /**
     * {@code POST  /sos-requests} : Create a new sosRequest.
     *
     * @param sosRequest the sosRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sosRequest, or with status {@code 400 (Bad Request)} if the sosRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sos-requests")
    public Mono<ResponseEntity<SosRequest>> createSosRequest(@Valid @RequestBody SosRequest sosRequest) throws URISyntaxException {
        log.debug("REST request to save SosRequest : {}", sosRequest);
        if (sosRequest.getId() != null) {
            throw new BadRequestAlertException("A new sosRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return sosRequestRepository
            .save(sosRequest)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/sos-requests/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /sos-requests/:id} : Updates an existing sosRequest.
     *
     * @param id the id of the sosRequest to save.
     * @param sosRequest the sosRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sosRequest,
     * or with status {@code 400 (Bad Request)} if the sosRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sosRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sos-requests/{id}")
    public Mono<ResponseEntity<SosRequest>> updateSosRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SosRequest sosRequest
    ) throws URISyntaxException {
        log.debug("REST request to update SosRequest : {}, {}", id, sosRequest);
        if (sosRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sosRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sosRequestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return sosRequestRepository
                    .save(sosRequest)
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
     * {@code PATCH  /sos-requests/:id} : Partial updates given fields of an existing sosRequest, field will ignore if it is null
     *
     * @param id the id of the sosRequest to save.
     * @param sosRequest the sosRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sosRequest,
     * or with status {@code 400 (Bad Request)} if the sosRequest is not valid,
     * or with status {@code 404 (Not Found)} if the sosRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the sosRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sos-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SosRequest>> partialUpdateSosRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SosRequest sosRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update SosRequest partially : {}, {}", id, sosRequest);
        if (sosRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sosRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sosRequestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SosRequest> result = sosRequestRepository
                    .findById(sosRequest.getId())
                    .map(existingSosRequest -> {
                        if (sosRequest.getUserId() != null) {
                            existingSosRequest.setUserId(sosRequest.getUserId());
                        }
                        if (sosRequest.getPhone() != null) {
                            existingSosRequest.setPhone(sosRequest.getPhone());
                        }
                        if (sosRequest.getDeviceSerialNumber() != null) {
                            existingSosRequest.setDeviceSerialNumber(sosRequest.getDeviceSerialNumber());
                        }
                        if (sosRequest.getDescription() != null) {
                            existingSosRequest.setDescription(sosRequest.getDescription());
                        }
                        if (sosRequest.getImage() != null) {
                            existingSosRequest.setImage(sosRequest.getImage());
                        }
                        if (sosRequest.getState() != null) {
                            existingSosRequest.setState(sosRequest.getState());
                        }
                        if (sosRequest.getRating() != null) {
                            existingSosRequest.setRating(sosRequest.getRating());
                        }
                        if (sosRequest.getDone() != null) {
                            existingSosRequest.setDone(sosRequest.getDone());
                        }
                        if (sosRequest.getDoneTime() != null) {
                            existingSosRequest.setDoneTime(sosRequest.getDoneTime());
                        }

                        return existingSosRequest;
                    })
                    .flatMap(sosRequestRepository::save);

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
     * {@code GET  /sos-requests} : get all the sosRequests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sosRequests in body.
     */
    @GetMapping("/sos-requests")
    public Mono<List<SosRequest>> getAllSosRequests() {
        log.debug("REST request to get all SosRequests");
        return sosRequestRepository.findAll().collectList();
    }

    /**
     * {@code GET  /sos-requests} : get all the sosRequests as a stream.
     * @return the {@link Flux} of sosRequests.
     */
    @GetMapping(value = "/sos-requests", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SosRequest> getAllSosRequestsAsStream() {
        log.debug("REST request to get all SosRequests as a stream");
        return sosRequestRepository.findAll();
    }

    /**
     * {@code GET  /sos-requests/:id} : get the "id" sosRequest.
     *
     * @param id the id of the sosRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sosRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sos-requests/{id}")
    public Mono<ResponseEntity<SosRequest>> getSosRequest(@PathVariable Long id) {
        log.debug("REST request to get SosRequest : {}", id);
        Mono<SosRequest> sosRequest = sosRequestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sosRequest);
    }

    /**
     * {@code DELETE  /sos-requests/:id} : delete the "id" sosRequest.
     *
     * @param id the id of the sosRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sos-requests/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSosRequest(@PathVariable Long id) {
        log.debug("REST request to delete SosRequest : {}", id);
        return sosRequestRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
