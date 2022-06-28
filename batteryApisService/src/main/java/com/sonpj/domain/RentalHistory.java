package com.sonpj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RentalHistory.
 */
@Entity
@Table(name = "rental_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RentalHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "battery_id")
    private Long batteryId;

    @Column(name = "time_start")
    private Instant timeStart;

    @Column(name = "time_end")
    private Instant timeEnd;

    @OneToMany(mappedBy = "rentalHistory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "rentalHistory" }, allowSetters = true)
    private Set<Battery> batteries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RentalHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public RentalHistory userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBatteryId() {
        return this.batteryId;
    }

    public RentalHistory batteryId(Long batteryId) {
        this.setBatteryId(batteryId);
        return this;
    }

    public void setBatteryId(Long batteryId) {
        this.batteryId = batteryId;
    }

    public Instant getTimeStart() {
        return this.timeStart;
    }

    public RentalHistory timeStart(Instant timeStart) {
        this.setTimeStart(timeStart);
        return this;
    }

    public void setTimeStart(Instant timeStart) {
        this.timeStart = timeStart;
    }

    public Instant getTimeEnd() {
        return this.timeEnd;
    }

    public RentalHistory timeEnd(Instant timeEnd) {
        this.setTimeEnd(timeEnd);
        return this;
    }

    public void setTimeEnd(Instant timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Set<Battery> getBatteries() {
        return this.batteries;
    }

    public void setBatteries(Set<Battery> batteries) {
        if (this.batteries != null) {
            this.batteries.forEach(i -> i.setRentalHistory(null));
        }
        if (batteries != null) {
            batteries.forEach(i -> i.setRentalHistory(this));
        }
        this.batteries = batteries;
    }

    public RentalHistory batteries(Set<Battery> batteries) {
        this.setBatteries(batteries);
        return this;
    }

    public RentalHistory addBattery(Battery battery) {
        this.batteries.add(battery);
        battery.setRentalHistory(this);
        return this;
    }

    public RentalHistory removeBattery(Battery battery) {
        this.batteries.remove(battery);
        battery.setRentalHistory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RentalHistory)) {
            return false;
        }
        return id != null && id.equals(((RentalHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RentalHistory{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", batteryId=" + getBatteryId() +
            ", timeStart='" + getTimeStart() + "'" +
            ", timeEnd='" + getTimeEnd() + "'" +
            "}";
    }
}
