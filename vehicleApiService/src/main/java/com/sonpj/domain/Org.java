package com.sonpj.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Org.
 */
@Entity
@Table(name = "org")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Org implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "org_id")
    private Long orgId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Org id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public Org orgId(Long orgId) {
        this.setOrgId(orgId);
        return this;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Org)) {
            return false;
        }
        return id != null && id.equals(((Org) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Org{" +
            "id=" + getId() +
            ", orgId=" + getOrgId() +
            "}";
    }
}
