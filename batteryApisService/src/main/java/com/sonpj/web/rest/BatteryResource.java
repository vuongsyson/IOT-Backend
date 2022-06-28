package com.sonpj.web.rest;

import com.sonpj.domain.Battery;
import com.sonpj.repository.BatteryRepository;
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
 * REST controller for managing {@link com.sonpj.domain.Battery}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BatteryResource {

    private final Logger log = LoggerFactory.getLogger(BatteryResource.class);

    private static final String ENTITY_NAME = "batteryApisServiceBattery";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BatteryRepository batteryRepository;

    public BatteryResource(BatteryRepository batteryRepository) {
        this.batteryRepository = batteryRepository;
    }

    /**
     * {@code POST  /batteries} : Create a new battery.
     *
     * @param battery the battery to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new battery, or with status {@code 400 (Bad Request)} if the battery has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/batteries")
    public ResponseEntity<Battery> createBattery(@Valid @RequestBody Battery battery) throws URISyntaxException {
        log.debug("REST request to save Battery : {}", battery);
        if (battery.getId() != null) {
            throw new BadRequestAlertException("A new battery cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Battery result = batteryRepository.save(battery);
        return ResponseEntity
            .created(new URI("/api/batteries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /batteries/:id} : Updates an existing battery.
     *
     * @param id the id of the battery to save.
     * @param battery the battery to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated battery,
     * or with status {@code 400 (Bad Request)} if the battery is not valid,
     * or with status {@code 500 (Internal Server Error)} if the battery couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/batteries/{id}")
    public ResponseEntity<Battery> updateBattery(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Battery battery
    ) throws URISyntaxException {
        log.debug("REST request to update Battery : {}, {}", id, battery);
        if (battery.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, battery.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!batteryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Battery result = batteryRepository.save(battery);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, battery.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /batteries/:id} : Partial updates given fields of an existing battery, field will ignore if it is null
     *
     * @param id the id of the battery to save.
     * @param battery the battery to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated battery,
     * or with status {@code 400 (Bad Request)} if the battery is not valid,
     * or with status {@code 404 (Not Found)} if the battery is not found,
     * or with status {@code 500 (Internal Server Error)} if the battery couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/batteries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Battery> partialUpdateBattery(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Battery battery
    ) throws URISyntaxException {
        log.debug("REST request to partial update Battery partially : {}, {}", id, battery);
        if (battery.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, battery.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!batteryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Battery> result = batteryRepository
            .findById(battery.getId())
            .map(existingBattery -> {
                if (battery.getSerialNo() != null) {
                    existingBattery.setSerialNo(battery.getSerialNo());
                }
                if (battery.getHwVersion() != null) {
                    existingBattery.setHwVersion(battery.getHwVersion());
                }
                if (battery.getSwVersion() != null) {
                    existingBattery.setSwVersion(battery.getSwVersion());
                }
                if (battery.getManufactureDate() != null) {
                    existingBattery.setManufactureDate(battery.getManufactureDate());
                }
                if (battery.getCapacity() != null) {
                    existingBattery.setCapacity(battery.getCapacity());
                }
                if (battery.getMaxCharge() != null) {
                    existingBattery.setMaxCharge(battery.getMaxCharge());
                }
                if (battery.getMaxDiscarge() != null) {
                    existingBattery.setMaxDiscarge(battery.getMaxDiscarge());
                }
                if (battery.getMaxVol() != null) {
                    existingBattery.setMaxVol(battery.getMaxVol());
                }
                if (battery.getMinVol() != null) {
                    existingBattery.setMinVol(battery.getMinVol());
                }
                if (battery.getUsed() != null) {
                    existingBattery.setUsed(battery.getUsed());
                }
                if (battery.getSoc() != null) {
                    existingBattery.setSoc(battery.getSoc());
                }
                if (battery.getSoh() != null) {
                    existingBattery.setSoh(battery.getSoh());
                }
                if (battery.getTemp() != null) {
                    existingBattery.setTemp(battery.getTemp());
                }
                if (battery.getOwnerId() != null) {
                    existingBattery.setOwnerId(battery.getOwnerId());
                }
                if (battery.getRenterId() != null) {
                    existingBattery.setRenterId(battery.getRenterId());
                }
                if (battery.getCycleCount() != null) {
                    existingBattery.setCycleCount(battery.getCycleCount());
                }

                return existingBattery;
            })
            .map(batteryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, battery.getId().toString())
        );
    }

    /**
     * {@code GET  /batteries} : get all the batteries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of batteries in body.
     */
    @GetMapping("/batteries")
    public List<Battery> getAllBatteries() {
        log.debug("REST request to get all Batteries");
        return batteryRepository.findAll();
    }

    /**
     * {@code GET  /batteries/:id} : get the "id" battery.
     *
     * @param id the id of the battery to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the battery, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/batteries/{id}")
    public ResponseEntity<Battery> getBattery(@PathVariable Long id) {
        log.debug("REST request to get Battery : {}", id);
        Optional<Battery> battery = batteryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(battery);
    }

    /**
     * {@code DELETE  /batteries/:id} : delete the "id" battery.
     *
     * @param id the id of the battery to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/batteries/{id}")
    public ResponseEntity<Void> deleteBattery(@PathVariable Long id) {
        log.debug("REST request to delete Battery : {}", id);
        batteryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
