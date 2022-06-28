package com.sonpj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Battery.
 */
@Table("battery")
public class Battery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("serial_no")
    private String serialNo;

    @NotNull(message = "must not be null")
    @Column("hw_version")
    private Integer hwVersion;

    @NotNull(message = "must not be null")
    @Column("sw_version")
    private Integer swVersion;

    @NotNull(message = "must not be null")
    @Column("manufacture_date")
    private String manufactureDate;

    @NotNull(message = "must not be null")
    @Column("capacity")
    private Integer capacity;

    @NotNull(message = "must not be null")
    @Column("max_charge")
    private Integer maxCharge;

    @NotNull(message = "must not be null")
    @Column("max_discarge")
    private Integer maxDiscarge;

    @NotNull(message = "must not be null")
    @Column("max_vol")
    private Integer maxVol;

    @NotNull(message = "must not be null")
    @Column("min_vol")
    private Integer minVol;

    @NotNull(message = "must not be null")
    @Column("used")
    private Boolean used;

    @NotNull(message = "must not be null")
    @Column("soc")
    private Integer soc;

    @NotNull(message = "must not be null")
    @Column("soh")
    private Integer soh;

    @NotNull(message = "must not be null")
    @Column("temp")
    private Integer temp;

    @NotNull(message = "must not be null")
    @Column("owner_id")
    private Long ownerId;

    @NotNull(message = "must not be null")
    @Column("renter_id")
    private Long renterId;

    @NotNull(message = "must not be null")
    @Column("cycle_count")
    private Long cycleCount;

    @Transient
    @JsonIgnoreProperties(value = { "batteries" }, allowSetters = true)
    private RentalHistory rentalHistory;

    @Column("rental_history_id")
    private Long rentalHistoryId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Battery id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public Battery serialNo(String serialNo) {
        this.setSerialNo(serialNo);
        return this;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getHwVersion() {
        return this.hwVersion;
    }

    public Battery hwVersion(Integer hwVersion) {
        this.setHwVersion(hwVersion);
        return this;
    }

    public void setHwVersion(Integer hwVersion) {
        this.hwVersion = hwVersion;
    }

    public Integer getSwVersion() {
        return this.swVersion;
    }

    public Battery swVersion(Integer swVersion) {
        this.setSwVersion(swVersion);
        return this;
    }

    public void setSwVersion(Integer swVersion) {
        this.swVersion = swVersion;
    }

    public String getManufactureDate() {
        return this.manufactureDate;
    }

    public Battery manufactureDate(String manufactureDate) {
        this.setManufactureDate(manufactureDate);
        return this;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public Battery capacity(Integer capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getMaxCharge() {
        return this.maxCharge;
    }

    public Battery maxCharge(Integer maxCharge) {
        this.setMaxCharge(maxCharge);
        return this;
    }

    public void setMaxCharge(Integer maxCharge) {
        this.maxCharge = maxCharge;
    }

    public Integer getMaxDiscarge() {
        return this.maxDiscarge;
    }

    public Battery maxDiscarge(Integer maxDiscarge) {
        this.setMaxDiscarge(maxDiscarge);
        return this;
    }

    public void setMaxDiscarge(Integer maxDiscarge) {
        this.maxDiscarge = maxDiscarge;
    }

    public Integer getMaxVol() {
        return this.maxVol;
    }

    public Battery maxVol(Integer maxVol) {
        this.setMaxVol(maxVol);
        return this;
    }

    public void setMaxVol(Integer maxVol) {
        this.maxVol = maxVol;
    }

    public Integer getMinVol() {
        return this.minVol;
    }

    public Battery minVol(Integer minVol) {
        this.setMinVol(minVol);
        return this;
    }

    public void setMinVol(Integer minVol) {
        this.minVol = minVol;
    }

    public Boolean getUsed() {
        return this.used;
    }

    public Battery used(Boolean used) {
        this.setUsed(used);
        return this;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Integer getSoc() {
        return this.soc;
    }

    public Battery soc(Integer soc) {
        this.setSoc(soc);
        return this;
    }

    public void setSoc(Integer soc) {
        this.soc = soc;
    }

    public Integer getSoh() {
        return this.soh;
    }

    public Battery soh(Integer soh) {
        this.setSoh(soh);
        return this;
    }

    public void setSoh(Integer soh) {
        this.soh = soh;
    }

    public Integer getTemp() {
        return this.temp;
    }

    public Battery temp(Integer temp) {
        this.setTemp(temp);
        return this;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public Battery ownerId(Long ownerId) {
        this.setOwnerId(ownerId);
        return this;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getRenterId() {
        return this.renterId;
    }

    public Battery renterId(Long renterId) {
        this.setRenterId(renterId);
        return this;
    }

    public void setRenterId(Long renterId) {
        this.renterId = renterId;
    }

    public Long getCycleCount() {
        return this.cycleCount;
    }

    public Battery cycleCount(Long cycleCount) {
        this.setCycleCount(cycleCount);
        return this;
    }

    public void setCycleCount(Long cycleCount) {
        this.cycleCount = cycleCount;
    }

    public RentalHistory getRentalHistory() {
        return this.rentalHistory;
    }

    public void setRentalHistory(RentalHistory rentalHistory) {
        this.rentalHistory = rentalHistory;
        this.rentalHistoryId = rentalHistory != null ? rentalHistory.getId() : null;
    }

    public Battery rentalHistory(RentalHistory rentalHistory) {
        this.setRentalHistory(rentalHistory);
        return this;
    }

    public Long getRentalHistoryId() {
        return this.rentalHistoryId;
    }

    public void setRentalHistoryId(Long rentalHistory) {
        this.rentalHistoryId = rentalHistory;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Battery)) {
            return false;
        }
        return id != null && id.equals(((Battery) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Battery{" +
            "id=" + getId() +
            ", serialNo='" + getSerialNo() + "'" +
            ", hwVersion=" + getHwVersion() +
            ", swVersion=" + getSwVersion() +
            ", manufactureDate='" + getManufactureDate() + "'" +
            ", capacity=" + getCapacity() +
            ", maxCharge=" + getMaxCharge() +
            ", maxDiscarge=" + getMaxDiscarge() +
            ", maxVol=" + getMaxVol() +
            ", minVol=" + getMinVol() +
            ", used='" + getUsed() + "'" +
            ", soc=" + getSoc() +
            ", soh=" + getSoh() +
            ", temp=" + getTemp() +
            ", ownerId=" + getOwnerId() +
            ", renterId=" + getRenterId() +
            ", cycleCount=" + getCycleCount() +
            "}";
    }
}
