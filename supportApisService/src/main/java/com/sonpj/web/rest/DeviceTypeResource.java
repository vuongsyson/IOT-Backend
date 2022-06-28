package com.sonpj.web.rest;

import com.sonpj.domain.DeviceType;
import com.sonpj.repository.DeviceTypeRepository;
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
 * REST controller for managing {@link com.sonpj.domain.DeviceType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DeviceTypeResource {

    private final Logger log = LoggerFactory.getLogger(DeviceTypeResource.class);

    private static final String ENTITY_NAME = "supportApisServiceDeviceType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceTypeRepository deviceTypeRepository;

    public DeviceTypeResource(DeviceTypeRepository deviceTypeRepository) {
        this.deviceTypeRepository = deviceTypeRepository;
    }

    /**
     * {@code POST  /device-types} : Create a new deviceType.
     *
     * @param deviceType the deviceType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceType, or with status {@code 400 (Bad Request)} if the deviceType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/device-types")
    public ResponseEntity<DeviceType> createDeviceType(@Valid @RequestBody DeviceType deviceType) throws URISyntaxException {
        log.debug("REST request to save DeviceType : {}", deviceType);
        if (deviceType.getId() != null) {
            throw new BadRequestAlertException("A new deviceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeviceType result = deviceTypeRepository.save(deviceType);
        return ResponseEntity
            .created(new URI("/api/device-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /device-types/:id} : Updates an existing deviceType.
     *
     * @param id the id of the deviceType to save.
     * @param deviceType the deviceType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceType,
     * or with status {@code 400 (Bad Request)} if the deviceType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/device-types/{id}")
    public ResponseEntity<DeviceType> updateDeviceType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeviceType deviceType
    ) throws URISyntaxException {
        log.debug("REST request to update DeviceType : {}, {}", id, deviceType);
        if (deviceType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeviceType result = deviceTypeRepository.save(deviceType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /device-types/:id} : Partial updates given fields of an existing deviceType, field will ignore if it is null
     *
     * @param id the id of the deviceType to save.
     * @param deviceType the deviceType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceType,
     * or with status {@code 400 (Bad Request)} if the deviceType is not valid,
     * or with status {@code 404 (Not Found)} if the deviceType is not found,
     * or with status {@code 500 (Internal Server Error)} if the deviceType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/device-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeviceType> partialUpdateDeviceType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeviceType deviceType
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeviceType partially : {}, {}", id, deviceType);
        if (deviceType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeviceType> result = deviceTypeRepository
            .findById(deviceType.getId())
            .map(existingDeviceType -> {
                if (deviceType.getName() != null) {
                    existingDeviceType.setName(deviceType.getName());
                }

                return existingDeviceType;
            })
            .map(deviceTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceType.getId().toString())
        );
    }

    /**
     * {@code GET  /device-types} : get all the deviceTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deviceTypes in body.
     */
    @GetMapping("/device-types")
    public List<DeviceType> getAllDeviceTypes() {
        log.debug("REST request to get all DeviceTypes");
        return deviceTypeRepository.findAll();
    }

    /**
     * {@code GET  /device-types/:id} : get the "id" deviceType.
     *
     * @param id the id of the deviceType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/device-types/{id}")
    public ResponseEntity<DeviceType> getDeviceType(@PathVariable Long id) {
        log.debug("REST request to get DeviceType : {}", id);
        Optional<DeviceType> deviceType = deviceTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(deviceType);
    }

    /**
     * {@code DELETE  /device-types/:id} : delete the "id" deviceType.
     *
     * @param id the id of the deviceType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/device-types/{id}")
    public ResponseEntity<Void> deleteDeviceType(@PathVariable Long id) {
        log.debug("REST request to delete DeviceType : {}", id);
        deviceTypeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
