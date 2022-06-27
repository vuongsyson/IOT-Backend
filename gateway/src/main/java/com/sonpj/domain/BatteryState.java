package com.sonpj.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A BatteryState.
 */
@Table("battery_state")
public class BatteryState implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("serial_number")
    private String serialNumber;

    @NotNull(message = "must not be null")
    @Column("vol")
    private Integer vol;

    @NotNull(message = "must not be null")
    @Column("cur")
    private Integer cur;

    @NotNull(message = "must not be null")
    @Column("soc")
    private Integer soc;

    @NotNull(message = "must not be null")
    @Column("soh")
    private Integer soh;

    @NotNull(message = "must not be null")
    @Column("state")
    private Integer state;

    @NotNull(message = "must not be null")
    @Column("status")
    private Integer status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BatteryState id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public BatteryState serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getVol() {
        return this.vol;
    }

    public BatteryState vol(Integer vol) {
        this.setVol(vol);
        return this;
    }

    public void setVol(Integer vol) {
        this.vol = vol;
    }

    public Integer getCur() {
        return this.cur;
    }

    public BatteryState cur(Integer cur) {
        this.setCur(cur);
        return this;
    }

    public void setCur(Integer cur) {
        this.cur = cur;
    }

    public Integer getSoc() {
        return this.soc;
    }

    public BatteryState soc(Integer soc) {
        this.setSoc(soc);
        return this;
    }

    public void setSoc(Integer soc) {
        this.soc = soc;
    }

    public Integer getSoh() {
        return this.soh;
    }

    public BatteryState soh(Integer soh) {
        this.setSoh(soh);
        return this;
    }

    public void setSoh(Integer soh) {
        this.soh = soh;
    }

    public Integer getState() {
        return this.state;
    }

    public BatteryState state(Integer state) {
        this.setState(state);
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getStatus() {
        return this.status;
    }

    public BatteryState status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BatteryState)) {
            return false;
        }
        return id != null && id.equals(((BatteryState) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BatteryState{" +
            "id=" + getId() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", vol=" + getVol() +
            ", cur=" + getCur() +
            ", soc=" + getSoc() +
            ", soh=" + getSoh() +
            ", state=" + getState() +
            ", status=" + getStatus() +
            "}";
    }
}
