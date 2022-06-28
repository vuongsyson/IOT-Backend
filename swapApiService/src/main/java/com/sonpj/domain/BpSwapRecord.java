package com.sonpj.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BpSwapRecord.
 */
@Entity
@Table(name = "bp_swap_record")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BpSwapRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "old_bat")
    private String oldBat;

    @Column(name = "new_bat")
    private String newBat;

    @Column(name = "old_cab")
    private String oldCab;

    @Column(name = "new_cab")
    private String newCab;

    @Column(name = "bss")
    private String bss;

    @Column(name = "user")
    private Long user;

    @Column(name = "state")
    private Integer state;

    @Column(name = "error")
    private Integer error;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BpSwapRecord id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOldBat() {
        return this.oldBat;
    }

    public BpSwapRecord oldBat(String oldBat) {
        this.setOldBat(oldBat);
        return this;
    }

    public void setOldBat(String oldBat) {
        this.oldBat = oldBat;
    }

    public String getNewBat() {
        return this.newBat;
    }

    public BpSwapRecord newBat(String newBat) {
        this.setNewBat(newBat);
        return this;
    }

    public void setNewBat(String newBat) {
        this.newBat = newBat;
    }

    public String getOldCab() {
        return this.oldCab;
    }

    public BpSwapRecord oldCab(String oldCab) {
        this.setOldCab(oldCab);
        return this;
    }

    public void setOldCab(String oldCab) {
        this.oldCab = oldCab;
    }

    public String getNewCab() {
        return this.newCab;
    }

    public BpSwapRecord newCab(String newCab) {
        this.setNewCab(newCab);
        return this;
    }

    public void setNewCab(String newCab) {
        this.newCab = newCab;
    }

    public String getBss() {
        return this.bss;
    }

    public BpSwapRecord bss(String bss) {
        this.setBss(bss);
        return this;
    }

    public void setBss(String bss) {
        this.bss = bss;
    }

    public Long getUser() {
        return this.user;
    }

    public BpSwapRecord user(Long user) {
        this.setUser(user);
        return this;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Integer getState() {
        return this.state;
    }

    public BpSwapRecord state(Integer state) {
        this.setState(state);
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getError() {
        return this.error;
    }

    public BpSwapRecord error(Integer error) {
        this.setError(error);
        return this;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BpSwapRecord)) {
            return false;
        }
        return id != null && id.equals(((BpSwapRecord) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BpSwapRecord{" +
            "id=" + getId() +
            ", oldBat='" + getOldBat() + "'" +
            ", newBat='" + getNewBat() + "'" +
            ", oldCab='" + getOldCab() + "'" +
            ", newCab='" + getNewCab() + "'" +
            ", bss='" + getBss() + "'" +
            ", user=" + getUser() +
            ", state=" + getState() +
            ", error=" + getError() +
            "}";
    }
}
