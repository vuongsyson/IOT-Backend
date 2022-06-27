package com.sonpj.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vehicle.
 */
@Entity
@Table(name = "vehicle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "clearance")
    private Integer clearance;

    @Column(name = "max_power")
    private Integer maxPower;

    @Column(name = "max_speed")
    private Integer maxSpeed;

    @Column(name = "max_load")
    private Integer maxLoad;

    @Column(name = "weight_total")
    private Integer weightTotal;

    @Column(name = "max_distance")
    private Integer maxDistance;

    @Column(name = "wheel_base")
    private Integer wheelBase;

    @Column(name = "hw_version")
    private Integer hwVersion;

    @Column(name = "sw_version")
    private Integer swVersion;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "manufacture_date")
    private String manufactureDate;

    @Column(name = "lot_number")
    private Integer lotNumber;

    @Column(name = "color")
    private String color;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "used")
    private Boolean used;

    @Column(name = "user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vehicle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getClearance() {
        return this.clearance;
    }

    public Vehicle clearance(Integer clearance) {
        this.setClearance(clearance);
        return this;
    }

    public void setClearance(Integer clearance) {
        this.clearance = clearance;
    }

    public Integer getMaxPower() {
        return this.maxPower;
    }

    public Vehicle maxPower(Integer maxPower) {
        this.setMaxPower(maxPower);
        return this;
    }

    public void setMaxPower(Integer maxPower) {
        this.maxPower = maxPower;
    }

    public Integer getMaxSpeed() {
        return this.maxSpeed;
    }

    public Vehicle maxSpeed(Integer maxSpeed) {
        this.setMaxSpeed(maxSpeed);
        return this;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Integer getMaxLoad() {
        return this.maxLoad;
    }

    public Vehicle maxLoad(Integer maxLoad) {
        this.setMaxLoad(maxLoad);
        return this;
    }

    public void setMaxLoad(Integer maxLoad) {
        this.maxLoad = maxLoad;
    }

    public Integer getWeightTotal() {
        return this.weightTotal;
    }

    public Vehicle weightTotal(Integer weightTotal) {
        this.setWeightTotal(weightTotal);
        return this;
    }

    public void setWeightTotal(Integer weightTotal) {
        this.weightTotal = weightTotal;
    }

    public Integer getMaxDistance() {
        return this.maxDistance;
    }

    public Vehicle maxDistance(Integer maxDistance) {
        this.setMaxDistance(maxDistance);
        return this;
    }

    public void setMaxDistance(Integer maxDistance) {
        this.maxDistance = maxDistance;
    }

    public Integer getWheelBase() {
        return this.wheelBase;
    }

    public Vehicle wheelBase(Integer wheelBase) {
        this.setWheelBase(wheelBase);
        return this;
    }

    public void setWheelBase(Integer wheelBase) {
        this.wheelBase = wheelBase;
    }

    public Integer getHwVersion() {
        return this.hwVersion;
    }

    public Vehicle hwVersion(Integer hwVersion) {
        this.setHwVersion(hwVersion);
        return this;
    }

    public void setHwVersion(Integer hwVersion) {
        this.hwVersion = hwVersion;
    }

    public Integer getSwVersion() {
        return this.swVersion;
    }

    public Vehicle swVersion(Integer swVersion) {
        this.setSwVersion(swVersion);
        return this;
    }

    public void setSwVersion(Integer swVersion) {
        this.swVersion = swVersion;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public Vehicle serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getManufactureDate() {
        return this.manufactureDate;
    }

    public Vehicle manufactureDate(String manufactureDate) {
        this.setManufactureDate(manufactureDate);
        return this;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Integer getLotNumber() {
        return this.lotNumber;
    }

    public Vehicle lotNumber(Integer lotNumber) {
        this.setLotNumber(lotNumber);
        return this;
    }

    public void setLotNumber(Integer lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getColor() {
        return this.color;
    }

    public Vehicle color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVehicleType() {
        return this.vehicleType;
    }

    public Vehicle vehicleType(String vehicleType) {
        this.setVehicleType(vehicleType);
        return this;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Boolean getUsed() {
        return this.used;
    }

    public Vehicle used(Boolean used) {
        this.setUsed(used);
        return this;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Vehicle userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicle)) {
            return false;
        }
        return id != null && id.equals(((Vehicle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicle{" +
            "id=" + getId() +
            ", clearance=" + getClearance() +
            ", maxPower=" + getMaxPower() +
            ", maxSpeed=" + getMaxSpeed() +
            ", maxLoad=" + getMaxLoad() +
            ", weightTotal=" + getWeightTotal() +
            ", maxDistance=" + getMaxDistance() +
            ", wheelBase=" + getWheelBase() +
            ", hwVersion=" + getHwVersion() +
            ", swVersion=" + getSwVersion() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", manufactureDate='" + getManufactureDate() + "'" +
            ", lotNumber=" + getLotNumber() +
            ", color='" + getColor() + "'" +
            ", vehicleType='" + getVehicleType() + "'" +
            ", used='" + getUsed() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
