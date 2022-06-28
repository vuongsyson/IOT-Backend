package com.sonpj.web.rest;

import com.sonpj.domain.UserAuth;
import com.sonpj.repository.UserAuthRepository;
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
 * REST controller for managing {@link com.sonpj.domain.UserAuth}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserAuthResource {

    private final Logger log = LoggerFactory.getLogger(UserAuthResource.class);

    private static final String ENTITY_NAME = "userAuth";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAuthRepository userAuthRepository;

    public UserAuthResource(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    /**
     * {@code POST  /user-auths} : Create a new userAuth.
     *
     * @param userAuth the userAuth to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAuth, or with status {@code 400 (Bad Request)} if the userAuth has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-auths")
    public Mono<ResponseEntity<UserAuth>> createUserAuth(@RequestBody UserAuth userAuth) throws URISyntaxException {
        log.debug("REST request to save UserAuth : {}", userAuth);
        if (userAuth.getId() != null) {
            throw new BadRequestAlertException("A new userAuth cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userAuthRepository
            .save(userAuth)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/user-auths/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-auths/:id} : Updates an existing userAuth.
     *
     * @param id the id of the userAuth to save.
     * @param userAuth the userAuth to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAuth,
     * or with status {@code 400 (Bad Request)} if the userAuth is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAuth couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-auths/{id}")
    public Mono<ResponseEntity<UserAuth>> updateUserAuth(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAuth userAuth
    ) throws URISyntaxException {
        log.debug("REST request to update UserAuth : {}, {}", id, userAuth);
        if (userAuth.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAuth.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userAuthRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userAuthRepository
                    .save(userAuth)
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
     * {@code PATCH  /user-auths/:id} : Partial updates given fields of an existing userAuth, field will ignore if it is null
     *
     * @param id the id of the userAuth to save.
     * @param userAuth the userAuth to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAuth,
     * or with status {@code 400 (Bad Request)} if the userAuth is not valid,
     * or with status {@code 404 (Not Found)} if the userAuth is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAuth couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-auths/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserAuth>> partialUpdateUserAuth(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAuth userAuth
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAuth partially : {}, {}", id, userAuth);
        if (userAuth.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAuth.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userAuthRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserAuth> result = userAuthRepository
                    .findById(userAuth.getId())
                    .map(existingUserAuth -> {
                        if (userAuth.getEmail() != null) {
                            existingUserAuth.setEmail(userAuth.getEmail());
                        }
                        if (userAuth.getPhone() != null) {
                            existingUserAuth.setPhone(userAuth.getPhone());
                        }
                        if (userAuth.getUsername() != null) {
                            existingUserAuth.setUsername(userAuth.getUsername());
                        }
                        if (userAuth.getAuthorities() != null) {
                            existingUserAuth.setAuthorities(userAuth.getAuthorities());
                        }
                        if (userAuth.getPassword() != null) {
                            existingUserAuth.setPassword(userAuth.getPassword());
                        }

                        return existingUserAuth;
                    })
                    .flatMap(userAuthRepository::save);

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
     * {@code GET  /user-auths} : get all the userAuths.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAuths in body.
     */
    @GetMapping("/user-auths")
    public Mono<List<UserAuth>> getAllUserAuths() {
        log.debug("REST request to get all UserAuths");
        return userAuthRepository.findAll().collectList();
    }

    /**
     * {@code GET  /user-auths} : get all the userAuths as a stream.
     * @return the {@link Flux} of userAuths.
     */
    @GetMapping(value = "/user-auths", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<UserAuth> getAllUserAuthsAsStream() {
        log.debug("REST request to get all UserAuths as a stream");
        return userAuthRepository.findAll();
    }

    /**
     * {@code GET  /user-auths/:id} : get the "id" userAuth.
     *
     * @param id the id of the userAuth to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAuth, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-auths/{id}")
    public Mono<ResponseEntity<UserAuth>> getUserAuth(@PathVariable Long id) {
        log.debug("REST request to get UserAuth : {}", id);
        Mono<UserAuth> userAuth = userAuthRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userAuth);
    }

    /**
     * {@code DELETE  /user-auths/:id} : delete the "id" userAuth.
     *
     * @param id the id of the userAuth to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-auths/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteUserAuth(@PathVariable Long id) {
        log.debug("REST request to delete UserAuth : {}", id);
        return userAuthRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
