package com.sonpj.web.rest;

import com.sonpj.domain.RefreshToken;
import com.sonpj.repository.RefreshTokenRepository;
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
 * REST controller for managing {@link com.sonpj.domain.RefreshToken}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RefreshTokenResource {

    private final Logger log = LoggerFactory.getLogger(RefreshTokenResource.class);

    private static final String ENTITY_NAME = "refreshToken";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenResource(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * {@code POST  /refresh-tokens} : Create a new refreshToken.
     *
     * @param refreshToken the refreshToken to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new refreshToken, or with status {@code 400 (Bad Request)} if the refreshToken has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/refresh-tokens")
    public Mono<ResponseEntity<RefreshToken>> createRefreshToken(@RequestBody RefreshToken refreshToken) throws URISyntaxException {
        log.debug("REST request to save RefreshToken : {}", refreshToken);
        if (refreshToken.getId() != null) {
            throw new BadRequestAlertException("A new refreshToken cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return refreshTokenRepository
            .save(refreshToken)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/refresh-tokens/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /refresh-tokens/:id} : Updates an existing refreshToken.
     *
     * @param id the id of the refreshToken to save.
     * @param refreshToken the refreshToken to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated refreshToken,
     * or with status {@code 400 (Bad Request)} if the refreshToken is not valid,
     * or with status {@code 500 (Internal Server Error)} if the refreshToken couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/refresh-tokens/{id}")
    public Mono<ResponseEntity<RefreshToken>> updateRefreshToken(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RefreshToken refreshToken
    ) throws URISyntaxException {
        log.debug("REST request to update RefreshToken : {}, {}", id, refreshToken);
        if (refreshToken.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, refreshToken.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return refreshTokenRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return refreshTokenRepository
                    .save(refreshToken)
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
     * {@code PATCH  /refresh-tokens/:id} : Partial updates given fields of an existing refreshToken, field will ignore if it is null
     *
     * @param id the id of the refreshToken to save.
     * @param refreshToken the refreshToken to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated refreshToken,
     * or with status {@code 400 (Bad Request)} if the refreshToken is not valid,
     * or with status {@code 404 (Not Found)} if the refreshToken is not found,
     * or with status {@code 500 (Internal Server Error)} if the refreshToken couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/refresh-tokens/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RefreshToken>> partialUpdateRefreshToken(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RefreshToken refreshToken
    ) throws URISyntaxException {
        log.debug("REST request to partial update RefreshToken partially : {}, {}", id, refreshToken);
        if (refreshToken.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, refreshToken.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return refreshTokenRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RefreshToken> result = refreshTokenRepository
                    .findById(refreshToken.getId())
                    .map(existingRefreshToken -> {
                        if (refreshToken.getUsername() != null) {
                            existingRefreshToken.setUsername(refreshToken.getUsername());
                        }

                        return existingRefreshToken;
                    })
                    .flatMap(refreshTokenRepository::save);

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
     * {@code GET  /refresh-tokens} : get all the refreshTokens.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of refreshTokens in body.
     */
    @GetMapping("/refresh-tokens")
    public Mono<List<RefreshToken>> getAllRefreshTokens() {
        log.debug("REST request to get all RefreshTokens");
        return refreshTokenRepository.findAll().collectList();
    }

    /**
     * {@code GET  /refresh-tokens} : get all the refreshTokens as a stream.
     * @return the {@link Flux} of refreshTokens.
     */
    @GetMapping(value = "/refresh-tokens", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<RefreshToken> getAllRefreshTokensAsStream() {
        log.debug("REST request to get all RefreshTokens as a stream");
        return refreshTokenRepository.findAll();
    }

    /**
     * {@code GET  /refresh-tokens/:id} : get the "id" refreshToken.
     *
     * @param id the id of the refreshToken to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the refreshToken, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/refresh-tokens/{id}")
    public Mono<ResponseEntity<RefreshToken>> getRefreshToken(@PathVariable Long id) {
        log.debug("REST request to get RefreshToken : {}", id);
        Mono<RefreshToken> refreshToken = refreshTokenRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(refreshToken);
    }

    /**
     * {@code DELETE  /refresh-tokens/:id} : delete the "id" refreshToken.
     *
     * @param id the id of the refreshToken to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/refresh-tokens/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteRefreshToken(@PathVariable Long id) {
        log.debug("REST request to delete RefreshToken : {}", id);
        return refreshTokenRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
