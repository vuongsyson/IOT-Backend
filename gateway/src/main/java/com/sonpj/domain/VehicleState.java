package com.sonpj.domain;

import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A VehicleState.
 */
@Table("vehicle_state")
public class VehicleState implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("speed")
    private Integer speed;

    @Column("lat")
    private Double lat;

    @Column("lon")
    private Double lon;

    @Column("error")
    private Integer error;

    @Column("serial_number")
    private String serialNumber;

    @Column("status")
    private String status;

    @Column("odo")
    private Double odo;

    @Column("power")
    private Double power;

    @Column("throttle")
    private Double throttle;

    @Column("time")
    private Instant time;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VehicleState id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSpeed() {
        return this.speed;
    }

    public VehicleState speed(Integer speed) {
        this.setSpeed(speed);
        return this;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Double getLat() {
        return this.lat;
    }

    public VehicleState lat(Double lat) {
        this.setLat(lat);
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return this.lon;
    }

    public VehicleState lon(Double lon) {
        this.setLon(lon);
        return this;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Integer getError() {
        return this.error;
    }

    public VehicleState error(Integer error) {
        this.setError(error);
        return this;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public VehicleState serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getStatus() {
        return this.status;
    }

    public VehicleState status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getOdo() {
        return this.odo;
    }

    public VehicleState odo(Double odo) {
        this.setOdo(odo);
        return this;
    }

    public void setOdo(Double odo) {
        this.odo = odo;
    }

    public Double getPower() {
        return this.power;
    }

    public VehicleState power(Double power) {
        this.setPower(power);
        return this;
    }

    public void setPower(Double power) {
        this.power = power;
    }

    public Double getThrottle() {
        return this.throttle;
    }

    public VehicleState throttle(Double throttle) {
        this.setThrottle(throttle);
        return this;
    }

    public void setThrottle(Double throttle) {
        this.throttle = throttle;
    }

    public Instant getTime() {
        return this.time;
    }

    public VehicleState time(Instant time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleState)) {
            return false;
        }
        return id != null && id.equals(((VehicleState) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleState{" +
            "id=" + getId() +
            ", speed=" + getSpeed() +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            ", error=" + getError() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", status='" + getStatus() + "'" +
            ", odo=" + getOdo() +
            ", power=" + getPower() +
            ", throttle=" + getThrottle() +
            ", time='" + getTime() + "'" +
            "}";
    }
}
