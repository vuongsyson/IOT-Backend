package com.sonpj.web.rest;

import com.sonpj.domain.Bss;
import com.sonpj.repository.BssRepository;
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
 * REST controller for managing {@link com.sonpj.domain.Bss}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BssResource {

    private final Logger log = LoggerFactory.getLogger(BssResource.class);

    private static final String ENTITY_NAME = "bss";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BssRepository bssRepository;

    public BssResource(BssRepository bssRepository) {
        this.bssRepository = bssRepository;
    }

    /**
     * {@code POST  /bsses} : Create a new bss.
     *
     * @param bss the bss to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bss, or with status {@code 400 (Bad Request)} if the bss has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bsses")
    public Mono<ResponseEntity<Bss>> createBss(@RequestBody Bss bss) throws URISyntaxException {
        log.debug("REST request to save Bss : {}", bss);
        if (bss.getId() != null) {
            throw new BadRequestAlertException("A new bss cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return bssRepository
            .save(bss)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/bsses/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /bsses/:id} : Updates an existing bss.
     *
     * @param id the id of the bss to save.
     * @param bss the bss to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bss,
     * or with status {@code 400 (Bad Request)} if the bss is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bss couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bsses/{id}")
    public Mono<ResponseEntity<Bss>> updateBss(@PathVariable(value = "id", required = false) final Long id, @RequestBody Bss bss)
        throws URISyntaxException {
        log.debug("REST request to update Bss : {}, {}", id, bss);
        if (bss.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bss.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bssRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return bssRepository
                    .save(bss)
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
     * {@code PATCH  /bsses/:id} : Partial updates given fields of an existing bss, field will ignore if it is null
     *
     * @param id the id of the bss to save.
     * @param bss the bss to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bss,
     * or with status {@code 400 (Bad Request)} if the bss is not valid,
     * or with status {@code 404 (Not Found)} if the bss is not found,
     * or with status {@code 500 (Internal Server Error)} if the bss couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bsses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Bss>> partialUpdateBss(@PathVariable(value = "id", required = false) final Long id, @RequestBody Bss bss)
        throws URISyntaxException {
        log.debug("REST request to partial update Bss partially : {}, {}", id, bss);
        if (bss.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bss.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bssRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Bss> result = bssRepository
                    .findById(bss.getId())
                    .map(existingBss -> {
                        if (bss.getName() != null) {
                            existingBss.setName(bss.getName());
                        }
                        if (bss.getAddress() != null) {
                            existingBss.setAddress(bss.getAddress());
                        }
                        if (bss.getSerialNumber() != null) {
                            existingBss.setSerialNumber(bss.getSerialNumber());
                        }
                        if (bss.getHwVersion() != null) {
                            existingBss.setHwVersion(bss.getHwVersion());
                        }
                        if (bss.getSwVersion() != null) {
                            existingBss.setSwVersion(bss.getSwVersion());
                        }
                        if (bss.getManufactureDate() != null) {
                            existingBss.setManufactureDate(bss.getManufactureDate());
                        }
                        if (bss.getLon() != null) {
                            existingBss.setLon(bss.getLon());
                        }
                        if (bss.getLat() != null) {
                            existingBss.setLat(bss.getLat());
                        }
                        if (bss.getTypeCode() != null) {
                            existingBss.setTypeCode(bss.getTypeCode());
                        }
                        if (bss.getCabNum() != null) {
                            existingBss.setCabNum(bss.getCabNum());
                        }
                        if (bss.getCabEmptyNum() != null) {
                            existingBss.setCabEmptyNum(bss.getCabEmptyNum());
                        }
                        if (bss.getBpReadyNum() != null) {
                            existingBss.setBpReadyNum(bss.getBpReadyNum());
                        }
                        if (bss.getSwapBpNo() != null) {
                            existingBss.setSwapBpNo(bss.getSwapBpNo());
                        }

                        return existingBss;
                    })
                    .flatMap(bssRepository::save);

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
     * {@code GET  /bsses} : get all the bsses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bsses in body.
     */
    @GetMapping("/bsses")
    public Mono<List<Bss>> getAllBsses() {
        log.debug("REST request to get all Bsses");
        return bssRepository.findAll().collectList();
    }

    /**
     * {@code GET  /bsses} : get all the bsses as a stream.
     * @return the {@link Flux} of bsses.
     */
    @GetMapping(value = "/bsses", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Bss> getAllBssesAsStream() {
        log.debug("REST request to get all Bsses as a stream");
        return bssRepository.findAll();
    }

    /**
     * {@code GET  /bsses/:id} : get the "id" bss.
     *
     * @param id the id of the bss to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bss, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bsses/{id}")
    public Mono<ResponseEntity<Bss>> getBss(@PathVariable Long id) {
        log.debug("REST request to get Bss : {}", id);
        Mono<Bss> bss = bssRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bss);
    }

    /**
     * {@code DELETE  /bsses/:id} : delete the "id" bss.
     *
     * @param id the id of the bss to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bsses/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBss(@PathVariable Long id) {
        log.debug("REST request to delete Bss : {}", id);
        return bssRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
