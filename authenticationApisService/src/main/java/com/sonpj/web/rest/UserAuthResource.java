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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sonpj.domain.UserAuth}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserAuthResource {

    private final Logger log = LoggerFactory.getLogger(UserAuthResource.class);

    private static final String ENTITY_NAME = "authenticationApisServiceUserAuth";

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
    public ResponseEntity<UserAuth> createUserAuth(@RequestBody UserAuth userAuth) throws URISyntaxException {
        log.debug("REST request to save UserAuth : {}", userAuth);
        if (userAuth.getId() != null) {
            throw new BadRequestAlertException("A new userAuth cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAuth result = userAuthRepository.save(userAuth);
        return ResponseEntity
            .created(new URI("/api/user-auths/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<UserAuth> updateUserAuth(
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

        if (!userAuthRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserAuth result = userAuthRepository.save(userAuth);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAuth.getId().toString()))
            .body(result);
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
    public ResponseEntity<UserAuth> partialUpdateUserAuth(
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

        if (!userAuthRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAuth> result = userAuthRepository
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
            .map(userAuthRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAuth.getId().toString())
        );
    }

    /**
     * {@code GET  /user-auths} : get all the userAuths.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAuths in body.
     */
    @GetMapping("/user-auths")
    public List<UserAuth> getAllUserAuths() {
        log.debug("REST request to get all UserAuths");
        return userAuthRepository.findAll();
    }

    /**
     * {@code GET  /user-auths/:id} : get the "id" userAuth.
     *
     * @param id the id of the userAuth to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAuth, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-auths/{id}")
    public ResponseEntity<UserAuth> getUserAuth(@PathVariable Long id) {
        log.debug("REST request to get UserAuth : {}", id);
        Optional<UserAuth> userAuth = userAuthRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userAuth);
    }

    /**
     * {@code DELETE  /user-auths/:id} : delete the "id" userAuth.
     *
     * @param id the id of the userAuth to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-auths/{id}")
    public ResponseEntity<Void> deleteUserAuth(@PathVariable Long id) {
        log.debug("REST request to delete UserAuth : {}", id);
        userAuthRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
