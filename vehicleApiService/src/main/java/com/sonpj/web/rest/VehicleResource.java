package com.sonpj.web.rest;

import com.sonpj.domain.Vehicle;
import com.sonpj.repository.VehicleRepository;
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
 * REST controller for managing {@link com.sonpj.domain.Vehicle}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VehicleResource {

    private final Logger log = LoggerFactory.getLogger(VehicleResource.class);

    private static final String ENTITY_NAME = "vehicleApiServiceVehicle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleRepository vehicleRepository;

    public VehicleResource(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * {@code POST  /vehicles} : Create a new vehicle.
     *
     * @param vehicle the vehicle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicle, or with status {@code 400 (Bad Request)} if the vehicle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vehicles")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) throws URISyntaxException {
        log.debug("REST request to save Vehicle : {}", vehicle);
        if (vehicle.getId() != null) {
            throw new BadRequestAlertException("A new vehicle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vehicle result = vehicleRepository.save(vehicle);
        return ResponseEntity
            .created(new URI("/api/vehicles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vehicles/:id} : Updates an existing vehicle.
     *
     * @param id the id of the vehicle to save.
     * @param vehicle the vehicle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicle,
     * or with status {@code 400 (Bad Request)} if the vehicle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vehicles/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable(value = "id", required = false) final Long id, @RequestBody Vehicle vehicle)
        throws URISyntaxException {
        log.debug("REST request to update Vehicle : {}, {}", id, vehicle);
        if (vehicle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vehicle result = vehicleRepository.save(vehicle);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicle.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vehicles/:id} : Partial updates given fields of an existing vehicle, field will ignore if it is null
     *
     * @param id the id of the vehicle to save.
     * @param vehicle the vehicle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicle,
     * or with status {@code 400 (Bad Request)} if the vehicle is not valid,
     * or with status {@code 404 (Not Found)} if the vehicle is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vehicles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vehicle> partialUpdateVehicle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vehicle vehicle
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vehicle partially : {}, {}", id, vehicle);
        if (vehicle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vehicle> result = vehicleRepository
            .findById(vehicle.getId())
            .map(existingVehicle -> {
                if (vehicle.getClearance() != null) {
                    existingVehicle.setClearance(vehicle.getClearance());
                }
                if (vehicle.getMaxPower() != null) {
                    existingVehicle.setMaxPower(vehicle.getMaxPower());
                }
                if (vehicle.getMaxSpeed() != null) {
                    existingVehicle.setMaxSpeed(vehicle.getMaxSpeed());
                }
                if (vehicle.getMaxLoad() != null) {
                    existingVehicle.setMaxLoad(vehicle.getMaxLoad());
                }
                if (vehicle.getWeightTotal() != null) {
                    existingVehicle.setWeightTotal(vehicle.getWeightTotal());
                }
                if (vehicle.getMaxDistance() != null) {
                    existingVehicle.setMaxDistance(vehicle.getMaxDistance());
                }
                if (vehicle.getWheelBase() != null) {
                    existingVehicle.setWheelBase(vehicle.getWheelBase());
                }
                if (vehicle.getHwVersion() != null) {
                    existingVehicle.setHwVersion(vehicle.getHwVersion());
                }
                if (vehicle.getSwVersion() != null) {
                    existingVehicle.setSwVersion(vehicle.getSwVersion());
                }
                if (vehicle.getSerialNumber() != null) {
                    existingVehicle.setSerialNumber(vehicle.getSerialNumber());
                }
                if (vehicle.getManufactureDate() != null) {
                    existingVehicle.setManufactureDate(vehicle.getManufactureDate());
                }
                if (vehicle.getLotNumber() != null) {
                    existingVehicle.setLotNumber(vehicle.getLotNumber());
                }
                if (vehicle.getColor() != null) {
                    existingVehicle.setColor(vehicle.getColor());
                }
                if (vehicle.getVehicleType() != null) {
                    existingVehicle.setVehicleType(vehicle.getVehicleType());
                }
                if (vehicle.getUsed() != null) {
                    existingVehicle.setUsed(vehicle.getUsed());
                }
                if (vehicle.getUserId() != null) {
                    existingVehicle.setUserId(vehicle.getUserId());
                }

                return existingVehicle;
            })
            .map(vehicleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicle.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicles} : get all the vehicles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicles in body.
     */
    @GetMapping("/vehicles")
    public List<Vehicle> getAllVehicles() {
        log.debug("REST request to get all Vehicles");
        return vehicleRepository.findAll();
    }

    /**
     * {@code GET  /vehicles/:id} : get the "id" vehicle.
     *
     * @param id the id of the vehicle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<Vehicle> getVehicle(@PathVariable Long id) {
        log.debug("REST request to get Vehicle : {}", id);
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vehicle);
    }

    /**
     * {@code DELETE  /vehicles/:id} : delete the "id" vehicle.
     *
     * @param id the id of the vehicle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        log.debug("REST request to delete Vehicle : {}", id);
        vehicleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
