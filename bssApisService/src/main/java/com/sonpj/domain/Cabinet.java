package com.sonpj.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cabinet.
 */
@Entity
@Table(name = "cabinet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cabinet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "bss_id", nullable = false)
    private Long bssId;

    @Column(name = "bp_id")
    private Long bpId;

    @Column(name = "bp_ready")
    private Boolean bpReady;

    @Column(name = "swap_no")
    private Long swapNo;

    @Column(name = "state_code")
    private Integer stateCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cabinet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBssId() {
        return this.bssId;
    }

    public Cabinet bssId(Long bssId) {
        this.setBssId(bssId);
        return this;
    }

    public void setBssId(Long bssId) {
        this.bssId = bssId;
    }

    public Long getBpId() {
        return this.bpId;
    }

    public Cabinet bpId(Long bpId) {
        this.setBpId(bpId);
        return this;
    }

    public void setBpId(Long bpId) {
        this.bpId = bpId;
    }

    public Boolean getBpReady() {
        return this.bpReady;
    }

    public Cabinet bpReady(Boolean bpReady) {
        this.setBpReady(bpReady);
        return this;
    }

    public void setBpReady(Boolean bpReady) {
        this.bpReady = bpReady;
    }

    public Long getSwapNo() {
        return this.swapNo;
    }

    public Cabinet swapNo(Long swapNo) {
        this.setSwapNo(swapNo);
        return this;
    }

    public void setSwapNo(Long swapNo) {
        this.swapNo = swapNo;
    }

    public Integer getStateCode() {
        return this.stateCode;
    }

    public Cabinet stateCode(Integer stateCode) {
        this.setStateCode(stateCode);
        return this;
    }

    public void setStateCode(Integer stateCode) {
        this.stateCode = stateCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cabinet)) {
            return false;
        }
        return id != null && id.equals(((Cabinet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cabinet{" +
            "id=" + getId() +
            ", bssId=" + getBssId() +
            ", bpId=" + getBpId() +
            ", bpReady='" + getBpReady() + "'" +
            ", swapNo=" + getSwapNo() +
            ", stateCode=" + getStateCode() +
            "}";
    }
}
